package com.fpt.evplatform.modules.review.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.deal.entity.Deal;
import com.fpt.evplatform.modules.deal.repository.DealRepository;
import com.fpt.evplatform.modules.review.dto.request.ReviewRequest;
import com.fpt.evplatform.modules.review.dto.response.ReviewResponse;
import com.fpt.evplatform.modules.review.entity.Review;
import com.fpt.evplatform.modules.review.mapper.ReviewMapper;
import com.fpt.evplatform.modules.review.repository.ReviewRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {

    ReviewRepository reviewRepository;
    DealRepository dealRepository;
    UserRepository userRepository;
    ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponse createReview(ReviewRequest req) {
        Deal deal = dealRepository.findById(req.getDealId())
                .orElseThrow(() -> new AppException(ErrorCode.DEAL_NOT_FOUND));

        User author = userRepository.findById(req.getAuthorId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        User target = userRepository.findById(req.getTargetId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (reviewRepository.findByDealAndAuthor(deal, author).isPresent()) {
            throw new AppException(ErrorCode.REVIEW_ALREADY_EXISTED);
        }

        Review review = reviewMapper.toEntity(req, deal, author, target);
        reviewRepository.save(review);

        return reviewMapper.toResponse(review);
    }

    public List<ReviewResponse> getReviewsByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return reviewRepository.findByTarget(user).stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getReviewsByAuthor(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return reviewRepository.findByAuthor(user).stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getReviewsByDeal(Integer dealId) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new AppException(ErrorCode.DEAL_NOT_FOUND));

        return reviewRepository.findByDeal(deal).stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReview(Integer reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);
        }
        reviewRepository.deleteById(reviewId);
    }
}
