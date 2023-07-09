package com.example.maatjes.services;

import com.example.maatjes.controllers.UserController;
import com.example.maatjes.dtos.outputDtos.AccountOutputDto;
import com.example.maatjes.dtos.inputDtos.AccountInputDto;
import com.example.maatjes.exceptions.AccessDeniedException;
import com.example.maatjes.exceptions.BadRequestException;
//import com.example.maatjes.exceptions.MaxUploadSizeExceededException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.User;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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
    private final UserController userController;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, UserController userController) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.userController = userController;
    }

    public AccountOutputDto createAccount(AccountInputDto accountInputDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RecordNotFoundException("Deze gebruiker bestaat niet."));
        if (user.getAccount() != null) {
            throw new BadRequestException("Je hebt al een account.");
        }

        Account account = transferInputDtoToAccount(accountInputDto);
        account.setUser(user);
        user.setAccount(account);
        accountRepository.save(account);

//        user.getAccount().setAccountId(account.getAccountId());
        userRepository.save(user);
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
    public AccountOutputDto uploadIdentificationDocument(String username, MultipartFile file)
            throws MaxUploadSizeExceededException, IOException, RecordNotFoundException {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Gebruiker niet gevonden");
        }
        User user = optionalUser.get();
        Account account = user.getAccount();
        if (account == null) {
            throw new RecordNotFoundException("Account niet gevonden");
        }

        long fileSize = file.getSize();
        long maxFileSize = 1000000; // 1MB in bytes
        if (fileSize > maxFileSize) {
            throw new MaxUploadSizeExceededException(maxFileSize);
        }

        byte[] documentData = file.getBytes();
        account.setDocument(documentData);
        Account returnAccount = accountRepository.save(account);

        return transferAccountToOutputDto(returnAccount);
    }

    @Transactional
    public String removeIdentificationDocument (@RequestBody String username) throws RecordNotFoundException {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Gebruiker niet gevonden");
        }
        User user = optionalUser.get();
        Account account = user.getAccount();
        if (account == null) {
            throw new RecordNotFoundException("Account niet gevonden");
        }

        if (account.getDocument() == null) {
            throw new RecordNotFoundException("De gebruiker heeft nog geen document geüpload");
        }
            account.setDocument(null);
        return "Document succesvol verwijderd";
    }

    public String removeAccount(@RequestBody String username) throws RecordNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isSelf = authentication.getName().equals(username);

        if (isAdmin || isSelf) {
            Optional<User> optionalUser = userRepository.findById(username);
            if (optionalUser.isEmpty()) {
                throw new RecordNotFoundException("Gebruiker niet gevonden");
            }

            User user = optionalUser.get();
            Account account = user.getAccount();

            if (account == null) {
                throw new RecordNotFoundException("Account niet gevonden");
            } else {
                // Remove the account association from the user
                user.setAccount(null);
                userRepository.save(user);

                // Delete the account
                //todo bij deze stap wordt ook de user verwijderd...whyyyyyy
                accountRepository.deleteById(account.getAccountId());
            }
        } else {
            throw new AccessDeniedException("Je hebt geen toegang tot deze gebruiker");
        }
        return "Account succesvol verwijderd";
    }



    public AccountOutputDto transferAccountToOutputDto(Account account) {
        AccountOutputDto accountOutputDto = new AccountOutputDto();
        accountOutputDto.name = account.getName();
        accountOutputDto.age = account.getAge();
        accountOutputDto.sex = account.getSex();
        accountOutputDto.city = account.getCity();
        accountOutputDto.bio = account.getBio();
        accountOutputDto.givesHelp = account.isGivesHelp();
        accountOutputDto.needsHelp = account.isNeedsHelp();
        accountOutputDto.activitiesToGive = account.getActivitiesToGive();
        accountOutputDto.activitiesToReceive = account.getActivitiesToReceive();
        accountOutputDto.availability = account.getAvailability();
        accountOutputDto.frequency = account.getFrequency();
        accountOutputDto.givenReviews = account.getGivenReviews();
        accountOutputDto.receivedReviews = account.getReceivedReviews();
//        accountOutputDto.user = account.getUser();
//        accountOutputDto.setUser(UserService.transferUserToOutputDto(account.getUser()));
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
