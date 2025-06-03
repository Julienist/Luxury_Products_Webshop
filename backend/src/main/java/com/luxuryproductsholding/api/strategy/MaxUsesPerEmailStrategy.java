package com.luxuryproductsholding.api.strategy;

import com.luxuryproductsholding.api.DAO.PromocodeUsageLogRepository;
import com.luxuryproductsholding.api.models.Promocode;
import com.luxuryproductsholding.api.models.PromocodeUsageLog;
import com.luxuryproductsholding.api.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MaxUsesPerEmailStrategy implements DiscountStrategy {

    private final PromocodeUsageLogRepository logRepository;

    @Autowired
    public MaxUsesPerEmailStrategy(PromocodeUsageLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public boolean isApplicable(Promocode promocode, Order order, String email) {
        Integer maxPerEmail = promocode.getMaxUsesPerEmail();
        if (maxPerEmail == null || maxPerEmail <= 0) {
            return true;
        }
        if (maxPerEmail == 1) {
            return logRepository.findByEmailAndPromocode_Code(email, promocode.getCode()).isEmpty();
        }

        List<PromocodeUsageLog> usedLogs = logRepository.findAllByEmailAndPromocode_Code(email, promocode.getCode());

        return usedLogs.size() < maxPerEmail;
    }
}
