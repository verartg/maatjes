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

    public AccountOutputDto createAccount(AccountInputDto accountInputDto) {
        Account account = transferInputDtoToAccount(accountInputDto);
        accountRepository.save(account);
        return transferAccountToOutputDto(account);
    }

    public List<AccountOutputDto> getAccountsByFilters(String city, Boolean givesHelp) {
        List<Account> accounts;

        if (city != null && givesHelp != null) {
            accounts = accountRepository.findAllByCityAndGivesHelp(city, givesHelp);
        } else if (city != null) {
            accounts = accountRepository.findAllByCityEqualsIgnoreCase(city);
        } else if (givesHelp != null) {
            accounts = accountRepository.findAllByGivesHelp(givesHelp);
        } else {
            accounts = accountRepository.findAll();
        }

        List<AccountOutputDto> accountOutputDtos = new ArrayList<>();
        for (Account account : accounts) {
            accountOutputDtos.add(transferAccountToOutputDto(account));
        }
        return accountOutputDtos;
    }

    public AccountOutputDto getAccount(Long accountId) throws RecordNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isEmpty()) {
            throw new RecordNotFoundException("Account niet gevonden");
        }
        Account account = optionalAccount.get();
        return transferAccountToOutputDto(account);
    }

    public AccountOutputDto updateAccount(Long accountId, AccountInputDto accountInputDto) throws RecordNotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account1 = accountOptional.get();

            account1.setName(accountInputDto.getName());
            LocalDate currentDate = LocalDate.now();
            Period age = Period.between(accountInputDto.getBirthdate(), currentDate);
            int ageYears = age.getYears();
            //todo check qua leeftijd? 18 jaar of ouder?
            account1.setAge(ageYears);
            account1.setSex(accountInputDto.getSex());
            account1.setPhoneNumber(accountInputDto.getPhoneNumber());
            account1.setEmailAddress(accountInputDto.getEmailAddress());
            account1.setStreet(accountInputDto.getStreet());
            account1.setHouseNumber(accountInputDto.getHouseNumber());
            account1.setPostalCode(accountInputDto.getPostalCode());
            account1.setCity(accountInputDto.getCity());
            account1.setBio(accountInputDto.getBio());
            account1.setDocument(accountInputDto.getDocument());
            account1.setGivesHelp(accountInputDto.isGivesHelp());
            account1.setNeedsHelp(accountInputDto.isNeedsHelp());
            account1.setActivitiesToGive(accountInputDto.getActivitiesToGive());
            account1.setActivitiesToReceive(accountInputDto.getActivitiesToReceive());
            account1.setAvailability(accountInputDto.getAvailability());
            account1.setFrequency(accountInputDto.getFrequency());
            Account returnAccount = accountRepository.save(account1);
            return transferAccountToOutputDto(returnAccount);
        } else {
            throw new RecordNotFoundException("Account niet gevonden");
        }
    }

    @Transactional
    public AccountOutputDto uploadIdentificationDocument(Long accountId, MultipartFile file) throws FileSizeExceededException, IOException, RecordNotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
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
    public void removeIdentificationDocument (@RequestBody Long accountId) throws RecordNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setDocument(null);
        } else {
            throw new RecordNotFoundException("Account niet gevonden");
        }
    }

    public void removeAccount(@RequestBody Long accountId) throws RecordNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            accountRepository.deleteById(accountId);
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
