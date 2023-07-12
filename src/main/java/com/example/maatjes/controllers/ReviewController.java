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

    @PostMapping("/new")
    public ResponseEntity<Object> createReview (@Valid @RequestBody ReviewInputDto reviewInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(reviewService.createReview(reviewInputDto), HttpStatus.ACCEPTED);
    }

    //todo moet ik geen getReview?
    @GetMapping("/by/{accountId}")
    public ResponseEntity<List<ReviewOutputDto>> getReviewsWrittenByAccount(@PathVariable Long accountId) {
        List<ReviewOutputDto> reviewOutputDtos = reviewService.getReviewsWrittenByAccount(accountId);
        return new ResponseEntity<>(reviewOutputDtos, HttpStatus.OK);
    }

    @GetMapping("/for/{accountId}")
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

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> removeReview(@PathVariable Long reviewId) {
        String message = reviewService.removeReview(reviewId);
        return ResponseEntity.ok().body(message);
    }
}
