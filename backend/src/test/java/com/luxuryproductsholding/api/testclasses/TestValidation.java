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
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
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

        // Assert
        assertEquals("PROMO123", validRequest.getCode());

    }

    @Test
    @Tag("Validation")
    void When_CodeIsDuplicateOfExistingCode_ThrowsException() {
        // Arrange
        when(promocodeRepository.existsByCode("PROMO123")).thenReturn(true);

        // Act
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                validatorService.validatePromocodeOnCreation(validRequest));

        // Assert
        assertEquals("Fout bij het aanmaken van de promocode: Promocode bestaat al.", ex.getMessage());
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
        assertEquals("Fout bij het aanmaken van de promocode: Minimale bestelwaarde moet groter zijn dan of gelijk aan 0.", ex.getMessage());
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

        assertEquals("Fout bij het aanmaken van de promocode: De opgegeven product 'TestProduct' bestaat niet.", ex.getMessage());
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
        assertEquals("Fout bij het aanmaken van de promocode: Kortingstype mag niet leeg zijn.", ex.getMessage());
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
        assertEquals("Fout bij het aanmaken van de promocode: Vervaldatum kan niet voor de aanmaakdatum liggen.", ex.getMessage());
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
        assertEquals("Fout bij het aanmaken van de promocode: Maximaal aantal keren dat de promocode per gebruiker gebruikt kan worden moet groter zijn dan 0.", ex.getMessage());
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
