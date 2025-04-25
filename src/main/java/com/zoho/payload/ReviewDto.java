package com.zoho.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class ReviewDto {

    private String content;
    private int rating;
    private String propertyName;
    private String cityName;
    private String countryName;
    private LocalDateTime createdAt;
    private Long signUpUserId;  // Store user ID instead of entire object

    // Getters and setters
}
