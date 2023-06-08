package com.example.maatjes.controllers;

import com.example.maatjes.dtos.AccountDto;
import com.example.maatjes.dtos.AccountInputDto;
import com.example.maatjes.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccounts() {
        List<AccountDto> accountDtos;
        accountDtos = accountService.getAccounts();

        return ResponseEntity.ok().body(accountDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable("id") Long id) {
        AccountDto account = accountService.getAccount(id);

        return ResponseEntity.ok().body(account);
    }

    @PostMapping("/createaccount")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountInputDto account) {
        AccountDto dto = accountService.createAccount(account);
        return ResponseEntity.created(null).body(dto);
    }
}
