package com.pvt.catalog_lifecycle.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.pvt.catalog_lifecycle.model.CatalogElement;
import com.pvt.catalog_lifecycle.model.LifecycleStatus;
import com.pvt.catalog_lifecycle.services.CatalogElementService;

import com.pvt.catalog_lifecycle.utilities.custom_exceptions.CatalogElementNotFoundException;
import com.pvt.catalog_lifecycle.utilities.custom_exceptions.InvalidStatusTransitionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/catalog-element")
@RequiredArgsConstructor
@Slf4j
public class CatalogElementController {

    private final CatalogElementService catalogElementService;

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, @RequestBody LifecycleStatus newStatus) {
        try {
            CatalogElement catalogElement = catalogElementService.getCatalogElementById(id);
            catalogElementService.validateAndUpdateStatus(catalogElement, newStatus);
        } catch (CatalogElementNotFoundException e) {
            log.error("Catalog element not found: {}", e.getMessage());
            throw e;
        } catch (InvalidStatusTransitionException e) {
            log.error("Invalid status transition: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/status")
    public void addStatus(@RequestBody LifecycleStatus status) {
        try {
            catalogElementService.addStatus(status);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/status")
    public List<CatalogElement> getAllStatuses() {
        try {
            return catalogElementService.getAllStatuses();
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw e;
        }
    }
}
