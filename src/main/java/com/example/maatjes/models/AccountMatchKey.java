//package com.example.maatjes.models;
//
//import java.io.Serializable;
//import java.util.Objects;
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//
////Deze embeddable class wordt gebruikt als Embedded Id in de AccountMatch class
//@Embeddable
//public class AccountMatchKey implements Serializable {
//
//    @Column(name = "account_id")
//    private Long accountId;
//
//    @Column(name = "match_id")
//    private Long matchId;
//
//    public AccountMatchKey() {}
//    public AccountMatchKey(Long accountId, Long matchId) {
//        this.accountId = accountId;
//        this.matchId = matchId;
//    }
//
//    public Long getAccountId() {
//        return accountId;
//    }
//
//    public Long getMatchId() {
//        return matchId;
//    }
//
//    public void setAccountId(Long accountId) {
//        this.accountId = accountId;
//    }
//
//    public void setMatchId(Long matchId) {
//        this.matchId = matchId;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if(this == o) return true;
//        if(o == null || getClass() != o.getClass()) return false;
//        AccountMatchKey that = (AccountMatchKey) o;
//        return accountId.equals(that.accountId)&& matchId.equals(that.matchId);
//    }
//
//    @Override
//    public int hashCode() {return Objects.hash(accountId, matchId);}
//}
