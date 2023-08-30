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
import com.example.maatjes.util.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.time.LocalDate;

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
        Match match = matchRepository.findById(reviewInputDto.getMatchId()).orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));

        String helpGiverName = match.getHelpGiver().getUser().getUsername();
        String helpReceiverName = match.getHelpReceiver().getUser().getUsername();

        SecurityUtils.validateUsernames(helpGiverName, helpReceiverName, "matches");

        if (!match.isGiverAccepted() || !match.isReceiverAccepted()) {
            throw new BadRequestException("Match moet eerst worden geaccepteerd voordat er een review kan worden geschreven");
        }

        for (Review r: match.getMatchReviews()) {
            if (Objects.equals(r.getWrittenBy().getUser().getUsername(), SecurityContextHolder.getContext().getAuthentication().getName())){
                throw new BadRequestException("Je kunt maar één review schrijven over je match");
            }
        }
                Review review = transferInputDtoToReview(reviewInputDto);
                review.setMatch(match);
                User user = userRepository.findById(SecurityContextHolder.getContext().getAuthentication().getName())
                        .orElseThrow(() -> new RecordNotFoundException("User niet gevonden"));
                Account writer = user.getAccount();
                review.setWrittenBy(writer);

                review.setWrittenFor(match.getHelpReceiver().equals(writer) ? match.getHelpGiver() : match.getHelpReceiver());
                review.setDate(LocalDate.now());
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
            if (!review.isVerified() && review.getFeedbackAdmin() == null) {
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
            if (feedbackAdmin == null) {
                throw new BadRequestException("FeedbackAdmin is vereist wanneer de review niet is geverifieerd.");
            }
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
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("De review met ID " + reviewId + " bestaat niet."));

        String username = review.getWrittenBy().getUser().getUsername();
        SecurityUtils.validateUsername(username, "reviews om aan te passen");

        review.setFeedbackAdmin(null);
        review = reviewRepository.save(transferInputDtoToReview(review, reviewInputDto));
        return  transferReviewToOutputDto(review);
    }

    public String removeReview(@RequestBody Long reviewId) throws RecordNotFoundException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("Review niet gevonden"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isSelf = review.getWrittenBy().getUser().getUsername().equals(authentication.getName());

        if (isAdmin || isSelf) {
            reviewRepository.deleteById(reviewId);
        } else {
            throw new AccessDeniedException("Je hebt geen toegang tot deze review");
        }
        return "Review succesvol verwijderd";
    }

    public Review transferInputDtoToReview(ReviewInputDto reviewInputDto) {
        Review review = new Review();
        return transferInputDtoToReview(review, reviewInputDto);
    }

    public Review transferInputDtoToReview(Review review, ReviewInputDto reviewInputDto) {
        review.setRating(reviewInputDto.getRating());
        review.setDescription(reviewInputDto.getDescription());
        review.setVerified(reviewInputDto.isVerified());
        return review;
    }

    public ReviewOutputDto transferReviewToOutputDto(Review review) {
        ReviewOutputDto reviewOutputDto = new ReviewOutputDto();
        reviewOutputDto.id = review.getId();
        reviewOutputDto.rating = review.getRating();
        reviewOutputDto.description = review.getDescription();
        reviewOutputDto.verified = review.isVerified();
        reviewOutputDto.activities = review.getMatch().getActivities();
        reviewOutputDto.date = review.getDate();
        reviewOutputDto.feedbackAdmin = review.getFeedbackAdmin();
        String writtenBy = review.getWrittenBy().getUser().getUsername();
        reviewOutputDto.writtenBy = writtenBy;
        String helpGiver = review.getMatch().getHelpGiver().getUser().getUsername();
        String helpReceiver = review.getMatch().getHelpReceiver().getUser().getUsername();
        reviewOutputDto.setWrittenFor(writtenBy.equals(helpGiver) ? helpReceiver : helpGiver);
        return reviewOutputDto;
    }

}

