//package com.example.maatjes.models;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class AccountMatch {
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("accountId")
//    @JoinColumn(name = "account_id")
//    private Account account;
//
//    @ManyToOne
//    @MapsId("matchId")
//    @JoinColumn(name = "match_id")
//    private Match match;
//}