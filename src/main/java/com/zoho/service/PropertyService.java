package com.zoho.service;


import com.zoho.repository.CityRepository;
import com.zoho.repository.CountryRepository;
import com.zoho.repository.PropertyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
