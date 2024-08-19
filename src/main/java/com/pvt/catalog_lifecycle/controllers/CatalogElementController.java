package com.pvt.catalog_lifecycle.controllers;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pvt.catalog_lifecycle.model.CatalogElement;
import com.pvt.catalog_lifecycle.model.LifecycleStatus;
import com.pvt.catalog_lifecycle.services.CatalogElementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/catalog-element")
@RequiredArgsConstructor
public class CatalogElementController {

    private final CatalogElementService catalogElementService;

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, @RequestBody LifecycleStatus newStatus) {
        CatalogElement catalogElement = catalogElementService.getCatalogElementById(id);
        catalogElementService.validateAndUpdateStatus(catalogElement, newStatus);
    }
}
