package com.luxuryproductsholding.api.DAO;

import com.luxuryproductsholding.api.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<CustomUser, Long> {
    CustomUser findByEmail(String email);
}
