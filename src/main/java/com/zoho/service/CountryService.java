package com.zoho.service;


import com.zoho.entity.City;
import com.zoho.entity.Country;
import com.zoho.exception.ResourceNotFoundException;
import com.zoho.payload.CityDto;
import com.zoho.payload.CountryDto;
import com.zoho.repository.CountryRepository;
import org.hibernate.validator.constraints.time.DurationMin;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private CountryRepository countryRepository;
    private ModelMapper modelMapper;

    public CountryService(CountryRepository countryRepository, ModelMapper modelMapper) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
    }


    public CountryDto createCountry(CountryDto countryDto) {

        //Dto to Entity
        Country country = modelMapper.map(countryDto, Country.class);
        Country savedCon = countryRepository.save(country);

        //Entity to Dto
        CountryDto dto = modelMapper.map(savedCon, CountryDto.class);
        return dto;

    }

    public void deleteCountry(long id) {
        countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country not with id " + id));

        countryRepository.deleteById(id);
    }

    public List<CountryDto> getAllCity(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Country> pageCountries = countryRepository.findAll(pageable);
        List<Country> countries = pageCountries.getContent();

        // Convert each Country to CountryDto
        List<CountryDto> dtos = countries.stream()
                .map(country -> modelMapper.map(country, CountryDto.class))
                .collect(Collectors.toList());
        return dtos;
    }

    public CountryDto updateCountry(long id, CountryDto countryDto) {
        countryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Country not with ID:" +id));

        // Dto to entity
        Country country = modelMapper.map(countryDto, Country.class);
        country.setId(id);// this is important part of the code
        Country saved = countryRepository.save(country);
        // Entity to Dto
        CountryDto dto = modelMapper.map(country, CountryDto.class);
        return dto;


    }
}
