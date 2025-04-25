package com.zoho.controller;

import com.zoho.entity.SignUpUser;
import com.zoho.payload.ReviewDto;
import com.zoho.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    // http://localhost:8080/api/v/reviews/create/propertyId=1
    @PostMapping("/create")
    public ResponseEntity<ReviewDto> create(@RequestBody ReviewDto dto,
                                            @RequestParam long propertyId,
                                            @AuthenticationPrincipal SignUpUser signUpUser) {
        ReviewDto review = reviewService.createReview(dto, propertyId, signUpUser);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }


    //   http://localhost:8080/api/v/reviews/user
    @GetMapping("/user")
    public ResponseEntity<List<ReviewDto>> getReviewsByUser(
            @AuthenticationPrincipal SignUpUser user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        List<ReviewDto> dtos = reviewService.getReviewsByUser(user, page, size, sortBy, sortDir);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


//    http://localhost:8080/api/v/reviews/update/3

    @PutMapping("/update/{id}")
    public ResponseEntity<ReviewDto> updateMyReview(
            @PathVariable Long id,
            @RequestBody ReviewDto dto,
            @AuthenticationPrincipal SignUpUser user) {
        ReviewDto updated = reviewService.updateMyReview(id, dto, user);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }


  //  http://localhost:8080/api/v/reviews/delete/3
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal SignUpUser user) {

        reviewService.deleteReview(id, user);
        return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
    }



    /*------------------------admin module ---------------------------------*/

//   @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<ReviewDto> update(@PathVariable Long id, @RequestBody ReviewDto dto) {
        ReviewDto dtos = reviewService.updateReview(id, dto);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

//   @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>("Review is deleted", HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/property/{propertyName}")
    public ResponseEntity<List<ReviewDto>> getByProperty(
            @PathVariable String propertyName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        List<ReviewDto> dtos = reviewService.getReviewsByProperty(propertyName, page, size, sortBy, sortDir);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
