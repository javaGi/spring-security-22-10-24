package com.zoho.controller;


import com.zoho.exception.ResourceNotFoundException;
import com.zoho.payload.CityDto;
import com.zoho.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v/city")
public class CityController {


    //http://localhost:8080/api/v/city
    private CityService cityService;


    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    //http://localhost:8080/api/v/city/create

    @PostMapping("/create")
    public ResponseEntity<?> createCity(@Validated @RequestBody CityDto cityDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        CityDto dto = cityService.createCity(cityDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);

    }

// http://localhost:8080/api/v/city/11
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCity(@PathVariable long id){
        cityService.deleteCity(id);

        return  new ResponseEntity<>("City is deleted",HttpStatus.OK);
    }

    //http://localhost:8080/api/v/city?pageNo=0&pageSize=5&sortBy=name or id
    @GetMapping
    public  ResponseEntity<List<CityDto>>getAllCity(

            @RequestParam(name = "pageNo",defaultValue = "0",required = false) int pageNo,
            @RequestParam(name = "pageSize",defaultValue = "3",required = false) int pageSize,
            @RequestParam(name= "sortBy",defaultValue = "id",required = false) String sortBy,
            @RequestParam(name="sortDir",defaultValue = "asc",required = false) String sortDir
// always check spelling and this is case-sensitive .so it should be exact matched with name
    ){

        List<CityDto> cityDtos = cityService.getAllCity(pageNo,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(cityDtos,HttpStatus.OK);
    }

    // http://localhost:8080/api/v/city/update/6

    @PutMapping("/update/{id}")
public ResponseEntity<CityDto> updateCity(@PathVariable long id, @RequestBody CityDto cityDto){

        CityDto dto = cityService.updateCity(id,cityDto);


         return new ResponseEntity<>(dto, HttpStatus.CREATED);
}

    //http://localhost:8080/api/v/city/exists/14
    // GET - Check if City Exists by ID
    @GetMapping("/exists/{id}")
    public ResponseEntity<String> existById(@PathVariable long id) {
        boolean exists = cityService.existById(id);
        if (exists) {
            return new ResponseEntity<>("City exists with the id", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("City not found", HttpStatus.NOT_FOUND);
        }

    }

   // http://localhost:8080api/v/city/by-name/pune

    // GET - Find City by Name
    @GetMapping("/by-name/{name}")
    public ResponseEntity<List<CityDto>> findByCity(@PathVariable String name) {
        List<CityDto> cities = cityService.findByCity(name);
        if (cities.isEmpty()) {
            throw new ResourceNotFoundException("No cities found with name: " + name);
        }
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

}


