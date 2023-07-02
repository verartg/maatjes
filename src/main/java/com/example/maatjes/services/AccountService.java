package com.example.maatjes.services;

import com.example.maatjes.dtos.outputDtos.AccountOutputDto;
import com.example.maatjes.dtos.inputDtos.AccountInputDto;
import com.example.maatjes.exceptions.FileSizeExceededException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Account;
import com.example.maatjes.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountOutputDto> getAccountsByFilters(String city, Boolean givesHelp) {
        List<Account> accounts;

        if (city != null && givesHelp != null) {
            // Filter by both city and givesHelp
            accounts = accountRepository.findAllByCityAndGivesHelp(city, givesHelp);
        } else if (city != null) {
            // Filter by city only
            accounts = accountRepository.findAllByCityEqualsIgnoreCase(city);
        } else if (givesHelp != null) {
            // Filter by givesHelp only
            accounts = accountRepository.findAllByGivesHelp(givesHelp);
        } else {
            // No filters specified, retrieve all accounts
            accounts = accountRepository.findAll();
        }

        List<AccountOutputDto> accountOutputDtos = new ArrayList<>();
        for (Account account : accounts) {
            accountOutputDtos.add(transferAccountToOutputDto(account));
        }
        return accountOutputDtos;
    }


    public AccountOutputDto getAccount(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new RecordNotFoundException("Account niet gevonden");}
        Account account = optionalAccount.get();
        return transferAccountToOutputDto(account);
    }

    public AccountOutputDto createAccount(AccountInputDto accountDto) {
        Account account = transferInputDtoToAccount(accountDto);
        accountRepository.save(account);
        return transferAccountToOutputDto(account);
    }

    public AccountOutputDto updateAccount(Long id, AccountInputDto newAccount) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account1 = accountOptional.get();

            account1.setName(newAccount.getName());
            LocalDate currentDate = LocalDate.now();
            Period age = Period.between(newAccount.getBirthdate(), currentDate);
            int ageYears = age.getYears();
            //todo check qua leeftijd? 18 jaar of ouder?
            account1.setAge(ageYears);
            account1.setSex(newAccount.getSex());
            account1.setPhoneNumber(newAccount.getPhoneNumber());
            account1.setEmailAddress(newAccount.getEmailAddress());
            account1.setStreet(newAccount.getStreet());
            account1.setHouseNumber(newAccount.getHouseNumber());
            account1.setPostalCode(newAccount.getPostalCode());
            account1.setCity(newAccount.getCity());
            account1.setBio(newAccount.getBio());
            account1.setDocument(newAccount.getDocument());
            account1.setGivesHelp(newAccount.isGivesHelp());
            account1.setNeedsHelp(newAccount.isNeedsHelp());
            account1.setActivitiesToGive(newAccount.getActivitiesToGive());
            account1.setActivitiesToReceive(newAccount.getActivitiesToReceive());
            account1.setAvailability(newAccount.getAvailability());
            account1.setFrequency(newAccount.getFrequency());
            Account returnAccount = accountRepository.save(account1);
            return transferAccountToOutputDto(returnAccount);
        } else {
            throw new RecordNotFoundException("Account niet gevonden");
        }
    }

    @Transactional
    public AccountOutputDto uploadDocument(Long id, MultipartFile file) throws FileSizeExceededException, IOException {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account1 = accountOptional.get();

            long fileSize = file.getBytes().length;

            long maxFileSize = 1000000; // 1MB in bytes
            if (fileSize > maxFileSize) {
                throw new FileSizeExceededException("Bestand is te groot");
            }
            byte[] documentData = file.getBytes();

            account1.setDocument(documentData);
            Account returnaccount = accountRepository.save(account1);

            return transferAccountToOutputDto(returnaccount);
        } else {
            throw new RecordNotFoundException("Account niet gevonden");
        }
    }

    @Transactional
    public void removeDocument (@RequestBody Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setDocument(null);
        } else {
            throw new RecordNotFoundException("Account niet gevonden");
        }
    }

    public void removeAccount(@RequestBody Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            accountRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("Account niet gevonden");
        }
    }

    public AccountOutputDto transferAccountToOutputDto(Account account) {
        AccountOutputDto accountOutputDto = new AccountOutputDto();
        accountOutputDto.id = account.getId();
        accountOutputDto.name = account.getName();
        accountOutputDto.age = account.getAge();
        accountOutputDto.sex = account.getSex();
        accountOutputDto.phoneNumber = account.getPhoneNumber();
        accountOutputDto.emailAddress = account.getEmailAddress();
        accountOutputDto.street = account.getStreet();
        accountOutputDto.houseNumber = account.getHouseNumber();
        accountOutputDto.postalCode = account.getPostalCode();
        accountOutputDto.city = account.getCity();
        accountOutputDto.bio = account.getBio();
        accountOutputDto.document = account.getDocument();
        accountOutputDto.givesHelp = account.isGivesHelp();
        accountOutputDto.needsHelp = account.isNeedsHelp();
        accountOutputDto.activitiesToGive = account.getActivitiesToGive();
        accountOutputDto.activitiesToReceive = account.getActivitiesToReceive();
        accountOutputDto.availability = account.getAvailability();
        accountOutputDto.frequency = account.getFrequency();
        accountOutputDto.helpGivers = account.getHelpGivers();
        accountOutputDto.helpReceivers = account.getHelpReceivers();
        accountOutputDto.givenReviews = account.getGivenReviews();
        accountOutputDto.receivedReviews = account.getReceivedReviews();
        return accountOutputDto;
        }

    public Account transferInputDtoToAccount(AccountInputDto accountInputDto) {
        Account account = new Account();
        account.setName(accountInputDto.getName());
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(accountInputDto.getBirthdate(), currentDate);
        int ageYears = age.getYears();
        account.setAge(ageYears);
        account.setSex(accountInputDto.getSex());
        account.setPhoneNumber(accountInputDto.getPhoneNumber());
        account.setEmailAddress(accountInputDto.getEmailAddress());
        account.setStreet(accountInputDto.getStreet());
        account.setHouseNumber(accountInputDto.getHouseNumber());
        account.setPostalCode(accountInputDto.getPostalCode());
        account.setCity(accountInputDto.getCity());
        account.setBio(accountInputDto.getBio());
        account.setDocument(accountInputDto.getDocument());
        account.setGivesHelp(accountInputDto.isGivesHelp());
        account.setNeedsHelp(accountInputDto.isNeedsHelp());
        account.setActivitiesToGive(accountInputDto.getActivitiesToGive());
        account.setActivitiesToReceive(accountInputDto.getActivitiesToReceive());
        account.setAvailability(accountInputDto.getAvailability());
        account.setFrequency(accountInputDto.getFrequency());
        return account;
        }
}
