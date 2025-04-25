package com.zoho.repository;

import com.zoho.entity.City;
import com.zoho.entity.Country;
import com.zoho.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p JOIN p.city c JOIN p.country co " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(co.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Property> searchHotels(@Param("name") String name);

    Page<Property> findByCity(City city, Pageable pageable);

    Page<Property> findByCountry(Country country, Pageable pageable);

    Page<Property> findByCityAndCountry(City city, Country country, Pageable pageable);

    Optional<Property> findByName(String name);



//    List<Property> findByCity_NameContainingIgnoreCaseOrCountry_NameContainingIgnoreCase(String city, String country);
}