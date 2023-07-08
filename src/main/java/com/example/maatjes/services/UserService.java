package com.example.maatjes.services;

import com.example.maatjes.dtos.inputDtos.UserInputDto;
import com.example.maatjes.dtos.outputDtos.UserOutputDto;
import com.example.maatjes.exceptions.AccessDeniedException;
import com.example.maatjes.exceptions.BadRequestException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.exceptions.UsernameNotFoundException;
import com.example.maatjes.models.Authority;
import com.example.maatjes.models.User;
import com.example.maatjes.repositories.UserRepository;
import com.example.maatjes.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public List<UserInputDto> getUsers() {
//        List<UserInputDto> collection = new ArrayList<>();
//        List<User> list = userRepository.findAll();
//        for (User user : list) {
//            collection.add(fromUser(user));
//        }
//        return collection;
//    }

    public UserOutputDto getUser(String username) throws RecordNotFoundException, AccessDeniedException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            User user = userRepository.findById(username)
                    .orElseThrow(() -> new RecordNotFoundException("Deze gebruiker bestaat niet."));
            return transferUserToOutputDto(user);
        } else if (username.equals(authentication.getName())) {
            User user = userRepository.findById(username)
                    .orElseThrow(() -> new RecordNotFoundException("Deze gebruiker bestaat niet."));
            return transferUserToOutputDto(user);
        } else {
            throw new AccessDeniedException("Je hebt geen toegang tot deze user");
        }
    }

    //todo onderste wil ik misschien niet op straat gooien. Misschien gewoon weggooien?
    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public UserOutputDto createUser(UserInputDto userInputDto) throws BadRequestException {
        if (userRepository.existsById(userInputDto.getUsername())) {
            throw new BadRequestException("Er bestaat al een andere gebruiker met de naam " + userInputDto.getUsername());
        }

        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        User user = transferUserInputDtoToUser(new User(), userInputDto, passwordEncoder);
        user.setApikey(randomString);
        user.addAuthority(new Authority(user.getUsername(), "ROLE_USER"));
        userRepository.save(user);
        return transferUserToOutputDto(user);
    }

    @PreAuthorize("#username == authentication.getName()")
    public UserOutputDto updateUser(String username, UserInputDto userInputDto) throws UsernameNotFoundException, BadRequestException, AccessDeniedException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker met de gebruikersnaam " + username + " niet gevonden"));

        if (!Objects.equals(userInputDto.getUsername(), username)) {
            throw new BadRequestException("Je kunt je gebruikersnaam niet aanpassen.");
        }

        user = transferUserInputDtoToUser(user, userInputDto, passwordEncoder);
        userRepository.save(user);
        return transferUserToOutputDto(user);
    }

    public String deleteUser(String username) throws RecordNotFoundException, AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")) || username.equals(authentication.getName())) {
            User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("Deze gebruiker bestaat niet."));
            userRepository.deleteById(user.getUsername());
            return "Gebruiker succesvol verwijderd";
        } else {
            throw new AccessDeniedException("Je hebt geen toegang tot deze user");
        }
    }

    public Set<Authority> getUserAuthorities(String username) throws RecordNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("Gebruiker met de gebruikersnaam " + username + " niet gevonden"));
        UserOutputDto userOutputDto = transferUserToOutputDto(user);
        userOutputDto.authorities = user.getAuthorities();
        return userOutputDto.authorities;
    }

    public UserOutputDto addUserAuthority(String username, String authority) throws RecordNotFoundException, BadRequestException {
        if (!userRepository.existsById(username)) throw new RecordNotFoundException("Gebruiker met de gebruikersnaam " + username + " niet gevonden");
        User user = userRepository.findById(username).get();
        for (Authority a : user.getAuthorities()) {
            if (a.getAuthority().equals("ROLE_" + authority)) {
                throw new BadRequestException("Deze gebruiker heeft al de " + authority + " rol");
            }
        }
        try {
            user.addAuthority(new Authority(user.getUsername(), "ROLE_" + authority));
        } catch (Exception e) {
            throw new BadRequestException("Deze authoriteit kan niet worden toegevoegd.");
        }
        userRepository.save(user);
        return transferUserToOutputDto(user);
    }

    public String removeAuthority(String username, String authority) throws RecordNotFoundException, BadRequestException {
        if (!userRepository.existsById(username)) throw new RecordNotFoundException("Gebruiker met de gebruikersnaam " + username + " niet gevonden");
        User user = userRepository.findById(username).get();
        String prefixedAuthority = "ROLE_" + authority;
        Authority authorityToRemove = user.getAuthorities().stream()
                .filter(a -> a.getAuthority().equalsIgnoreCase(prefixedAuthority))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Gebruiker heeft die autoriteit niet"));
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
        return "Authoriteit succesvol verwijderd";
    }

    public static UserOutputDto transferUserToOutputDto(User user) {
        UserOutputDto userOutputDto = new UserOutputDto();
        userOutputDto.username = user.getUsername();
        userOutputDto.email = user.getEmail();
        return userOutputDto;
    }

    public static User transferUserInputDtoToUser(User user, UserInputDto userInputDto, PasswordEncoder passwordEncoder) {
        if (userInputDto.password != null) {
            user.setPassword(passwordEncoder.encode(userInputDto.password));
        }
        user.setEmail(userInputDto.email);
        user.setEnabled(true);
        user.setUsername(userInputDto.getUsername());
        return user;
    }

}
