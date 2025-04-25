package com.zoho.repository;

import com.zoho.entity.Review;
import com.zoho.entity.SignUpUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByProperty_NameIgnoreCase(String propertyName, Pageable pageable);


    //------------------------user custom query----------------------------
    boolean existsBySignUpUserAndPropertyId(SignUpUser signUpUser, long propertyId);
//    ✅ correct field name
Page<Review> findBySignUpUser(SignUpUser user, Pageable pageable);


}
