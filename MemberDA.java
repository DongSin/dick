
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da;

import domain.Book;
import domain.Member;
import domain.Purchase;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Looi Dong Sin
 */
public class MemberDA {

    private static String host = "jdbc:derby://localhost:1527/LondonBookstore";
    private static String user = "nbuser";
    private static String pass = "nbuser";
    private static Connection conn;
    private static PreparedStatement stmt;
    private static String sql;
    private static ResultSet rs;

    public MemberDA() throws SQLException {

    }

    private static void createConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(host, user, pass);
        }
    }

    public static Member searchMember(String id) throws SQLException, IOException {
        closeConnection();
        createConnection();
        Member member = null;
        sql = "select * from member where member_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, id);
        rs = stmt.executeQuery();
        if (rs.next()) {
            String name = rs.getString("member_name");
            String password = rs.getString("member_password");
            String email = rs.getString("email");
            Book[] cart = getCart(id);
            Purchase[] purchase = getPurchase(id);
            member = new Member(id, name, password, email, cart, purchase);
        }

        return member;
    }


    public static Member searchMemberByName(String name) throws SQLException, IOException {
        closeConnection();
        createConnection();
        Member member = null;
        sql = "select * from member where member_name = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);     
        rs = stmt.executeQuery();
        if (rs.next()) {
            String id = rs.getString("member_id");
            String password = rs.getString("member_password");
            String email = rs.getString("email");
            Book[] cart = getCart(id);
            Purchase[] purchase = getPurchase(id);
            member = new Member(id, name, password, email, cart, purchase);
        }

        return member;
    }

    public static void addMember(Member member) throws SQLException {
        closeConnection();
        createConnection();
        sql = "insert into member values(?,?,?,?)";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, member.getMemberId());
        stmt.setString(2, member.getMemberName());
        stmt.setString(3, member.getPassword());
        stmt.setString(4, member.getEmail());
        stmt.executeUpdate();
        closeConnection();
    }

    public static void updateMember(Member member) throws SQLException {
        closeConnection();
        createConnection();
        sql = "update member set member_name = ?, member_password = ?, email = ? where member_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, member.getMemberName());
        stmt.setString(2, member.getPassword());
        stmt.setString(3, member.getEmail());
        stmt.setString(4, member.getMemberId());
        stmt.executeUpdate();
        closeConnection();
    }

    public static void deleteMember(String id) throws SQLException {
        closeConnection();
        createConnection();
        sql = "delete from member where member_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, id);
        stmt.executeUpdate();
        closeConnection();
    }

    private static Book[] getCart(String id) throws SQLException, IOException {
        closeConnection();
        createConnection();
        sql = "select book_id from cart where member_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, id);
        rs = stmt.executeQuery();
        String[] bookId = null;
        int i = 0;
        Book[] book = null;
        while (rs.next()) {
            bookId[i] = rs.getString("book_id");
            book[i] = new BookDA().searchBook(bookId[i]);
            i++;
        }

        return book;
    }

    private static Purchase[] getPurchase(String id) throws SQLException, IOException {
        closeConnection();
        createConnection();
        sql = "select purchase_date from purchase where member_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, id);
        rs = stmt.executeQuery();
        Purchase[] purchases = null;
        String bookId = null;
        Book book = null;
        Date[] dates = null;
        int[] quantities = null;
        int i = 0;
        while (rs.next()) {
            dates[i] = rs.getDate("purchase_date");
            quantities[i] = rs.getInt("quantity");
            bookId = rs.getString("book_id");
            book = BookDA.searchBook(bookId);
            int day = dates[i].getDate();
            int month = dates[i].getMonth();
            int year = dates[i].getYear();
            purchases[i] = new Purchase(day, month, year, book, quantities[i]);
            i++;
        }

        return purchases;
    }

//    private static Book[] getPurchaseBooks(String id, Date date) throws SQLException {
//        closeConnection();
//        createConnection();
//        sql = "select book_id from purchase where member_id = ? and purchase_date = ?";
//        stmt = conn.prepareStatement(sql);
//        stmt.setString(1, id);
//        stmt.setDate(2, date);
//        rs = stmt.executeQuery();
//        String[] bookIds = null;
//        Book[] books = null;
//        int i = 0;
//        while (rs.next()) {
//            bookIds[i] = rs.getString("book_id");
//            books[i] = new BookDA().searchBook(bookIds[i]);
//            i++;
//        }
//
//        return books;
//    }

    public static boolean login(String name, String pass) throws SQLException {
        closeConnection();
        createConnection();
        sql = "select * from member where member_name = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        rs = stmt.executeQuery();
        String correctPass = "";
        if (rs.next()) {
            correctPass = rs.getString("member_password");
        }
        Boolean login = pass.equals(correctPass);
        return login;
    }

    private static void closeConnection() throws SQLException {

        if (rs != null && !(rs.isClosed())) {
            rs.close();
        }
        if (stmt != null && !(stmt.isClosed())) {
            stmt.close();
        }
        if (conn != null && !(conn.isClosed())) {
            conn.close();
        }

    }
    
    private static void clearCart(String memberId) throws SQLException {
        closeConnection();
        createConnection();
        sql = "delete from cart where member_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, memberId);
        stmt.executeUpdate();
        closeConnection();
    }

    private static void insertCart(String memberId, Book[] cart) throws SQLException {
        closeConnection();
        createConnection();
        sql = "insert into cart values(?,?)";
        stmt = conn.prepareStatement(sql);
        for (int i = 0; i < cart.length; i++) {
            Book book = cart[i];
            if (book != null) {
                stmt.setString(1, memberId);
                stmt.setString(2, book.getBookId());
                stmt.executeUpdate();
            }
        }
        closeConnection();
    }

    private static void clearPurchaseHistory(String memberId) throws SQLException {
        closeConnection();
        createConnection();
        sql = "delete from purchase where member_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, memberId);
        stmt.executeUpdate();
        closeConnection();
    }

    private static void insertPurchaseHistory(String memberId, Purchase[] p) throws SQLException {
        closeConnection();
        createConnection();
        sql = "insert into purchase values(?,?,?,?)";
        stmt = conn.prepareStatement(sql);
        int i = 0;
        while (i < p.length && p[i] != null) {
            stmt.setString(1, memberId);
            stmt.setString(2, p[i].getPurchaseBooks().getBookId());
            Date date = new Date(p[i].getPurchaseDay(), p[i].getPurchaseMonth(), p[i].getPurchaseYear());
            stmt.setDate(3, date);
            stmt.setInt(4, p[i].getQuantity());
            stmt.executeUpdate();
            i++;
        }
        
        closeConnection();
    }
    
    public static int getMemberCount() throws SQLException{
        closeConnection();
        createConnection();
        int count;
        String lastId;
        sql = "select member_id from member order by member_id desc FETCH FIRST 1 ROWS ONLY";
        stmt = conn.prepareStatement(sql);
        rs = stmt.executeQuery();
        if(rs.next()){
            lastId = rs.getString(1);
            lastId = lastId.replace('M', '0');
            count = Integer.parseInt(lastId);
        }else{
            count = 0;
        }
        closeConnection();
        return count;
        
    }

}
