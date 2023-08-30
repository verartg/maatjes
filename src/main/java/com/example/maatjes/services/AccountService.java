package com.example.maatjes.services;

import com.example.maatjes.dtos.outputDtos.AccountOutputDto;
import com.example.maatjes.dtos.inputDtos.AccountInputDto;
import com.example.maatjes.exceptions.AccessDeniedException;
import com.example.maatjes.exceptions.BadRequestException;
import com.example.maatjes.exceptions.IllegalArgumentException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.Match;
import com.example.maatjes.models.User;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.UserRepository;
import com.example.maatjes.util.SecurityUtils;
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

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public AccountOutputDto createAccount(AccountInputDto accountInputDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findById(username)
                .orElseThrow(() -> new RecordNotFoundException("Deze gebruiker bestaat niet."));

        if (user.getAccount() != null) {
            throw new BadRequestException("Je hebt al een account.");
        }

        boolean givesHelp = accountInputDto.isGivesHelp();
        boolean needsHelp = accountInputDto.isNeedsHelp();

        if (givesHelp && (accountInputDto.getActivitiesToGive() == null || accountInputDto.getActivitiesToGive().isEmpty())) {
            throw new BadRequestException("Je moet activiteiten opgeven als je hulp biedt.");
        }

        if (needsHelp && (accountInputDto.getActivitiesToReceive() == null || accountInputDto.getActivitiesToReceive().isEmpty())) {
            throw new BadRequestException("Je moet activiteiten opgeven als je hulp nodig hebt.");
        }

        Account account = transferInputDtoToAccount(accountInputDto);
        account.setUser(user);
        user.setAccount(account);
        accountRepository.save(account);
        userRepository.save(user);
        return transferAccountToOutputDto(account);
    }

    private User getUserAndAccount(String username) throws RecordNotFoundException {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Gebruiker niet gevonden");
        }

        User user = optionalUser.get();
        Account account = user.getAccount();
        if (account == null) {
            throw new RecordNotFoundException("Account niet gevonden");
        }
        return user;
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

    public AccountOutputDto getAccount(String username) throws RecordNotFoundException {
        User user = getUserAndAccount(username);
        Account account = user.getAccount();
        return transferAccountToOutputDto(account);
    }

    public byte[] getIdentificationDocument(String username) throws RecordNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isSelf = authentication.getName().equals(username);

        if (isAdmin || isSelf) {
            User user = getUserAndAccount(username);
            Account account = user.getAccount();
            byte[] document = account.getIdDocument();
            if (document == null) {
                throw new RecordNotFoundException("Document niet gevonden");
            }
            return document;
        } else {
            throw new AccessDeniedException("Je hebt geen toegang tot deze match");
        }
    }

    public AccountOutputDto updateAccount(String username, AccountInputDto accountInputDto) throws RecordNotFoundException {
        SecurityUtils.validateUsername(username, "account");
        User user = getUserAndAccount(username);
        Account account = user.getAccount();

        if (accountInputDto.getName() != null) {
            account.setName(accountInputDto.getName());
        }

        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(accountInputDto.getBirthdate(), currentDate);
        int ageYears = age.getYears();

        if (ageYears < 18) {
            throw new IllegalArgumentException("Alleen volwassenen kunnen een account aanmaken");
        }

        account.setAge(ageYears);

        if (accountInputDto.getSex() != null) {
            account.setSex(accountInputDto.getSex());
        }

        if (accountInputDto.getPhoneNumber() != null) {
            account.setPhoneNumber(accountInputDto.getPhoneNumber());
        }

        if (accountInputDto.getStreet() != null) {
            account.setStreet(accountInputDto.getStreet());
        }

        if (accountInputDto.getHouseNumber() != null) {
            account.setHouseNumber(accountInputDto.getHouseNumber());
        }

        if (accountInputDto.getPostalCode() != null) {
            account.setPostalCode(accountInputDto.getPostalCode());
        }

        if (accountInputDto.getCity() != null) {
            account.setCity(accountInputDto.getCity());
        }

        if (accountInputDto.getBio() != null) {
            account.setBio(accountInputDto.getBio());
        }

        if (accountInputDto.getIdDocument() != null) {
            account.setIdDocument(accountInputDto.getIdDocument());
        }

        account.setGivesHelp(accountInputDto.isGivesHelp());
        account.setNeedsHelp(accountInputDto.isNeedsHelp());

        if (accountInputDto.getActivitiesToGive() != null) {
            account.setActivitiesToGive(accountInputDto.getActivitiesToGive());
        }

        if (accountInputDto.getActivitiesToReceive() != null) {
            account.setActivitiesToReceive(accountInputDto.getActivitiesToReceive());
        }

        if (accountInputDto.getAvailability() != null) {
            account.setAvailability(accountInputDto.getAvailability());
        }

        if (accountInputDto.getFrequency() != null) {
            account.setFrequency(accountInputDto.getFrequency());
        }

        Account returnAccount = accountRepository.save(account);
        return transferAccountToOutputDto(returnAccount);
    }

    @Transactional
    public AccountOutputDto uploadIdentificationDocument(String username, MultipartFile file)
            throws MaxUploadSizeExceededException, IOException, RecordNotFoundException, BadRequestException {
        SecurityUtils.validateUsername(username, "account");
        User user = getUserAndAccount(username);
        Account account = user.getAccount();

        long fileSize = file.getSize();
        long maxFileSize = 1000000; // 1MB in bytes
        if (fileSize > maxFileSize) {
            throw new MaxUploadSizeExceededException(maxFileSize);
        }

        String fileType = file.getContentType();
        if (!fileType.equals("application/pdf")) {
            throw new BadRequestException("Ongeldig documenttype. Alleen PDF-bestanden zijn toegestaan.");
        }

        byte[] documentData = file.getBytes();
        account.setIdDocument(documentData);
        Account returnAccount = accountRepository.save(account);

        return transferAccountToOutputDto(returnAccount);
    }

    @Transactional
    public String removeIdentificationDocument (@RequestBody String username) throws RecordNotFoundException {
        SecurityUtils.validateUsername(username, "account");
        User user = getUserAndAccount(username);
        Account account = user.getAccount();

        if (account.getIdDocument() == null) {
            throw new RecordNotFoundException("De gebruiker heeft nog geen document geüpload");
        }
            account.setIdDocument(null);
        return "Document succesvol verwijderd";
    }

    public String removeAccount(String username) throws RecordNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isSelf = authentication.getName().equals(username);

        if (isAdmin || isSelf) {
            User user = getUserAndAccount(username);
            Account account = user.getAccount();

            if (account != null) {
                user.setAccount(null);
                userRepository.save(user);
                accountRepository.deleteById(account.getAccountId());
                return "Account succesvol verwijderd";
            } else {
                throw new RecordNotFoundException("Account niet gevonden");
            }
        } else {
            throw new AccessDeniedException("Je hebt geen toegang tot deze gebruiker");
        }
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
        return accountOutputDto;
        }

    public Account transferInputDtoToAccount(AccountInputDto accountInputDto) {
        Account account = new Account();
        account.setName(accountInputDto.getName());
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(accountInputDto.getBirthdate(), currentDate);
        int ageYears = age.getYears();

        if (ageYears < 18) {
            throw new IllegalArgumentException("Je moet boven de achttien zijn");
        }

        account.setAge(ageYears);
        account.setSex(accountInputDto.getSex());
        account.setPhoneNumber(accountInputDto.getPhoneNumber());
        account.setStreet(accountInputDto.getStreet());
        account.setHouseNumber(accountInputDto.getHouseNumber());
        account.setPostalCode(accountInputDto.getPostalCode());
        account.setCity(accountInputDto.getCity());
        account.setBio(accountInputDto.getBio());
        account.setIdDocument(accountInputDto.getIdDocument());
        account.setGivesHelp(accountInputDto.isGivesHelp());
        account.setNeedsHelp(accountInputDto.isNeedsHelp());
        account.setActivitiesToGive(accountInputDto.getActivitiesToGive());
        account.setActivitiesToReceive(accountInputDto.getActivitiesToReceive());
        account.setAvailability(accountInputDto.getAvailability());
        account.setFrequency(accountInputDto.getFrequency());
        return account;
        }
}
