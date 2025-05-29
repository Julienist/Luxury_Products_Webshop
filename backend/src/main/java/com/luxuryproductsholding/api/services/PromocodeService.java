package com.luxuryproductsholding.api.services;

import com.luxuryproductsholding.api.DAO.PromocodeRepository;
import com.luxuryproductsholding.api.DAO.PromocodeUsageLogRepository;
import com.luxuryproductsholding.api.DTO.OrderRequest;
import com.luxuryproductsholding.api.models.Order;
import com.luxuryproductsholding.api.models.Promocode;
import com.luxuryproductsholding.api.models.PromocodeUsageLog;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromocodeService {

    private final PromocodeRepository promocodeRepository;
    private final PromocodeValidatorService validatorService;
    private final PromocodeUsageLogRepository logRepository;
    private final OrderService orderService;

    public PromocodeService(PromocodeRepository promocodeRepository,
                            PromocodeValidatorService validatorService,
                            PromocodeUsageLogRepository logRepository,
                            OrderService orderService) {
        this.promocodeRepository = promocodeRepository;
        this.validatorService = validatorService;
        this.logRepository = logRepository;
        this.orderService = orderService;
    }

//    public Order validateAndApplyPromocode(OrderRequest dto) {
//        Promocode promocode = promocodeRepository.findByCode(dto.getPromocode())
//                .orElseThrow(() -> new IllegalArgumentException("Promocode bestaat niet"));
//
//        if (!validatorService.validate(promocode, dto.toOrder(), dto.getEmail())) {
//            throw new IllegalArgumentException("Promocode niet geldig voor deze bestelling");
//        }
//
//        BigDecimal korting = validatorService.applyDiscount(promocode, dto.toOrder(), dto.getEmail());
//        Order order = orderService.createOrderWithDiscount(dto.toOrder(), korting);
//
//        logRepository.save(new PromocodeUsageLog(dto.getEmail(), promocode, korting, LocalDateTime.now()));
//
//        return order;
//    }

    public Promocode getPromocodeByCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Promocode mag niet leeg zijn.");
        }
        return promocodeRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Promocode bestaat niet."));
        // eigen exception maken..
    }

    public void validatePromocode(String code, Order order, String email) {
        Promocode promocode = getPromocodeByCode(code);
        if (!validatorService.validate(promocode, order, email)) {
            throw new IllegalArgumentException("Promocode is niet geldig voor deze bestelling.");
        }
    }

    public BigDecimal applyPromocode(String code, Order order, String email) {
        Promocode promocode = getPromocodeByCode(code);
        return validatorService.applyDiscount(promocode, order, email);
    }
}

