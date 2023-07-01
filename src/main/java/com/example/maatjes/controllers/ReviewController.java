package com.example.maatjes.controllers;

import com.example.maatjes.dtos.ReviewDto;
import com.example.maatjes.dtos.ReviewInputDto;
import com.example.maatjes.services.ReviewService;
import com.example.maatjes.util.FieldErrorHandling;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ReviewDto> getReview(@PathVariable("id") Long id) {
//        return new ResponseEntity<>(reviewService.getReviewById(id), HttpStatus.OK);
//    }

    @PostMapping("/{matchId}/{accountId}")
    public ResponseEntity<Object> createReview (@PathVariable("matchId") Long matchId, @PathVariable ("accountId") Long accountId, @Valid @RequestBody ReviewInputDto reviewInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(reviewService.createReview(matchId, accountId, reviewInputDto), HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getReviews(), HttpStatus.OK);
    }

    @GetMapping("/reviews/by/{accountId}")
    public ResponseEntity<List<ReviewDto>> getReviewsWrittenByAccount(@PathVariable("accountId") Long accountId) {
        List<ReviewDto> reviewDtos = reviewService.getReviewsWrittenByAccount(accountId);
        return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
    }

    @GetMapping("/reviews/for/{accountId}")
    public ResponseEntity<List<ReviewDto>> getReviewsWrittenForAccount(@PathVariable("accountId") Long accountId) {
        List<ReviewDto> reviewDtos = reviewService.getReviewsWrittenForAccount(accountId);
        return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
    }

    @GetMapping("/toverify")
    public ResponseEntity<List<ReviewDto>> getReviewsToVerify() {
        return new ResponseEntity<>(reviewService.getReviewsToVerify(), HttpStatus.OK);
    }

    @PutMapping("/verify/{reviewId}")
    public ResponseEntity<Object> verifyReview(@PathVariable("reviewId") Long reviewId) {
        return new ResponseEntity<>(reviewService.verifyReview(reviewId), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Object> updateReview(@PathVariable Long reviewId, @Valid @RequestBody ReviewInputDto review, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(reviewService.updateReview(reviewId, review), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeReview(@PathVariable Long id) {
        reviewService.removeReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
