package com.example.maatjes.controllers;

import com.example.maatjes.dtos.AccountDto;
import com.example.maatjes.dtos.AccountInputDto;
import com.example.maatjes.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            @RequestParam(required = false) Boolean givesHelp
    ) {
        List<AccountDto> accountDtos;
        accountDtos = accountService.getAccountsByFilters(city, givesHelp);
        return ResponseEntity.ok().body(accountDtos);
    }
// in onderstaande code wordt gebruik gemaakt van <Optional>, dat werkte niet meer bij meerdere filters, maar is het dan nog wel oke?
//    public ResponseEntity<List<AccountDto>> getAccountsByFilters(@RequestParam(required = false) Optional<String> city, @RequestParam(required = false) Boolean givesHelp) {
//        List<AccountDto> accountDtos;
//        if (city.isEmpty()) {
//            accountDtos = accountService.getAccounts();
//        } else {
//            accountDtos = accountService.getAccountsByFilters(city.get());
//        }
//        return ResponseEntity.ok().body(accountDtos);
//    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeAccount(@PathVariable Long id) {
        accountService.removeAccount(id);
        return ResponseEntity.noContent().build();
    }

}
