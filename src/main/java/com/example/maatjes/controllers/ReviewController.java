package com.example.maatjes.controllers;

import com.example.maatjes.dtos.ReviewDto;
import com.example.maatjes.dtos.ReviewInputDto;
import com.example.maatjes.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {this.reviewService = reviewService;}

//    @GetMapping("/{id}")
//    public ResponseEntity<ReviewDto> getReview(@PathVariable("id") Long id) {
//        return new ResponseEntity<>(reviewService.getReviewById(id), HttpStatus.OK);
//    }

    @PostMapping("/{matchId}/{accountId}")
    public ResponseEntity<Object> createReview (@PathVariable("matchId") Long matchId, @PathVariable ("accountId") Long accountId, @RequestBody ReviewInputDto reviewInputDto) {

        ReviewDto reviewDto = reviewService.createReview(matchId, accountId, reviewInputDto);
        return new ResponseEntity<>(reviewDto, HttpStatus.ACCEPTED);
    }
}
