package com.example.maatjes.services;

import com.example.maatjes.dtos.outputDtos.ReviewOutputDto;
import com.example.maatjes.dtos.inputDtos.ReviewInputDto;
import com.example.maatjes.exceptions.AccessDeniedException;
import com.example.maatjes.exceptions.BadRequestException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.Match;
import com.example.maatjes.models.Review;
import com.example.maatjes.models.User;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.MatchRepository;
import com.example.maatjes.repositories.ReviewRepository;
import com.example.maatjes.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MatchRepository matchRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, MatchRepository matchRepository, AccountRepository accountRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.matchRepository = matchRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public ReviewOutputDto createReview (ReviewInputDto reviewInputDto) throws RecordNotFoundException, AccessDeniedException, BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

                Match match = matchRepository.findById(reviewInputDto.getMatchId()).orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));
        boolean isAssociatedUser = username.equals(match.getHelpGiver().getUser().getUsername())
                || username.equals(match.getHelpReceiver().getUser().getUsername());

        if (!isAssociatedUser) {
            throw new AccessDeniedException("Je kunt alleen een beoordeling schrijven over je eigen matches");
        }

        if (!match.isGiverAccepted() || !match.isReceiverAccepted()) {
            throw new BadRequestException("Match moet eerst worden geaccepteerd voordat er een review kan worden geschreven");
        }

        for (Review r: match.getMatchReviews()) {
            if (Objects.equals(r.getWrittenBy().getUser().getUsername(), username)){
                throw new BadRequestException("Je kunt maar één review schrijven over je match");
            }
        }
                Review review = transferInputDtoToReview(reviewInputDto);
                review.setMatch(match);
                User user = userRepository.findById(username)
                        .orElseThrow(() -> new RecordNotFoundException("User niet gevonden"));
                Account writer = user.getAccount();
                review.setWrittenBy(writer);

                review.setWrittenFor(match.getHelpReceiver().equals(writer) ? match.getHelpGiver() : match.getHelpReceiver());
                reviewRepository.save(review);
                return transferReviewToOutputDto(review);
            }

    public ReviewOutputDto getReview(Long reviewId) throws RecordNotFoundException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("Review niet gevonden"));
        return transferReviewToOutputDto(review);
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

    public ReviewOutputDto verifyReview(Long reviewId, String feedbackAdmin, boolean verify) throws RecordNotFoundException, BadRequestException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("Review niet gevonden"));

        if (review.isVerified()) {
            throw new BadRequestException("Review is al geverifieerd");
        } else if (!verify) {
            review.setFeedbackAdmin(feedbackAdmin);
            review.setVerified(false);
        } else {
            review.setFeedbackAdmin(null);
            review.setVerified(true);
        }
        reviewRepository.save(review);
        return transferReviewToOutputDto(review);
    }

    public ReviewOutputDto updateReview(Long reviewId, ReviewInputDto reviewInputDto) throws RecordNotFoundException, AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("De review met ID " + reviewId + " bestaat niet."));
        if (!review.getWrittenBy().getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Je kunt alleen je eigen review aanpassen");
        }
        review.setRating(reviewInputDto.getRating());
        review.setDescription(reviewInputDto.getDescription());
        review.setVerified(false);
        Review returnReview = reviewRepository.save(review);
        return  transferReviewToOutputDto(returnReview);
    }

    public String removeReview(@RequestBody Long reviewId) throws RecordNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("Review niet gevonden"));
        boolean isSelf = review.getWrittenBy().getUser().getUsername().equals(authentication.getName());
        if (isAdmin || isSelf) {
            reviewRepository.deleteById(reviewId);
        } else {
            throw new AccessDeniedException("Je hebt geen toegang tot deze review");
        }
        return "Review succesvol verwijderd";
    }

    public ReviewOutputDto transferReviewToOutputDto(Review review) {
        ReviewOutputDto reviewOutputDto = new ReviewOutputDto();
        reviewOutputDto.id = review.getId();
        reviewOutputDto.rating = review.getRating();
        reviewOutputDto.description = review.getDescription();
        reviewOutputDto.verified = review.isVerified();
        reviewOutputDto.activities = review.getMatch().getActivities();
        reviewOutputDto.writtenBy = review.getWrittenBy().getName();
        reviewOutputDto.date = review.getDate();
        reviewOutputDto.feedbackAdmin = review.getFeedbackAdmin();
        String writtenBy = review.getWrittenBy().getUser().getUsername();
        String helpGiver = review.getMatch().getHelpGiver().getUser().getUsername();
        String helpReceiver = review.getMatch().getHelpReceiver().getUser().getUsername();
        reviewOutputDto.setWrittenFor(writtenBy.equals(helpGiver) ? helpReceiver : helpGiver);
        return reviewOutputDto;
    }

        public Review transferInputDtoToReview(ReviewInputDto reviewInputDto) {
        Review review = new Review();
        review.setRating(reviewInputDto.getRating());
        review.setDescription(reviewInputDto.getDescription());
        review.setVerified(reviewInputDto.isVerified());
        review.setDate(reviewInputDto.getDate());
        return review;
        }
    }

