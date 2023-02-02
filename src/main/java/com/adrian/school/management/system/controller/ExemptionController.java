package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.ExemptionRequest;
import com.adrian.school.management.system.service.ExemptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exemptions")
@RequiredArgsConstructor
public class ExemptionController {

    private final ExemptionService exemptionService;

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addExemption(@Valid @RequestBody ExemptionRequest exemptionRequest) {
        exemptionService.createExemption(exemptionRequest);
    }
}
