package com.example.maatjes.controllers;

import com.example.maatjes.dtos.inputDtos.UserInputDto;
import com.example.maatjes.dtos.outputDtos.UserOutputDto;
import com.example.maatjes.exceptions.AccessDeniedException;
import com.example.maatjes.services.UserService;
import com.example.maatjes.util.FieldErrorHandling;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping
//    public ResponseEntity<List<UserInputDto>> getUsers() {
//        List<UserInputDto> userInputDtos = userService.getUsers();
//        return ResponseEntity.ok().body(userInputDtos);
//    }

    @GetMapping("/{username}")
    public ResponseEntity<UserOutputDto> getUser(@PathVariable("username") String username) {
        UserOutputDto optionalUser = userService.getUser(username);
        return ResponseEntity.ok().body(optionalUser);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserInputDto userInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        UserOutputDto userOutputDto = userService.createUser(userInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + userOutputDto.username).toUriString());
        return ResponseEntity.created(uri).body(userOutputDto);
        //todo of zet ik hier : "je user is succesvol aangemaakt, je kunt gaan inloggen"
    }

    @PutMapping("/{username}")
    public ResponseEntity<Object> updateUser(@PathVariable("username") String username, @RequestBody UserInputDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(authentication.getName())) {
            throw new AccessDeniedException("Je hebt geen toegang tot deze user");
        }
        UserOutputDto userOutputDto = userService.updateUser(username, dto);
        return ResponseEntity.ok().body(userOutputDto);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable("username") String username) {
        String message = userService.deleteUser(username);
        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/{username}/authorities")
    public ResponseEntity<Object> getUserAuthorities(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getUserAuthorities(username));
    }

    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<UserOutputDto> addUserAuthority(@PathVariable("username") String username, @RequestParam("authority") String authority) {
        UserOutputDto userOutputDto = userService.addUserAuthority(username, authority.toUpperCase());
        return new ResponseEntity<>(userOutputDto, HttpStatus.CREATED);
    }
    @DeleteMapping("/{username}/authorities/{authority}")
    public ResponseEntity<String> deleteUserAuthority(@PathVariable("username") String username, @PathVariable("authority") String authority) {
        String message = userService.removeAuthority(username, authority);
        return ResponseEntity.ok().body(message);
    }
}