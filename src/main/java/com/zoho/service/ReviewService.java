package com.zoho.service;

import com.zoho.entity.Property;
import com.zoho.entity.Review;
import com.zoho.entity.SignUpUser;
import com.zoho.exception.ResourceNotFoundException;
import com.zoho.payload.ReviewDto;
import com.zoho.repository.PropertyRepository;
import com.zoho.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private ModelMapper modelMapper;


    public ReviewDto createReview(ReviewDto dto, long propertyId, SignUpUser signUpUser) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        // ✅ Check one review per user per property
        if (reviewRepository.existsBySignUpUserAndPropertyId(signUpUser, propertyId)) {
            throw new IllegalArgumentException("User can only leave one review per property.");
        }

        Review review = modelMapper.map(dto, Review.class);
        review.setProperty(property); // ✅ set property
        review.setSignUpUser(signUpUser); // ✅ set user (this must not be null)
        review.setCreatedAt(LocalDateTime.now());


        return mapToDto(reviewRepository.save(review));
    }

    public List<ReviewDto> getReviewsByUser(SignUpUser user, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Review> pageReviews = reviewRepository.findBySignUpUser(user, pageable);

        return pageReviews.getContent()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public ReviewDto updateMyReview(Long reviewId, ReviewDto dto, SignUpUser user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getSignUpUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only update your own review.");
        }

        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        return mapToDto(reviewRepository.save(review));
    }


    public void deleteReview(Long id, SignUpUser user) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        // ✅ Only the creator of the review can delete it
        if (!review.getSignUpUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to delete this review.");
        }

        reviewRepository.delete(review);
    }





    /*------------------------admin module ---------------------------------*/

    public ReviewDto updateReview(Long id, ReviewDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        review.setContent(dto.getContent());
        review.setRating(dto.getRating());

        ReviewDto dtos = mapToDto(reviewRepository.save(review));
        return dtos;
    }


    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        reviewRepository.delete(review);
    }


    public List<ReviewDto> getReviewsByProperty(String propertyName, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        propertyName = propertyName.trim();
        Page<Review> pageReviews = reviewRepository.findByProperty_NameIgnoreCase(propertyName, pageable);

        List<Review> reviews = pageReviews.getContent();
        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found for property: " + propertyName);
        }

        return pageReviews.getContent()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ReviewDto mapToDto(Review review) {
        ReviewDto dto = modelMapper.map(review, ReviewDto.class);
        dto.setPropertyName(review.getProperty().getName());
        dto.setCityName(review.getProperty().getCity().getName());
        dto.setCountryName(review.getProperty().getCity().getCountry().getName());
        return dto;
    }
}
