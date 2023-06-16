package com.example.maatjes.controllers;

import com.example.maatjes.dtos.AccountDto;
import com.example.maatjes.dtos.AccountInputDto;
import com.example.maatjes.dtos.MatchDto;
import com.example.maatjes.services.AccountMatchService;
import com.example.maatjes.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AccountMatchService accountMatchService;

    public AccountController(AccountService accountService, AccountMatchService accountMatchService) {
        this.accountService = accountService;
        this.accountMatchService = accountMatchService;
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

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountInputDto account) {
        AccountDto dto = accountService.updateAccount(id, account);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/{id}/upload")
    public ResponseEntity<Object> uploadDocument(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws Exception {
        AccountDto dto = accountService.uploadDocument(id, file);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}/upload")
    public ResponseEntity<Object> removeDocument(@PathVariable Long id) {
        accountService.removeDocument(id);
        return ResponseEntity.noContent().build();
    }

    // Deze methode is om alle matches op te halen die aan een bepaalde account gekoppeld zijn.
    // Deze methode maakt gebruik van de accountMatchService.
    @GetMapping("/matches/{accountId}")
    public ResponseEntity<Collection<MatchDto>> getMatchesByAccountId(@PathVariable("accountId") Long accountId){
        return ResponseEntity.ok(accountMatchService.getMatchesByAccountId(accountId));
    }
}
