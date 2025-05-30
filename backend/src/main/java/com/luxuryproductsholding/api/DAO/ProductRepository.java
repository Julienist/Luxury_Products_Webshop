package com.luxuryproductsholding.api.DAO;

import com.luxuryproductsholding.api.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String scopeValue);

    Optional<Product> findByName(String name);
}
