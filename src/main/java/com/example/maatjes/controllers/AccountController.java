package com.example.maatjes.controllers;

import com.example.maatjes.dtos.outputDtos.AccountOutputDto;
import com.example.maatjes.dtos.inputDtos.AccountInputDto;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.services.AccountService;
import com.example.maatjes.util.FieldErrorHandling;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{username}")
    public ResponseEntity<AccountOutputDto> getAccount(@PathVariable String username) {
        return new ResponseEntity<>(accountService.getAccount(username), HttpStatus.OK);
    }

    @GetMapping("/{username}/identification")
    public ResponseEntity<byte[]> getIdentificationDocument(@PathVariable String username) throws RecordNotFoundException {
        byte[] document = accountService.getIdentificationDocument(username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(document);
    }

    @PutMapping("/{username}")
    public ResponseEntity<Object> updateAccount(@PathVariable String username, @Valid @RequestBody AccountInputDto accountInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }

        AccountOutputDto updatedAccount = accountService.updateAccount(username, accountInputDto);
        return ResponseEntity.accepted().body(updatedAccount);
    }

    @PutMapping("/{username}/identification")
    public ResponseEntity<Object> uploadIdentificationDocument(@PathVariable String username, @RequestParam("file") MultipartFile file) throws Exception{
        return new ResponseEntity<>(accountService.uploadIdentificationDocument(username, file), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{username}/identification")
    public ResponseEntity<String> removeIdentificationDocument(@PathVariable String username) {
        String message = accountService.removeIdentificationDocument(username);
        return ResponseEntity.ok().body(message);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> removeAccount(@PathVariable String username) {
        String message = accountService.removeAccount(username);
        return ResponseEntity.ok().body(message);
    }
}
