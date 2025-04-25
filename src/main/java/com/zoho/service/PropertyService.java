package com.zoho.service;


import com.zoho.entity.City;
import com.zoho.entity.Country;
import com.zoho.entity.Property;
import com.zoho.exception.ResourceNotFoundException;
import com.zoho.payload.CityDto;
import com.zoho.payload.CountryDto;
import com.zoho.payload.CreateAndUpdatePropertyDto;
import com.zoho.payload.PropertyDto;
import com.zoho.repository.CityRepository;
import com.zoho.repository.CountryRepository;
import com.zoho.repository.PropertyRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
public class PropertyService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PropertyRepository propertyRepository;


    public CreateAndUpdatePropertyDto addProperty(long cityId, long countryId, CreateAndUpdatePropertyDto crAndUpDto) {

        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found with id: " + cityId));

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + countryId));

        // Convert DTO to entity
        Property property = modelMapper.map(crAndUpDto, Property.class);

        // Set city and country
        property.setCity(city);
        property.setCountry(country);

        // Save the property
        Property savedProperty = propertyRepository.save(property);

        // Convert back to DTO
        CreateAndUpdatePropertyDto dto = modelMapper.map(savedProperty, CreateAndUpdatePropertyDto.class);
        return dto;
    }


    public CreateAndUpdatePropertyDto updateProperty(Long id, long cityId, long countryId, CreateAndUpdatePropertyDto crAndUpDto) {
        // Fetch the existing property by ID
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        // Fetch the related city and country by their IDs
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + cityId));
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryId));

        // Update property details using ModelMapper
        property.setName(crAndUpDto.getName());
        property.setNo_of_guests(crAndUpDto.getNo_of_guests());
        property.setNo_of_bedrooms(crAndUpDto.getNo_of_bedrooms());
        property.setNo_of_bathrooms(crAndUpDto.getNo_of_bathrooms());
        property.setNo_of_beds(crAndUpDto.getNo_of_beds());

        // Set the updated city and country
        property.setCity(city);
        property.setCountry(country);

        // Save the updated property
        Property saved = propertyRepository.save(property);

        // Map the updated property to PropertyDto
        CreateAndUpdatePropertyDto dto = modelMapper.map(saved, CreateAndUpdatePropertyDto.class);
        dto.setCountry(modelMapper.map(country, CountryDto.class));
        dto.setCity(modelMapper.map(city, CityDto.class));
        return dto;
    }

    public void deleteProperty(Long id) {
        propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property was not found :" + id)
        );
        propertyRepository.deleteById(id);
    }

    public List<PropertyDto> getAllProperties(int page, int size, String sortBy, String sortDir, Long countryId, Long cityId) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Property> pageProperties;

        if (countryId != null && cityId != null) {
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryId));

            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + cityId));

            pageProperties = propertyRepository.findByCityAndCountry(city, country, pageable);
            if (pageProperties.isEmpty()) {
                throw new ResourceNotFoundException("No properties found for the given city and country");
            }

        } else if (cityId != null) {
            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + cityId));

            pageProperties = propertyRepository.findByCity(city, pageable);
            if (pageProperties.isEmpty()) {
                throw new ResourceNotFoundException("No properties found for city id: " + cityId);
            }

        } else if (countryId != null) {
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryId));

            pageProperties = propertyRepository.findByCountry(country, pageable);
            if (pageProperties.isEmpty()) {
                throw new ResourceNotFoundException("No properties found for country id: " + countryId);
            }

        } else {
            pageProperties = propertyRepository.findAll(pageable);
        }

        List<Property> properties = pageProperties.getContent();
        return properties.stream()
                .map(property -> modelMapper.map(property, PropertyDto.class))
                .collect(Collectors.toList());
    }



//    public List<PropertyDto> searchPropertiesByLocation(String location) {
//        List<Property> properties = propertyRepository.findByCity_NameContainingIgnoreCaseOrCountry_NameContainingIgnoreCase(location, location);
//
//        List<PropertyDto> dtos = properties.stream()
//                .map(property -> modelMapper.map(property, PropertyDto.class))
//                .collect(Collectors.toList());
//        return dtos;
//    }

    public List<PropertyDto> searchHotels(String name) {
        // Step 1: Check if any country or city matches
        boolean cityExists = cityRepository.existsByNameContainingIgnoreCase(name);
        boolean countryExists = countryRepository.existsByNameContainingIgnoreCase(name);

        if (!cityExists && !countryExists) {
            throw new RuntimeException("No city or country found matching: " + name);
        }

        // Step 2: Search properties
        List<Property> properties = propertyRepository.searchHotels(name);

        if (properties.isEmpty()) {
            throw new RuntimeException("No properties found in city or country matching: " + name);
        }

        // Step 3: Map to DTOs
        List<PropertyDto> dtos = properties.stream()
                .map(property -> modelMapper.map(property, PropertyDto.class))
                .collect(Collectors.toList());
        return dtos;
    }


}



/* @Transactional ensures all DB operations inside addProperty() are executed within a single transaction.

If either cityId or countryId is invalid, an exception is thrown and the transaction is rolled back.

You should make sure CreateAndUpdatePropertyDto has matching field names with Property for ModelMapper to work smoothly. */