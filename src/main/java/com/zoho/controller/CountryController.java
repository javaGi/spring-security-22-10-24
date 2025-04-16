package com.zoho.controller;


import com.zoho.payload.CityDto;
import com.zoho.payload.CountryDto;
import com.zoho.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v/country")
public class CountryController {


    private CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }


    // http://localhost:8080/api/v/country/create

    @PostMapping("/create")
    public ResponseEntity<?> createCountry(@Validated @RequestBody CountryDto countryDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        CountryDto dto = countryService.createCountry(countryDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

//    http://localhost:8080/api/v/country/10
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCountry(@PathVariable long id){

        countryService.deleteCountry(id);
        return new ResponseEntity<>("Country is deleted", HttpStatus.OK);
    }

    //http://localhost:8080/api/v/country?pageNo=0&pageSize=5&sortBy=id
    @GetMapping
    public  ResponseEntity<List<CountryDto>>getAllCity(

            @RequestParam(name = "pageNo",defaultValue = "0",required = false) int pageNo,
            @RequestParam(name = "pageSize",defaultValue = "3",required = false) int pageSize,
            @RequestParam(name= "sortBy",defaultValue = "id",required = false) String sortBy,
            @RequestParam(name="sortDir",defaultValue = "asc",required = false) String sortDir
// always check spelling and this is case-sensitive .so it should be exact matched with name
    ){

        List<CountryDto> countryDtos = countryService.getAllCity(pageNo,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(countryDtos,HttpStatus.OK);
    }


    //http://localhost:8080/api/v/country/update/5

    @PutMapping("/update/{id}")
    public ResponseEntity<CountryDto> updateCountry(@PathVariable long id, @RequestBody CountryDto countryDto){

        CountryDto dto = countryService.updateCountry(id,countryDto);


        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

}
