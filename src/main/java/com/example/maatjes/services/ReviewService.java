package com.example.maatjes.services;

import com.example.maatjes.dtos.ReviewDto;
import com.example.maatjes.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) { this.reviewRepository = reviewRepository;}

}
