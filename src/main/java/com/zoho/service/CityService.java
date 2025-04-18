package com.zoho.service;

import com.zoho.entity.City;
import com.zoho.entity.Country;
import com.zoho.exception.ResourceNotFoundException;
import com.zoho.payload.CityDto;
import com.zoho.payload.CountryDto;
import com.zoho.repository.CityRepository;
import com.zoho.repository.CountryRepository;
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
public class CityService {

    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;
    private CountryRepository countryRepository;

    @Autowired
    public CityService(CityRepository cityRepository, ModelMapper modelMapper, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
        this.countryRepository = countryRepository;
    }

    public CityDto createCity(CityDto cityDto, long countryId) {

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryId));
        //Dto to entity
        City city = modelMapper.map(cityDto, City.class);
        city.setCountry(country);
        City savedCity = cityRepository.save(city);

        // Entity to Dto
        CityDto dto = modelMapper.map(savedCity, CityDto.class);
        return dto;
    }

    public void deleteCity(long id) {
        cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id)

        );
        cityRepository.deleteById(id);


    }


    public List<CityDto> getAllCity(int pageNo, int pageSize, String sortBy, String sortDir, Long countryId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<City> pageCities;

        // If countryId is provided, filter cities by country
        if (countryId != null) {
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new RuntimeException("Country not found with id: " + countryId));

            // Fetch cities for the given country with pagination
            pageCities = cityRepository.findByCountryId(countryId, pageable);

            // If no cities are found for the given country, throw an exception
            if (pageCities.getContent().isEmpty()) {
                throw new RuntimeException("No cities found for country with id: " + countryId);
            }

        } else {
            // If countryId is not provided, fetch all cities with pagination
            pageCities = cityRepository.findAll(pageable);
        }


        List<City> cities = pageCities.getContent();

        // Convert each City to CityDto
        List<CityDto> dtos = cities.stream()
                .map(city -> modelMapper.map(city, CityDto.class))
                .collect(Collectors.toList());
        return dtos;
    }


    public CityDto updateCity(long id, CityDto cityDto, Long countryId) {

        cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City not with the id:" + id));

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryId));

        //Dto to entity
        City city = modelMapper.map(cityDto, City.class);
        city.setId(id); // <-- This line is important!
        city.setCountry(country);

        City savedCity = cityRepository.save(city);

        // Entity to Dto
        CityDto dto = modelMapper.map(savedCity, CityDto.class);
        dto.setCountry(modelMapper.map(country, CountryDto.class));
        return dto;

    }

    public boolean existById(long id) {
        boolean exists = cityRepository.existsById(id);
        return exists;
    }

    public List<CityDto> findByCity(String name) {

        List<City> cities = cityRepository.findByNameIgnoreCase(name);
        if (cities.isEmpty()) {
            throw new ResourceNotFoundException("No cities found with name: " + name);
        }
        List<CityDto> dtos = cities.stream()
                .map(city -> modelMapper.map(city, CityDto.class))
                .collect(Collectors.toList());
        return dtos;
    }


}