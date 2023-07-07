package com.example.maatjes.services;

import com.example.maatjes.dtos.outputDtos.AccountOutputDto;
import com.example.maatjes.dtos.inputDtos.AccountInputDto;
import com.example.maatjes.exceptions.FileSizeExceededException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.User;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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

//    public AccountOutputDto getAccount(String username) throws RecordNotFoundException {
//        Optional<Account> optionalAccount = accountRepository.findById(username);
//        if (optionalAccount.isEmpty()) {
//            throw new RecordNotFoundException("Account niet gevonden");
//        }
//        Account account = optionalAccount.get();
//        return transferAccountToOutputDto(account);
//    }
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.getName()")
    public AccountOutputDto getAccount(String username) throws RecordNotFoundException {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Gebruiker niet gevonden");
        }
        User user = optionalUser.get();
        Account account = user.getAccount();
        if (account == null) {
            throw new RecordNotFoundException("Account niet gevonden");
        }
        return transferAccountToOutputDto(account);
    }

    @PreAuthorize("#username == authentication.getName()")
    public AccountOutputDto updateAccount(String username, AccountInputDto accountInputDto) throws RecordNotFoundException {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Gebruiker niet gevonden");
        }
        User user = optionalUser.get();
        Account account = user.getAccount();
        if (account == null) {
            throw new RecordNotFoundException("Account niet gevonden");
        }
            account.setName(accountInputDto.getName());
            LocalDate currentDate = LocalDate.now();
            Period age = Period.between(accountInputDto.getBirthdate(), currentDate);
            int ageYears = age.getYears();
            //todo check qua leeftijd? 18 jaar of ouder?
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
            Account returnAccount = accountRepository.save(account);
            return transferAccountToOutputDto(returnAccount);
        }

    @Transactional
    @PreAuthorize("#username == authentication.getName()")
    public AccountOutputDto uploadIdentificationDocument(String username, MultipartFile file) throws FileSizeExceededException, IOException, RecordNotFoundException {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Gebruiker niet gevonden");
        }
        User user = optionalUser.get();
        Account account = user.getAccount();
        if (account == null) {
            throw new RecordNotFoundException("Account niet gevonden");
        }
            long fileSize = file.getBytes().length;
            long maxFileSize = 1000000; // 1MB in bytes
            if (fileSize > maxFileSize) {
                throw new FileSizeExceededException("Bestand is te groot");
            }
            byte[] documentData = file.getBytes();

            account.setDocument(documentData);
            Account returnaccount = accountRepository.save(account);

            return transferAccountToOutputDto(returnaccount);
    }

    @Transactional
    @PreAuthorize("#username == authentication.getName()")
    public void removeIdentificationDocument (@RequestBody String username) throws RecordNotFoundException {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Gebruiker niet gevonden");
        }
        User user = optionalUser.get();
        Account account = user.getAccount();
        if (account == null) {
            throw new RecordNotFoundException("Account niet gevonden");
        }
            account.setDocument(null);
    }

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.getName()")
    public void removeAccount(@RequestBody String username) throws RecordNotFoundException {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Gebruiker niet gevonden");
        }
        User user = optionalUser.get();
        Account account = user.getAccount();

        if (account == null) {
            throw new RecordNotFoundException("Account niet gevonden");
        } else {
            accountRepository.deleteById(account.getAccountId());
        }
    }

    public AccountOutputDto transferAccountToOutputDto(Account account) {
        AccountOutputDto accountOutputDto = new AccountOutputDto();
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
