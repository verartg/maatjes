package com.example.maatjes.services;

import com.example.maatjes.dtos.outputDtos.ReviewOutputDto;
import com.example.maatjes.dtos.inputDtos.ReviewInputDto;
import com.example.maatjes.exceptions.AccountNotAssociatedException;
import com.example.maatjes.exceptions.BadRequestException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.Match;
import com.example.maatjes.models.Review;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.MatchRepository;
import com.example.maatjes.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MatchRepository matchRepository;
    private final AccountRepository accountRepository;

    public ReviewService(ReviewRepository reviewRepository, MatchRepository matchRepository, AccountRepository accountRepository) {
        this.reviewRepository = reviewRepository;
        this.matchRepository = matchRepository;
        this.accountRepository = accountRepository;
    }

    public ReviewOutputDto createReview (Long matchId, Long accountId, ReviewInputDto reviewInputDto) throws RecordNotFoundException, AccountNotAssociatedException, BadRequestException {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));
        //todo omschrijven naar schrijverReview
        //todo als ik de principle heb, dan hoef ik alleen maar de matchid mee te geven om bij de writer en receiver te komen van de review.
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RecordNotFoundException("Account niet gevonden"));
        if (!match.getHelpGiver().equals(account) && !match.getHelpReceiver().equals(account)) {
            throw new AccountNotAssociatedException("Je kunt alleen een beoordeling schrijven over je eigen matches");
        }
        if (!match.isGiverAccepted() || !match.isReceiverAccepted()) {
            throw new AccountNotAssociatedException("Match moet eerst worden geaccepteerd voordat je een review kunt schrijven");}

        for (Review r: match.getMatchReviews()) {
            if (Objects.equals(r.getWrittenBy().getId(), accountId)){
                throw new BadRequestException("Je kunt maar één review schrijven over je match");
            }
        }
                Review review = transferInputDtoToReview(reviewInputDto);
                review.setMatch(match);
                review.setWrittenBy(account);
                review.setWrittenFor(match.getHelpReceiver().equals(account) ? match.getHelpGiver() : match.getHelpReceiver());
                reviewRepository.save(review);
                return transferReviewToOutputDto(review);
            }

    public List<ReviewOutputDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Review review : reviews) {
            ReviewOutputDto reviewOutputDto = transferReviewToOutputDto(review);
            reviewOutputDtos.add(reviewOutputDto);
        }
        return reviewOutputDtos;
    }

    public List<ReviewOutputDto> getReviewsWrittenByAccount(Long accountId) throws RecordNotFoundException {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RecordNotFoundException("Account niet gevonden"));
        List<Review> reviews = reviewRepository.findByWrittenBy(account);
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();

        for (Review review : reviews) {
            if (review.isVerified()) {
                ReviewOutputDto reviewOutputDto = transferReviewToOutputDto(review);
                reviewOutputDtos.add(reviewOutputDto);
            }
        }
        return reviewOutputDtos;
    }

    public List<ReviewOutputDto> getReviewsWrittenForAccount(Long accountId) throws RecordNotFoundException {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RecordNotFoundException("Account niet gevonden"));
        List<Review> reviews = reviewRepository.findByWrittenFor(account);
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();

        for (Review review : reviews) {
            if (review.isVerified()) {
                ReviewOutputDto reviewOutputDto = transferReviewToOutputDto(review);
                reviewOutputDtos.add(reviewOutputDto);
            }
        }
        return reviewOutputDtos;
    }

    public List<ReviewOutputDto> getReviewsToVerify() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewOutputDto> reviewOutputDtosToVerify = new ArrayList<>();
        for (Review review : reviews) {
            if (!review.isVerified()) {
                ReviewOutputDto reviewOutputDto = transferReviewToOutputDto(review);
                reviewOutputDtosToVerify.add(reviewOutputDto);
            }
        }
        return reviewOutputDtosToVerify;
    }

    public ReviewOutputDto verifyReview(Long reviewId) throws RecordNotFoundException, BadRequestException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("Review niet gevonden"));
        //todo if review niet goed is, dan setten we een feedback met een string. dan blijft review.setVerified(false). in het andere geval:

        if (review.isVerified()) {
            throw new BadRequestException("Review is al geverifieerd");
        } else {
            review.setVerified(true);
        }
        reviewRepository.save(review);
        return transferReviewToOutputDto(review);
    }

    public ReviewOutputDto updateReview(Long reviewId, ReviewInputDto reviewInputDto) throws RecordNotFoundException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("De review met ID " + reviewId + " bestaat niet."));
        review.setRating(reviewInputDto.getRating());
        review.setDescription(reviewInputDto.getDescription());
        review.setVerified(false);
        Review returnReview = reviewRepository.save(review);
        return  transferReviewToOutputDto(returnReview);
    }

    public void removeReview(@RequestBody Long reviewId) throws RecordNotFoundException {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
           reviewRepository.deleteById(reviewId);
        } else {
            throw new RecordNotFoundException("Review niet gevonden");
        }
    }

    public ReviewOutputDto transferReviewToOutputDto(Review review) {
        ReviewOutputDto reviewOutputDto = new ReviewOutputDto();
        reviewOutputDto.id = review.getId();
        reviewOutputDto.rating = review.getRating();
        reviewOutputDto.description = review.getDescription();
        reviewOutputDto.verified = review.isVerified();
        reviewOutputDto.activities = review.getMatch().getActivities();
        reviewOutputDto.writtenBy = review.getWrittenBy().getName();
        String writtenBy = review.getWrittenBy().getName();
        String helpGiver = review.getMatch().getHelpGiver().getName();
        String helpReceiver = review.getMatch().getHelpReceiver().getName();
        reviewOutputDto.setWrittenFor(writtenBy.equals(helpGiver) ? helpReceiver : helpGiver);
        return reviewOutputDto;
    }

        public Review transferInputDtoToReview(ReviewInputDto reviewInputDto) {
        Review review = new Review();
        review.setRating(reviewInputDto.getRating());
        review.setDescription(reviewInputDto.getDescription());
        review.setVerified(reviewInputDto.isVerified());
        return review;
        }
    }

