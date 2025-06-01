package com.luxuryproductsholding.api.services;

import com.luxuryproductsholding.api.DAO.*;
import com.luxuryproductsholding.api.DTO.PromocodeIntermediaryRequest;
import com.luxuryproductsholding.api.DTO.PromocodeRequest;
import com.luxuryproductsholding.api.DTO.PromocodeResponse;
import com.luxuryproductsholding.api.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PromocodeService {

    private final PromocodeValidatorService validatorService;
    private final PromocodeUsageLogRepository logRepository;
    private final OrderService orderService;
    private final PromocodeRepository promocodeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    private final ProductRepository productRepository;

    public PromocodeService(PromocodeValidatorService validatorService,
                            PromocodeUsageLogRepository logRepository,
                            OrderService orderService,
                            PromocodeRepository promocodeRepository,
                            ProductRepository productRepository,
                            CategoryRepository categoryRepository,
                            UserRepository userRepository) {
        this.validatorService = validatorService;
        this.logRepository = logRepository;
        this.orderService = orderService;
        this.promocodeRepository = promocodeRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public void createPromocodeAfterValidation(PromocodeRequest dto) {
        dto.setCreationDate(java.time.LocalDateTime.now());
        dto.setUsedCount(0);

        this.validatorService.validatePromocodeOnCreation(dto);
        Set<Product> products = new HashSet<>();
        Set<Category> categories = new HashSet<>();

        if ("PRODUCT".equalsIgnoreCase(dto.getScopeType())) {
            Product product = (Product) productRepository.findByName(dto.getScopeValue())
                    .orElseThrow(() -> new IllegalStateException("Product werd niet gevonden, ondanks validatie"));
            products.add(product);
        } else if ("CATEGORY".equalsIgnoreCase(dto.getScopeType())) {
            Category category = (Category) categoryRepository.findByName(dto.getScopeValue())
                    .orElseThrow(() -> new IllegalStateException("Categorie werd niet gevonden, ondanks validatie"));
            categories.add(category);
        }

        Promocode newPromocode = new Promocode();

        newPromocode.setCode(dto.getCode());
        newPromocode.setActive(true);
        newPromocode.setDiscountType(DiscountType.valueOf(dto.getDiscountType()));
        newPromocode.setDiscountValue(dto.getDiscountValue());
        newPromocode.setMinimumOrderAmount(dto.getMinOrderAmount());
        newPromocode.setMaxUsesPerEmail(dto.getMaxUsesPerEmail());
        newPromocode.setApplicableProducts(products);
        newPromocode.setApplicableCategories(categories);
        newPromocode.setCreationDate(dto.getCreationDate());
        newPromocode.setExpiryDate(dto.getExpiryDate());
        newPromocode.setUsedCount(dto.getUsedCount());

        promocodeRepository.save(newPromocode);
    }

    public PromocodeResponse validatePromocode(PromocodeIntermediaryRequest dto) {
        Promocode promocode = promocodeRepository.findByCode(dto.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Promocode bestaat niet"));

        // Map cart items naar dummy Order
        List<OrderItem> orderItems = dto.getCartItems().stream().map(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product ID " + item.getProductId() + " niet gevonden"));
            return new OrderItem(product, item.getProductName(), item.getQuantity(), BigDecimal.valueOf(item.getPrice()));
        }).toList();


        Order dummyOrder = new Order();
        dummyOrder.setEmail(dto.getEmail());
        dummyOrder.setOrderItems(orderItems);
        dummyOrder.setTotalPrice(dto.getTotalPrice());


        if (!validatorService.validate(promocode, dummyOrder, dto.getEmail())) {
            throw new IllegalArgumentException("Promocode is niet geldig voor deze bestelling");
        }

        BigDecimal korting = validatorService.applyDiscount(promocode, dummyOrder, dto.getEmail());

        return new PromocodeResponse(korting, true, "Promocode toegepast");
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

//    public Promocode getPromocodeByCode(String code) {
//        if (code == null || code.isEmpty()) {
//            throw new IllegalArgumentException("Promocode mag niet leeg zijn.");
//        }
//        return promocodeRepository.findByCode(code)
//                .orElseThrow(() -> new IllegalArgumentException("Promocode bestaat niet."));
//        // eigen exception maken..
//    }

//    public void validatePromocode(String code, Order order, String email) {
//        Promocode promocode = getPromocodeByCode(code);
//        if (!validatorService.validate(promocode, order, email)) {
//            throw new IllegalArgumentException("Promocode is niet geldig voor deze bestelling.");
//        }
//    }

//    public BigDecimal applyPromocode(String code, Order order, String email) {
//        Promocode promocode = getPromocodeByCode(code);
//        return validatorService.applyDiscount(promocode, order, email);
//    }
}

