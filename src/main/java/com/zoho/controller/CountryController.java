package com.zoho.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v/country")
public class CountryController {

    // http://localhost:8080/api/v/country
    // http://localhost:8080/api/v/country/addCountry
    @PostMapping("/addCountry")
    public String addCountry(){
        return "added";
    }
}
