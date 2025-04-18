package com.zoho.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAndUpdatePropertyDto {
    @NotBlank(message = "Property name is required")
    private String name;

    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "Minimum 1 guest required")
    private Integer no_of_guests;

    @NotNull(message = "Number of bedrooms is required")
    @Min(value = 1, message = "Minimum 1 bedroom required")
    private Integer no_of_bedrooms;

    @NotNull(message = "Number of bathrooms is required")
    @Min(value = 1, message = "Minimum 1 bathroom required")
    private Integer no_of_bathrooms;

    @NotNull(message = "Number of beds is required")
    @Min(value = 1, message = "Minimum 1 bed required")
    private Integer no_of_beds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CountryDto country;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CityDto city;


}
