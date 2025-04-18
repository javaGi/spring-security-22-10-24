package com.zoho.repository;

import com.zoho.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p JOIN p.city c JOIN p.country co " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(co.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Property> searchHotels(@Param("name") String name);





//    List<Property> findByCity_NameContainingIgnoreCaseOrCountry_NameContainingIgnoreCase(String city, String country);
}