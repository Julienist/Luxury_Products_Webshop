package com.luxuryproductsholding.api.services;

import com.luxuryproductsholding.api.DAO.*;
import com.luxuryproductsholding.api.DTO.*;
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
    private final UserService userService;

    @Autowired
    private final ProductRepository productRepository;

    public PromocodeService(PromocodeValidatorService validatorService,
                            PromocodeUsageLogRepository logRepository,
                            OrderService orderService,
                            PromocodeRepository promocodeRepository,
                            ProductRepository productRepository,
                            CategoryRepository categoryRepository,
                            UserService userService) {
        this.validatorService = validatorService;
        this.logRepository = logRepository;
        this.orderService = orderService;
        this.promocodeRepository = promocodeRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
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
        // Controleer of de promocode bestaat
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

        // Controleer of de promocode geldig is voor deze bestelling
        // Aan de hand van validatiestrategies
        if (!validatorService.validate(promocode, dummyOrder, dto.getEmail())) {
            throw new IllegalArgumentException("Promocode is niet geldig voor deze bestelling");
        }

        // Controleer of de promocode al gebruikt is door deze email
        if (promocode.getMaxUsesPerEmail() != null) {
            int currentUsage = logRepository.countByEmailAndPromocode(dto.getEmail(), promocode);
            if (currentUsage >= promocode.getMaxUsesPerEmail()) {
                throw new IllegalArgumentException("Deze promotiecode is al maximaal gebruikt voor uw email.");
            }
        }

        // set de gebruikte count van de promocode
        promocode.setUsedCount(promocode.getUsedCount() + 1);

        // Pas de korting toe
        // aan de hand van DiscountStrategies
        BigDecimal korting = validatorService.applyDiscount(promocode, dummyOrder, dto.getEmail());

        String UserEmail = this.userService.getAuthenticatedUser().getEmail();

        // log het gebruik van de promocode
        this.logPromocodeUsage(UserEmail, promocode, korting);

        // geef antwoord terug naar frontend
        return new PromocodeResponse(korting, true, "Promocode toegepast");
    }

    public void logPromocodeUsage(String email, Promocode promocode, BigDecimal korting) {
        PromocodeUsageLog log = new PromocodeUsageLog();
        log.setEmail(email);
        log.setUsedAt(java.time.LocalDateTime.now());
        log.setPromocode(promocode);
        log.setDiscountApplied(korting);

        logRepository.save(log);
    }

    public List<String> getAllPromocodeCodes() {
        try {
            List<Promocode> promocodes = promocodeRepository.findAll();
            return promocodes.stream()
                    .map(Promocode::getCode)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Fout bij het ophalen van de promotiecodes: " + e.getMessage(), e);
        }
    }

//    public void enablePromocode(PromocodeEnableDTO dto) {
//        Promocode promocode = promocodeRepository.findByCode(dto.getCode())
//                .orElseThrow(() -> new IllegalArgumentException("Promocode bestaat niet"));
//
//        if (dto.isActive()) {
//            throw new IllegalArgumentException("Promocode is al actief");
//        }
//
//        promocode.setActive(true);
//        promocodeRepository.save(promocode);
//    }

    public void disablePromocode(PromocodeDisableDTO dto) {
        Promocode promocode = promocodeRepository.findByCode(dto.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Promocode bestaat niet"));

        if (dto.isActive()) {
            throw new IllegalArgumentException("Promocode is al actief");
        }

        promocode.setActive(false);
        promocodeRepository.save(promocode);
    }

//    public String getAllPromocodes() {
//        List<Promocode> promocodes = promocodeRepository.findAll();
//        StringBuilder sb = new StringBuilder();
//        for (Promocode promocode : promocodes) {
//            sb.append(promocode.toString()).append("\n");
//        }
//        return sb.toString();
//    }

//    public List<PromocodesList> getAllPromocodes() {
//        try {
//        List<Promocode> promocodes = promocodeRepository.findAll();
//        return promocodes.stream()
//                .map(promocode -> new PromocodesList(List.of(promocode.getCode())))
//                .toList();
//        } catch (Exception e) {
//            throw new RuntimeException("Fout bij het ophalen van de promotiecodes: " + e.getMessage(), e);
//        }
//    }

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
