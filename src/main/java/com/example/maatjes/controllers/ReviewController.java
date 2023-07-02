package com.example.maatjes.controllers;

import com.example.maatjes.dtos.outputDtos.ReviewOutputDto;
import com.example.maatjes.dtos.inputDtos.ReviewInputDto;
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
    public ResponseEntity<List<ReviewOutputDto>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getReviews(), HttpStatus.OK);
    }

    @GetMapping("/reviews/by/{accountId}")
    public ResponseEntity<List<ReviewOutputDto>> getReviewsWrittenByAccount(@PathVariable("accountId") Long accountId) {
        List<ReviewOutputDto> reviewOutputDtos = reviewService.getReviewsWrittenByAccount(accountId);
        return new ResponseEntity<>(reviewOutputDtos, HttpStatus.OK);
    }

    @GetMapping("/reviews/for/{accountId}")
    public ResponseEntity<List<ReviewOutputDto>> getReviewsWrittenForAccount(@PathVariable("accountId") Long accountId) {
        List<ReviewOutputDto> reviewOutputDtos = reviewService.getReviewsWrittenForAccount(accountId);
        return new ResponseEntity<>(reviewOutputDtos, HttpStatus.OK);
    }

    @GetMapping("/toverify")
    public ResponseEntity<List<ReviewOutputDto>> getReviewsToVerify() {
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
