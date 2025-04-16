package com.zoho.service;

import com.zoho.entity.City;
import com.zoho.exception.ResourceNotFoundException;
import com.zoho.payload.CityDto;
import com.zoho.repository.CityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CityService(CityRepository cityRepository, ModelMapper modelMapper) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
    }

    public CityDto createCity(CityDto cityDto) {
        //Dto to entity
        City city = modelMapper.map(cityDto, City.class);
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


    public List<CityDto> getAllCity(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<City> pageCities = cityRepository.findAll(pageable);
        List<City> cities = pageCities.getContent();

        // Convert each City to CityDto
        List<CityDto> dtos = cities.stream()
                .map(city -> modelMapper.map(city, CityDto.class))
                .collect(Collectors.toList());
        return dtos;
    }


    public CityDto updateCity(long id, CityDto cityDto) {

        cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City not with the id:" + id));

        //Dto to entity
        City city = modelMapper.map(cityDto, City.class);
        city.setId(id); // <-- This line is important!
        City savedCity = cityRepository.save(city);

        // Entity to Dto
        CityDto dto = modelMapper.map(savedCity, CityDto.class);
        return dto;

    }

    public boolean existById(long id) {
        cityRepository.findById(id).orElseThrow(() -> new
                ResourceNotFoundException("City not Found with id :"));
        boolean exists = cityRepository.existsById(id);
        return exists;
    }

    public List<CityDto> findByCity(String name) {
        List<City> cities = cityRepository.findByNameIgnoreCase(name);
        List<CityDto> dtos = cities.stream()
                .map(city -> modelMapper.map(city, CityDto.class))
                .collect(Collectors.toList());
        return dtos;
    }


}