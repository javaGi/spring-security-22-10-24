package com.zoho.controller;

import com.zoho.payload.CreateAndUpdatePropertyDto;
import com.zoho.payload.PropertyDto;
import com.zoho.repository.PropertyRepository;
import com.zoho.service.PropertyService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v/property")
public class PropertyController {

    private PropertyService propertyService;
    private final PropertyRepository propertyRepository;

    public PropertyController(PropertyService propertyService,
                              PropertyRepository propertyRepository) {

        this.propertyService = propertyService;
        this.propertyRepository = propertyRepository;
    }


    // ✅ 1. Add Property
    //Example POST http://localhost:8080/api/v/property/add?cityId=2&countryId=2
    @PostMapping("/add")
    public ResponseEntity<?> addProperty(@RequestParam("cityId") @Valid long cityId,
                                         @RequestParam("countryId") @Valid long countryId,
                                         @RequestBody @Valid CreateAndUpdatePropertyDto crAndUpDto,
                                         BindingResult bindingResult) {

        // Check for validation errors in request body or params
        if (bindingResult.hasErrors()) {
            // Collect all error messages into a list
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            // Return the error messages with Bad Request status
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }

        // Proceed to add the property
        CreateAndUpdatePropertyDto dto = propertyService.addProperty(cityId, countryId, crAndUpDto);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }


    // Example PUT http://localhost:8080/api/v/property/update/3?cityId=5&countryId=2
    // ✅ 2. Update Property
    @PutMapping("/update/{id}")
    public ResponseEntity<CreateAndUpdatePropertyDto> updateProperty(@PathVariable long id,
                                                                     @RequestParam long cityId,
                                                                     @RequestParam long countryId,
                                                                     @RequestBody @Valid CreateAndUpdatePropertyDto crAndUpDto) {
        CreateAndUpdatePropertyDto dto = propertyService.updateProperty(id, cityId, countryId, crAndUpDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    //Example DELETE : http://localhost:8080/api/v/property/delete/2
    // ✅ 3. Delete Property
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok("Property deleted successfully");
    }


    // ✅ 4. Get All with Pagination and Sorting
    // Example GET : http://localhost:8080/api/v/property/all?page=0&size=5&sortBy=name&sortDir=asc&cityId=1&countyId=1
    @GetMapping("/properties")
    public ResponseEntity<List<PropertyDto>> getAllProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Long countryId,
            @RequestParam(required = false) Long cityId
    ) {
        List<PropertyDto> dtos = propertyService.getAllProperties(page, size, sortBy, sortDir, countryId, cityId);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }



//    // ✅ 5. Search by Location (city or country name)
//    // Example GET : http://localhost:8080/api/v/property/search?location=delhi
//    @GetMapping("/search")
//    public ResponseEntity<List<PropertyDto>> searchByLocation(@RequestParam String location) {
//        List<PropertyDto> dtos = propertyService.searchPropertiesByLocation(location);
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }


    //    ✅ 6. Search by Location (city or country name)
   //   Example GET : http://localhost:8080/api/v/property/search-hotels?name=mumbai
    @GetMapping("/search-hotels")
    public ResponseEntity<List<PropertyDto>> searchHotels(
            @RequestParam String name
    ) {

        List<PropertyDto> dtos = propertyService.searchHotels(name);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

}
