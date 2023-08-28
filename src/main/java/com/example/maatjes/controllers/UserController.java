package com.example.maatjes.controllers;

import com.example.maatjes.dtos.inputDtos.UserInputDto;
import com.example.maatjes.dtos.outputDtos.UserOutputDto;
import com.example.maatjes.exceptions.AccessDeniedException;
import com.example.maatjes.services.UserService;
import com.example.maatjes.util.FieldErrorHandling;
import com.example.maatjes.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserInputDto userInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        String message = userService.createUser(userInputDto);
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/{username}/authorities")
    public ResponseEntity<UserOutputDto> addUserAuthority(@PathVariable String username, @RequestParam("authority") String authority) {
        UserOutputDto userOutputDto = userService.addUserAuthority(username, authority.toUpperCase());
        return new ResponseEntity<>(userOutputDto, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserOutputDto> getUser(@PathVariable String username) {
        UserOutputDto optionalUser = userService.getUser(username);
        return ResponseEntity.ok().body(optionalUser);
    }

    @GetMapping("/{username}/authorities")
    public ResponseEntity<Object> getUserAuthorities(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.getUserAuthorities(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<Object> updateUser(@PathVariable String username, @Valid @RequestBody UserInputDto dto, BindingResult bindingResult) {
        SecurityUtils.validateUsername(username, "user");
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        UserOutputDto userOutputDto = userService.updateUser(username, dto);
        return ResponseEntity.ok().body(userOutputDto);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        String message = userService.deleteUser(username);
        return ResponseEntity.ok().body(message);
    }

    @DeleteMapping("/{username}/authorities/{authority}")
    public ResponseEntity<String> removeAuthority(@PathVariable String username, @PathVariable("authority") String authority) {
        String message = userService.removeAuthority(username, authority);
        return ResponseEntity.ok().body(message);
    }
}