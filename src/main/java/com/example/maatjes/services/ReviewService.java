package com.example.maatjes.services;

import com.example.maatjes.dtos.ReviewDto;
import com.example.maatjes.dtos.ReviewInputDto;
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

import java.util.Objects;

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

    public ReviewDto createReview (Long matchId, Long accountId, ReviewInputDto reviewInputDto) throws RecordNotFoundException, AccountNotAssociatedException {
        //ik ga op zoek naar de match
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));
        //ik ga op zoek naar het account van de schrijver van de review
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RecordNotFoundException("Account niet gevonden"));
        //als de schrijver van de review niet overeenkomt met de gever of receiver in de match, mag het niet.
        if (!match.getHelpGiver().equals(account) && !match.getHelpReceiver().equals(account)) {
            throw new AccountNotAssociatedException("Je kunt alleen een beoordeling schrijven over je eigen matches");
        }
        if (!match.isGiverAccepted() || !match.isReceiverAccepted()) {
            throw new AccountNotAssociatedException("Match moet eerst worden geaccepteerd voordat je een review kunt schrijven");}

        //voor alle bestaande reviews van de match
        for (Review r: match.getMatchReviews()) {
            //als de ID van de schrijver van één van die review overeenkomt met de accountId
            if (Objects.equals(r.getWrittenBy().getId(), accountId)){
                throw new BadRequestException("Je kunt maar één review schrijven over je match");
            }
        }
                Review review = transferInputDtoToReview(reviewInputDto);
                review.setMatch(match);
                review.setWrittenBy(account);

                reviewRepository.save(review);
                return transferReviewToDto(review);
            }




//    public List<ReviewDto> getReviews() {
//        List<Review> reviews = reviewRepository.findAll();
//        List<ReviewDto> reviewDtos = new ArrayList<>();
//        for (Review review : reviews) {
//            ReviewDto reviewDto = transferReviewToDto(review);
//            reviewDtos.add(reviewDto);
//        }
//        return reviewDtos;
//    }

    //get reviews from Account over wie het gaat() (verified == true)
    //get reviews from account welke die heeft geschreven. (verified == true)
    //get allreviews (admin)
    //get reviews to verify (admin)
    //(als user) createReview()
    //verify review() setVerified = true. feedback wanneer = not verified?
    //(als user) editOwnReview()
    //delete review

    public ReviewDto transferReviewToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.id = review.getId();
        reviewDto.rating = review.getRating();
        reviewDto.description = review.getDescription();
        reviewDto.verified = review.isVerified();
        reviewDto.activities = review.getMatch().getActivities();
        reviewDto.writtenBy = review.getWrittenBy().getName();
        String writtenBy = review.getWrittenBy().getName();
        String helpGiver = review.getMatch().getHelpGiver().getName();
        String helpReceiver = review.getMatch().getHelpReceiver().getName();
        reviewDto.setWrittenFor(writtenBy.equals(helpGiver) ? helpReceiver : helpGiver);

        return reviewDto;
    }
        public Review transferInputDtoToReview(ReviewInputDto reviewInputDto) {
        var review = new Review();
        review.setRating(reviewInputDto.getRating());
        review.setDescription(reviewInputDto.getDescription());
        review.setVerified(reviewInputDto.isVerified());
        return review;
        }
    }

