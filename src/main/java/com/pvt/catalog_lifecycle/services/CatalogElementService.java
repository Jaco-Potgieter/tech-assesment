package com.pvt.catalog_lifecycle.services;

import org.springframework.stereotype.Service;

import com.pvt.catalog_lifecycle.model.CatalogElement;
import com.pvt.catalog_lifecycle.model.LifecycleStatus;
import com.pvt.catalog_lifecycle.repository.CatalogElementRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatalogElementService {

    private final CatalogElementRepository repository;

    public CatalogElement getCatalogElementById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Catalog element not found"));
    }

    public void validateAndUpdateStatus(CatalogElement catalogElement, LifecycleStatus newStatus) {
        validateTransition(catalogElement.getStatus(), newStatus);
        catalogElement.setStatus(newStatus);
        repository.save(catalogElement);
    }

    private void validateTransition(LifecycleStatus currentStatus, LifecycleStatus newStatus) {
        switch (currentStatus) {
            case IN_STUDY:
                if (newStatus != LifecycleStatus.IN_DESIGN) {
                    throw new IllegalArgumentException("Invalid transition from IN_STUDY to " + newStatus);
                }
                break;
            case IN_DESIGN:
                if (newStatus != LifecycleStatus.IN_TEST) {
                    throw new IllegalArgumentException("Invalid transition from IN_DESIGN to " + newStatus);
                }
                break;
            case IN_TEST:
                if (newStatus != LifecycleStatus.ACTIVE && newStatus != LifecycleStatus.REJECTED) {
                    throw new IllegalArgumentException("Invalid transition from IN_TEST to " + newStatus);
                }
                break;
            case ACTIVE:
                if (newStatus != LifecycleStatus.LAUNCHED && newStatus != LifecycleStatus.RETIRED) {
                    throw new IllegalArgumentException("Invalid transition from ACTIVE to " + newStatus);
                }
                break;
            case LAUNCHED:
                if (newStatus != LifecycleStatus.RETIRED) {
                    throw new IllegalArgumentException("Invalid transition from LAUNCHED to " + newStatus);
                }
                break;
            case RETIRED:
                if (newStatus != LifecycleStatus.OBSOLETE) {
                    throw new IllegalArgumentException("Invalid transition from RETIRED to " + newStatus);
                }
                break;
            case REJECTED:
            case OBSOLETE:
                throw new IllegalArgumentException("Cannot transition from " + currentStatus);
            default:
                throw new IllegalArgumentException("Unknown current status: " + currentStatus);
        }
    }
}
