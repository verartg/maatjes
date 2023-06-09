package com.example.maatjes.controllers;

import com.example.maatjes.dtos.AccountDto;
import com.example.maatjes.dtos.AccountInputDto;
import com.example.maatjes.exceptions.FileSizeExceededException;
import com.example.maatjes.services.AccountService;
import com.example.maatjes.util.FileSizeExceededHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccountsByFilters(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Boolean givesHelp) {
        return new ResponseEntity<>(accountService.getAccountsByFilters(city, givesHelp), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable("id") Long id) {
        return new ResponseEntity<>(accountService.getAccount(id), HttpStatus.OK);
    }

    //todo bindingresult?
    @PostMapping("/createaccount")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountInputDto account) {
        return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.CREATED);
    }

    //todo bindingresult?
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountInputDto account) {
        return new ResponseEntity<>(accountService.updateAccount(id, account), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}/upload")
    public ResponseEntity<Object> uploadDocument(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws Exception{
        return new ResponseEntity<>(accountService.uploadDocument(id, file), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeAccount(@PathVariable Long id) {
        accountService.removeAccount(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/upload")
    public ResponseEntity<Object> removeDocument(@PathVariable Long id) {
        accountService.removeDocument(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
