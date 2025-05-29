package com.luxuryproductsholding.api.DAO;

import com.luxuryproductsholding.api.models.PromocodeUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromocodeUsageLogRepository extends JpaRepository<PromocodeUsageLog, Long> {
    PromocodeUsageLog findByUserIdAndPromocodeId(long userId, long promocodeId);
    PromocodeUsageLog findByUserIdAndPromocodeCode(long userId, String promocodeCode);
    PromocodeUsageLog findByPromocodeCode(String promocodeCode);
}
