package com.luxuryproductsholding.api.controllers;

import com.luxuryproductsholding.api.DTO.PromocodeIntermediaryRequest;
import com.luxuryproductsholding.api.DTO.PromocodeRequest;
import com.luxuryproductsholding.api.DTO.PromocodeResponse;
import com.luxuryproductsholding.api.services.PromocodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/promocodes")
public class PromocodeController {

    private final PromocodeService promocodeService;

    @Autowired
    public PromocodeController(PromocodeService promocodeService) {
        this.promocodeService = promocodeService;
    }

    @PostMapping
    public ResponseEntity<String> createPromocode(@RequestBody PromocodeRequest promocodeRequest) {
        try {
            promocodeService.createPromocodeAfterValidation(promocodeRequest);
            return ResponseEntity.ok("Promocode is valid and created.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validateAndApply")
    public ResponseEntity<PromocodeResponse> validatePromocode(@RequestBody PromocodeIntermediaryRequest dto) {
        try {
            PromocodeResponse response = promocodeService.validatePromocode(dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new PromocodeResponse(BigDecimal.ZERO, false, e.getMessage()));
        }
    }

}
