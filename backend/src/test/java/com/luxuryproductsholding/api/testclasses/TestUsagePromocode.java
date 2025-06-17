package com.luxuryproductsholding.api.testclasses;

import com.luxuryproductsholding.api.DAO.*;
import com.luxuryproductsholding.api.DTO.*;
import com.luxuryproductsholding.api.models.*;
import com.luxuryproductsholding.api.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TestUsagePromocode {

    @Mock
    private PromocodeValidatorService validatorService;
    @Mock
    private PromocodeUsageLogRepository logRepository;
    @Mock
    private OrderService orderService;
    @Mock
    private PromocodeRepository promocodeRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private PromocodeService promocodeService;

    private Promocode promocode;
    private PromocodeIntermediaryRequest request;
    private Product product;
    private OrderItemDTO orderItemDTO;

    @BeforeEach
    void setUp() {
        promocode = new Promocode();
        promocode.setCode("PROMO10");
        promocode.setActive(true);
        promocode.setDiscountType(DiscountType.PERCENTAGE);
        promocode.setDiscountValue(BigDecimal.TEN);
        promocode.setMinimumOrderAmount(BigDecimal.valueOf(50));
        promocode.setMaxUsesPerEmail(2);
        promocode.setUsedCount(0);

        product = new Product();
        product.setId(1L);
        product.setName("TestProduct");

        orderItemDTO = new OrderItemDTO(1L, "TestProduct", 1,100);

        OrderItemDTO cartItemDTO = new OrderItemDTO();
        cartItemDTO.setProductId(1L);
        cartItemDTO.setProductName("TestProduct");
        cartItemDTO.setQuantity(1);
        cartItemDTO.setPrice(100);

        request = new PromocodeIntermediaryRequest();
        request.setCode("PROMO10");
        request.setEmail("test@example.com");
        request.setCartItems(List.of(cartItemDTO));
        request.setTotalPrice(BigDecimal.valueOf(100));
    }

    @Test
    @Tag("usage")
    void When_PromocodeIsValid_Expected_to_passWithSuccess() {
        // Arrange
        when(promocodeRepository.findByCode("PROMO10")).thenReturn(Optional.of(promocode));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(validatorService.validate(any(), any(), anyString())).thenReturn(true);
        when(logRepository.countByEmailAndPromocode("test@example.com", promocode)).thenReturn(0);
        when(validatorService.applyDiscount(any(), any(), anyString())).thenReturn(BigDecimal.valueOf(10));
        CustomUser user = new CustomUser();
        user.setEmail("test@example.com");
        when(userService.getAuthenticatedUser()).thenReturn(user);

        // Act
        PromocodeResponse response = promocodeService.validatePromocode(request);

        // Assert
        assertTrue(response.isValid());
        assertEquals(BigDecimal.valueOf(10), response.getDiscountValue());
        assertEquals("Promocode toegepast", response.getMessage());
        verify(logRepository).save(any(PromocodeUsageLog.class));
    }

    @Test
    @Tag("usage")
    void When_PromocodeIsInvalid_Expected_promocode_doesnt_exist_exception() {
        // Arrange
        when(promocodeRepository.findByCode("INVALID")).thenReturn(Optional.empty());

        // Act
        request.setCode("INVALID");

        // Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> promocodeService.validatePromocode(request));
        assertEquals("Promocode bestaat niet", ex.getMessage());
    }

    @Test
    @Tag("usage")
    void When_promocode_isUsedMaxForEmail_Expected_to_give_exception() {
        // Arrange
        when(promocodeRepository.findByCode("PROMO10")).thenReturn(Optional.of(promocode));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(validatorService.validate(any(), any(), anyString())).thenReturn(true);
        when(logRepository.countByEmailAndPromocode("test@example.com", promocode)).thenReturn(2);

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> promocodeService.validatePromocode(request));
        assertEquals("Deze promotiecode is al maximaal gebruikt voor uw email.", ex.getMessage());
    }

    @Test
    @Tag("usage")
    void When_Promocode_IsNotValidForOrder_expected_exception_NotValidForOrder() {
        // Arrange
        when(promocodeRepository.findByCode("PROMO10")).thenReturn(Optional.of(promocode));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(validatorService.validate(any(), any(), anyString())).thenReturn(false);

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> promocodeService.validatePromocode(request));
        assertEquals("Promocode is niet geldig voor deze bestelling", ex.getMessage());
    }

    @Test
    @Tag("usage")
    void When_Promocode_IsNotActive_Expected_Exception_NotValidForOrder() {
        // Arrange
        promocode.setActive(false);
        when(promocodeRepository.findByCode("PROMO10")).thenReturn(Optional.of(promocode));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> promocodeService.validatePromocode(request));
        assertEquals("Promocode is niet geldig voor deze bestelling", ex.getMessage());
    }

    @Test
    @Tag("usage")
    void When_Promocode_CantBeUsed_With_Product_Expected_Exception() {
        // Arrange
        when(promocodeRepository.findByCode("PROMO10")).thenReturn(Optional.of(promocode));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(validatorService.validate(any(), any(), anyString())).thenReturn(false);

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> promocodeService.validatePromocode(request));
        assertEquals("Promocode is niet geldig voor deze bestelling", ex.getMessage());
    }
}
