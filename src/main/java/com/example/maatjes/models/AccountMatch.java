package com.example.maatjes.models;

import jakarta.persistence.*;

@Entity
public class AccountMatch {
    @EmbeddedId
    private AccountMatchKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "match_id")
    private Match match;

    public AccountMatchKey getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public Match getMatch() {
        return match;
    }

    public void setId(AccountMatchKey id) {
        this.id = id;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}