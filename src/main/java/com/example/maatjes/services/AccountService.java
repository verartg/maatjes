package com.example.maatjes.services;

import com.example.maatjes.dtos.AccountDto;
import com.example.maatjes.dtos.AccountInputDto;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Account;
import com.example.maatjes.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountDto> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountDto> accountDtos = new ArrayList<>();
        for (Account account : accounts) {
            accountDtos.add(transferAccountToDto(account));
        }
        return accountDtos;
    }

    public AccountDto getAccount(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new RecordNotFoundException("Account not found");}
        Account account = optionalAccount.get();
        return transferAccountToDto(account);
    }

    public AccountDto createAccount(AccountInputDto accountDto) {
        Account account = transferToAccount(accountDto);
        accountRepository.save(account);
        return transferAccountToDto(account);
    }



    public AccountDto transferAccountToDto(Account account) {
        AccountDto accountDto = new AccountDto();

        accountDto.id = account.getId();
        accountDto.name = account.getName();
        accountDto.age = account.getAge();
        accountDto.sex = account.getSex();
        accountDto.phoneNumber = account.getPhoneNumber();
        accountDto.emailAddress = account.getEmailAddress();
        accountDto.street = account.getStreet();
        accountDto.houseNumber = account.getHouseNumber();
        accountDto.postalCode = account.getPostalCode();
        accountDto.city = account.getCity();
        accountDto.bio = account.getBio();
        accountDto.givesHelp = account.isGivesHelp();
        accountDto.needsHelp = account.isNeedsHelp();
        accountDto.activitiesToGive = account.getActivitiesToGive();
        accountDto.activitiesToReceive = account.getActivitiesToReceive();
        accountDto.availability = account.getAvailability();
        accountDto.frequency = account.getFrequency();
        return accountDto;
        }

    public Account transferToAccount(AccountInputDto accountDto) {
        var account = new Account();

        account.setName(accountDto.getName());
        account.setAge(accountDto.getAge());
        account.setSex(accountDto.getSex());
        account.setPhoneNumber(accountDto.getPhoneNumber());
        account.setEmailAddress(accountDto.getEmailAddress());
        account.setStreet(accountDto.getStreet());
        account.setHouseNumber(accountDto.getHouseNumber());
        account.setPostalCode(accountDto.getPostalCode());
        account.setCity(accountDto.getCity());
        account.setBio(accountDto.getBio());
        account.setGivesHelp(accountDto.isGivesHelp());
        account.setNeedsHelp(accountDto.isNeedsHelp());
        account.setActivitiesToGive(accountDto.getActivitiesToGive());
        account.setActivitiesToReceive(accountDto.getActivitiesToReceive());
        account.setAvailability(accountDto.getAvailability());
        account.setFrequency(accountDto.getFrequency());

        return account;

        }
}
