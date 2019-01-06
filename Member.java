
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import da.MemberDA;
import java.sql.SQLException;

/**
 *
 * @author Looi Dong Sin
 */
public class Member {
    
    private String memberId;
    private String memberName;
    private String password;
    private String email;
    private Book[] memberCart;
    private Purchase[] purchaseHistory;
    private static int count = 0;   

    public Member() {
    }

    public Member(String memberId, String memberName, String password, String email, Book[] memberCart, Purchase[] purchaseHistory) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.password = password;
        this.email = email;
        this.memberCart = memberCart;
        this.purchaseHistory = purchaseHistory;
        ;
    }
    
    

    public Member(String memberName, String password, String email, Book[] memberCart, Purchase[] purchaseHistory) throws SQLException {
        memberId = String.format("M%04d",MemberDA.getMemberCount()+1);
        this.memberName = memberName;
        this.password = password;
        this.email = email;
        this.memberCart = memberCart;
        this.purchaseHistory = purchaseHistory;     
    }
    public String getEmail() {
        return email;
    }

    public static int getCount() {
        return count;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Book[] getMemberCart() {
        return memberCart;
    }

    public void setMemberCart(Book[] memberCart) {
        this.memberCart = memberCart;
    }

    public Purchase[] getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(Purchase[] purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }
    
    

}