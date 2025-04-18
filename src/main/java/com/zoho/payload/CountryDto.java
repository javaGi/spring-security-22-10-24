package com.zoho.payload;


import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
public class CountryDto {

    private long id;
    @Size(min = 2,message = "the name should be at least 2 characters")
    private String name;
}
