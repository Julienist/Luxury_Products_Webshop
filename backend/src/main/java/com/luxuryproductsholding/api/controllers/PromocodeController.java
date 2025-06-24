package com.luxuryproductsholding.api.controllers;

import com.luxuryproductsholding.api.DTO.*;
import com.luxuryproductsholding.api.models.Promocode;
import com.luxuryproductsholding.api.models.PromocodeUsageLog;
import com.luxuryproductsholding.api.services.PromocodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/promocodes")
public class PromocodeController {

    private final PromocodeService promocodeService;

    @Autowired
    public PromocodeController(PromocodeService promocodeService) {
        this.promocodeService = promocodeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('Make_and_deactivate_promocodes') or hasRole('SuperAdmin')")
    public ResponseEntity<String> createPromocode(@RequestBody PromocodeRequest promocodeRequest) {
        try {
            promocodeService.createPromocodeAfterValidation(promocodeRequest);
            return ResponseEntity.ok("Promocode is valid and created.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validateAndApply")
    public ResponseEntity<PromocodeResponse> validatePromocode(@RequestBody PromocodeIntermediaryRequest dto) {
        try {
            PromocodeResponse response = promocodeService.validatePromocode(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.ok(new PromocodeResponse(BigDecimal.ZERO, false, e.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('Make_and_deactivate_promocodes') or hasRole('SuperAdmin')")
    public ResponseEntity<List<String>> getPromocodes() {
        try {
            List<String> promocodes = promocodeService.getAllPromocodeCodes();
            return ResponseEntity.ok(promocodes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonList("Error retrieving promocodes: " + e.getMessage()));
        }
    }

    @PutMapping("/disable_promocode_{code}")
    @PreAuthorize("hasRole('Make_and_deactivate_promocodes') or hasRole('SuperAdmin')")
    public ResponseEntity<String> disablePromocode(@PathVariable String code) {
        try {
            promocodeService.disablePromocode(code);
            return ResponseEntity.ok("Promocode disabled successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SuperAdmin') or hasRole('All_insights')")
    public ResponseEntity<?> getAllExistingPromocodeData() {
        try {
            List<Promocode> promocodes = promocodeService.getAllPromocodeData();
            return ResponseEntity.ok(promocodes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/allusage")
    @PreAuthorize("hasRole('SuperAdmin') or hasRole('All_insights') or hasRole('Insight_promocode_usage')")
    public ResponseEntity<?> getAllPromocodeUsageLogging() {
        try {
            List<PromocodeUsageLog> promocodeLogs = promocodeService.getAllPromocodeUsageLogging();
            return ResponseEntity.ok(promocodeLogs);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
