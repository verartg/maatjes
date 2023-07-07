package com.example.maatjes.controllers;

import com.example.maatjes.dtos.outputDtos.AccountOutputDto;
import com.example.maatjes.dtos.inputDtos.AccountInputDto;
import com.example.maatjes.services.AccountService;
import com.example.maatjes.util.FieldErrorHandling;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
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

    @PostMapping("/createaccount")
    public ResponseEntity<Object> createAccount(@Valid @RequestBody AccountInputDto accountInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(accountService.createAccount(accountInputDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AccountOutputDto>> getAccountsByFilters(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Boolean givesHelp) {
        return new ResponseEntity<>(accountService.getAccountsByFilters(city, givesHelp), HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<AccountOutputDto> getAccount(@PathVariable("accountId") Long accountId) {
        return new ResponseEntity<>(accountService.getAccount(accountId), HttpStatus.OK);
    }

//    @GetMapping("/{accountId}")
//    @PreAuthorize("#username == authentication.principal.username")
//    public ResponseEntity<AccountOutputDto> getAccount(@PathVariable("accountId") Long accountId, @AuthenticationPrincipal(expression = "username") String username) {
//        return new ResponseEntity<>(accountService.getAccount(username), HttpStatus.OK);
//    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Object> updateAccount(@PathVariable Long accountId, @Valid @RequestBody AccountInputDto accountInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(accountService.updateAccount(accountId, accountInputDto), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{accountId}/upload")
    public ResponseEntity<Object> uploadIdentificationDocument(@PathVariable Long accountId, @RequestParam("file") MultipartFile file) throws Exception{
        return new ResponseEntity<>(accountService.uploadIdentificationDocument(accountId, file), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{accountId}/upload")
    public ResponseEntity<Object> removeIdentificationDocument(@PathVariable Long accountId) {
        accountService.removeIdentificationDocument(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Object> removeAccount(@PathVariable Long accountId) {
        accountService.removeAccount(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
