package com.luxuryproductsholding.api.DAO;

import com.luxuryproductsholding.api.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String scopeValue);

    Optional<Category> findByName(String name);

}
