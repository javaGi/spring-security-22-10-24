package com.zoho.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyDto {

    @NotBlank(message = "Property name must not be blank")
    private String name;

    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "At least one guest is required")
    private Integer no_of_guests;

    @NotNull(message = "Number of bedrooms is required")
    @Min(value = 1, message = "At least one bedroom is required")
    private Integer no_of_bedrooms;

    @NotNull(message = "Number of bathrooms is required")
    @Min(value = 1, message = "At least one bathroom is required")
    private Integer no_of_bathrooms;

    @NotNull(message = "Number of beds is required")
    @Min(value = 1, message = "At least one bed is required")
    private Integer no_of_beds;

    // Response-only fields

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CityDto city;

}
