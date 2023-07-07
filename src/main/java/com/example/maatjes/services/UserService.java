package com.example.maatjes.services;

import com.example.maatjes.dtos.inputDtos.UserInputDto;
import com.example.maatjes.dtos.outputDtos.UserOutputDto;
import com.example.maatjes.exceptions.BadRequestException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Authority;
import com.example.maatjes.models.User;
import com.example.maatjes.repositories.UserRepository;
import com.example.maatjes.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
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

    @PreAuthorize("#username == authentication.getName()")
    public UserOutputDto getUser(String username) throws RecordNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("Deze gebruiker bestaat niet."));
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
//            throw new BadRequestException("You're not allowed to view this profile.");
//        }
        return transferUserToOutputDto(user);
    }

    //todo onderste wil ik misschien niet op straat gooien.
    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public UserOutputDto createUser(UserInputDto userInputDto) throws BadRequestException {
        if (userRepository.existsById(userInputDto.username)) {
            throw new BadRequestException("Invalid request: another user exists with the username: " + userInputDto.username);
        }
        User user = new User();
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        user = transferUserInputDtoToUser(user, userInputDto, passwordEncoder);
        user.setApikey(randomString);
        user.addAuthority(new Authority(user.getUsername(), "ROLE_USER"));
        userRepository.save(user);
        return transferUserToOutputDto(user);
    }

    @PreAuthorize("#username == authentication.getName()")
    public void updateUser(String username, UserInputDto userInputDto) {
        if (!userRepository.existsById(username)) throw new RecordNotFoundException("Gebruiker met de gebruikersnaam " + username + " niet gevonden");
        User user = userRepository.findById(username).get();
        user = transferUserInputDtoToUser(user, userInputDto, passwordEncoder);
        userRepository.save(user);
    }

    @PreAuthorize("#username == authentication.getName()")
    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public Set<Authority> getUserAuthorities(String username) throws RecordNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("User with ID " + username + " doesn't exist."));
        UserOutputDto userOutputDto = transferUserToOutputDto(user);
        userOutputDto.authorities = user.getAuthorities();
        return userOutputDto.authorities;
    }

    public void addAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new RecordNotFoundException("Gebruiker met de gebruikersnaam " + username + " niet gevonden");
        User user = userRepository.findById(username).get();
        user.addAuthority(new Authority(username, authority));
        userRepository.save(user);
    }

    public void removeAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new RecordNotFoundException("Gebruiker met de gebruikersnaam " + username + " niet gevonden");
        User user = userRepository.findById(username).get();
        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
    }

//    public static UserInputDto fromUser(User user){
//        var dto = new UserInputDto();
//        dto.username = user.getUsername();
//        dto.password = user.getPassword();
//        dto.enabled = user.isEnabled();
//        dto.apikey = user.getApikey();
//        dto.email = user.getEmail();
//        dto.authorities = user.getAuthorities();
//        return dto;
//    }
//
//    public User toUser(UserInputDto userInputDto) {
//        var user = new User();
//        user.setUsername(userInputDto.getUsername());
//        user.setPassword(userInputDto.getPassword());
//        user.setEnabled(userInputDto.getEnabled());
//        user.setApikey(userInputDto.getApikey());
//        user.setEmail(userInputDto.getEmail());
//        return user;
//    }

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
