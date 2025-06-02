package com.luxuryproductsholding.api.DAO;

import com.luxuryproductsholding.api.models.PromocodeUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromocodeUsageLogRepository extends JpaRepository<PromocodeUsageLog, Long> {
    Optional<PromocodeUsageLog> findByEmailAndPromocode_Code(String email, String promocodeCode);

    List<PromocodeUsageLog> findAllByEmailAndPromocode_Code(String email, String promocodeCode);
}
