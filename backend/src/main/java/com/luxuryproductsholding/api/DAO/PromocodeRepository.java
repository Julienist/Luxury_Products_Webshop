package com.luxuryproductsholding.api.DAO;

import com.luxuryproductsholding.api.models.Promocode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromocodeRepository extends JpaRepository<Promocode, Long> {
    Optional<Promocode> findByCode(String code);

    Promocode findByCodeAndActiveTrue(String code);
    Promocode findByCodeAndActiveFalse(String code);

    boolean existsByCode(String code);
}
