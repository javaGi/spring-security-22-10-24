package com.zoho.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePropertyRequest {
    private String name;
    private Integer no_of_guests;
    private Integer no_of_bedrooms;
    private Integer no_of_bathrooms;
    private Integer no_of_beds;
    private Long country_id;
    private Long city_id;
}
