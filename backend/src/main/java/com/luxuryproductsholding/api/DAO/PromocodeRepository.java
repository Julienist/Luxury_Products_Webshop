package com.luxuryproductsholding.api.DAO;

import com.luxuryproductsholding.api.models.Promocode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromocodeRepository extends JpaRepository<Promocode, Long> {
    Promocode findByCode(String code);
    Promocode findByCodeAndActiveTrue(String code);
    Promocode findByCodeAndActiveFalse(String code);
}
