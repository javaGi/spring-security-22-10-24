package com.zoho.repository;

import com.zoho.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findByNameIgnoreCase(String name);

    Page<City> findByCountryId(Long countryId, Pageable pageable);

    boolean existsByNameContainingIgnoreCase(String name);
}