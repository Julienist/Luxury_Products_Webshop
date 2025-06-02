package com.luxuryproductsholding.api.testclasses;

import com.luxuryproductsholding.api.DAO.CategoryRepository;
import com.luxuryproductsholding.api.DAO.ProductRepository;
import com.luxuryproductsholding.api.DAO.PromocodeRepository;
import com.luxuryproductsholding.api.DTO.PromocodeRequest;
import com.luxuryproductsholding.api.services.PromocodeValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestValidation {

    @Mock
    PromocodeRepository promocodeRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    PromocodeValidatorService validatorService;

    PromocodeRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new PromocodeRequest();
        validRequest.setCode("PROMO123");
        validRequest.setScopeType("PRODUCT");
        validRequest.setScopeValue("TestProduct");
        validRequest.setDiscountType("PERCENTAGE");
        validRequest.setDiscountValue(BigDecimal.valueOf(10));
        validRequest.setMinOrderAmount(BigDecimal.valueOf(50));
        validRequest.setMaxUsesPerEmail(1);
        validRequest.setUsedCount(0);
        validRequest.setCreationDate(LocalDateTime.now());
        validRequest.setExpiryDate(LocalDateTime.now().plusDays(10));
    }

    @Test
    @Tag("Validation")
    void When_CodeIsNotTheSame_ButProductIs_Validate_Code() {
        //Arrange
        when(promocodeRepository.existsByCode("PROMO123")).thenReturn(false);
        when(productRepository.existsByName("TestProduct")).thenReturn(true);

        //Act & Assert
        assertDoesNotThrow(() -> validatorService.validatePromocodeOnCreation(validRequest));
        verify(promocodeRepository, times(1)).existsByCode("PROMO123");
        verify(productRepository, times(1)).existsByName("TestProduct");
        System.out.println("Code and product validation passed successfully.");
    }

    @Test
    @Tag("Validation")
    void When_CodeIsDuplicateOfExistingCode_ThrowsException() {
        // Arrange
        when(promocodeRepository.existsByCode("PROMO123")).thenReturn(true);

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                validatorService.validatePromocodeOnCreation(validRequest));
        assertTrue(ex.getMessage().contains("bestaat al"));
        System.out.println(ex.getMessage());
    }

    @Test
    @Tag("Validation")
    void When_PromocodeMinimumOrderAmountIsLessThanZero_ThrowsException() {
        // Arrange
        validRequest.setMinOrderAmount(BigDecimal.valueOf(-10));
        when(promocodeRepository.existsByCode("PROMO123")).thenReturn(false);
        when(productRepository.existsByName("TestProduct")).thenReturn(true);

        // Act
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                validatorService.validatePromocodeOnCreation(validRequest));
        // Assert
        assertTrue(ex.getMessage().contains("Minimale bestelwaarde moet groter zijn dan of gelijk aan 0."));
        System.out.println(ex.getMessage());
    }

    @Test
    @Tag("Validation")
    void When_ProductDoesntExist_ButPromocodeDoes_ValidationWillReturnErrorMessage() {
        // Arrange
        when(promocodeRepository.existsByCode("PROMO123")).thenReturn(false);
        when(productRepository.existsByName("TestProduct")).thenReturn(false);

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                validatorService.validatePromocodeOnCreation(validRequest));
        assertTrue(ex.getMessage().contains("bestaat niet"));
        System.out.println(ex.getMessage());
    }

    @Test
    @Tag("Validation")
    void When_DiscountTypeIsNull_ThrowsException() {
        // Arrange
        validRequest.setDiscountType(null);
        when(promocodeRepository.existsByCode("PROMO123")).thenReturn(false);
        when(productRepository.existsByName("TestProduct")).thenReturn(true);

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                validatorService.validatePromocodeOnCreation(validRequest));
        assertTrue(ex.getMessage().contains("Kortingstype mag niet leeg zijn."));
        System.out.println(ex.getMessage());
    }

    @Test
    @Tag("Validation")
    void When_ExpiryDateIsBeforeCreationDate_ThrowsException() {
        // Arrange
        validRequest.setExpiryDate(LocalDateTime.now().minusDays(1));
        when(promocodeRepository.existsByCode("PROMO123")).thenReturn(false);
        when(productRepository.existsByName("TestProduct")).thenReturn(true);

        // Act

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                validatorService.validatePromocodeOnCreation(validRequest));
        // Assert
        assertTrue(ex.getMessage().contains("Vervaldatum kan niet voor de aanmaakdatum liggen."));
        System.out.println(ex.getMessage());
    }

    @Test
    @Tag("Validation")
    void When_MaxUsesPerEmailIsZeroOrNegative_ThrowsException() {
        // Arrange
        validRequest.setMaxUsesPerEmail(0);
        when(promocodeRepository.existsByCode("PROMO123")).thenReturn(false);
        when(productRepository.existsByName("TestProduct")).thenReturn(true);

        // Act
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                validatorService.validatePromocodeOnCreation(validRequest));

        // Assert
        assertTrue(ex.getMessage().contains("Maximaal aantal keren dat de promocode per gebruiker gebruikt kan worden moet groter zijn dan 0."));
        System.out.println(ex.getMessage());
    }

    @Test
    @Tag("Validation")
    void PromocodeWithValidData_ShouldPassAllValidations() {
        // Arrange
        when(promocodeRepository.existsByCode("PROMO123")).thenReturn(false);
        when(productRepository.existsByName("TestProduct")).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> validatorService.validatePromocodeOnCreation(validRequest));
        verify(promocodeRepository, times(1)).existsByCode("PROMO123");
        verify(productRepository, times(1)).existsByName("TestProduct");
        System.out.println("Promocode with valid data passed all validations successfully.");
    }

    @Test
    @Tag("Validation")
    void When_DiscountValueIsZeroOrNegative_ThrowsException() {
        // Arrange
        validRequest.setDiscountValue(BigDecimal.ZERO);
        when(promocodeRepository.existsByCode("PROMO123")).thenReturn(false);
        when(productRepository.existsByName("TestProduct")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> validatorService.validatePromocodeOnCreation(validRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Korting moet groter zijn dan 0.");
    }
}
