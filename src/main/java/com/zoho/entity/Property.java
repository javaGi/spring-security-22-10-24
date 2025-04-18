package com.zoho.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "no_of_guests", nullable = false)
    private Integer no_of_guests;

    @Column(name = "no_of_bedrooms", nullable = false)
    private Integer no_of_bedrooms;

    @Column(name = "no_of_bathrooms", nullable = false)
    private Integer no_of_bathrooms;

    @Column(name = "no_of_beds", nullable = false)
    private Integer no_of_beds;

    // ✅ Link to country
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    // ✅ Link to city
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}
