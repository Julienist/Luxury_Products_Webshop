package com.luxuryproductsholding.api.testclasses;

import com.luxuryproductsholding.api.DAO.PromocodeRepository;
import com.luxuryproductsholding.api.models.DiscountType;
import com.luxuryproductsholding.api.models.Promocode;
import com.luxuryproductsholding.api.services.PromocodeService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TestPromocodeDeactivation {

    @Mock
    PromocodeRepository promocodeRepository;

    @InjectMocks
    PromocodeService promocodeService;

    @Test
    @Tag("deactivation")
    void Should_deactivate_promocode_When_setDisabled() {
        // Arrange
        String code = "TEST123";
        Promocode promocode = new Promocode(
                code,
                true,
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                DiscountType.PERCENTAGE,
                new BigDecimal("10.00"),
                new BigDecimal("50.00"),
                0,
                1,
                new HashSet<>(),
                new HashSet<>()
        );

        when(promocodeRepository.findByCode(code)).thenReturn(Optional.of(promocode));
        when(promocodeRepository.save(ArgumentMatchers.any(Promocode.class))).thenReturn(promocode);

        // Act
        promocodeService.disablePromocode(code);

        // Assert
        verify(promocodeRepository).findByCode(code);
        verify(promocodeRepository).save(promocode);
        assertFalse(promocode.isActive());
    }
}
