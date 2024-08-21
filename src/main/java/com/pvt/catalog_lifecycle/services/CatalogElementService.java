package com.pvt.catalog_lifecycle.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pvt.catalog_lifecycle.model.CatalogElement;
import com.pvt.catalog_lifecycle.model.LifecycleStatus;
import com.pvt.catalog_lifecycle.repository.CatalogElementRepository;

import com.pvt.catalog_lifecycle.utilities.custom_exceptions.CatalogElementNotFoundException;
import com.pvt.catalog_lifecycle.utilities.custom_exceptions.InvalidStatusTransitionException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatalogElementService {

    private final CatalogElementRepository repository;

    public CatalogElement getCatalogElementById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new CatalogElementNotFoundException(id));
    }

    public CatalogElement addStatus(LifecycleStatus status) {
        CatalogElement catalogElement = new CatalogElement(status);
        return repository.save(catalogElement);
    }

    public List<CatalogElement> getAllStatuses() {
        List<CatalogElement> catalogElements = repository.findAll();
        if (catalogElements.isEmpty()) {
            throw new IllegalStateException("No catalog elements found.");
        }
        return catalogElements;
    }

    public void validateAndUpdateStatus(CatalogElement catalogElement, LifecycleStatus newStatus) {
        try {
            validateTransition(catalogElement.getStatus(), newStatus);
            catalogElement.setStatus(newStatus);
            repository.save(catalogElement);
        } catch (InvalidStatusTransitionException e) {
            throw new InvalidStatusTransitionException("Failed to update status: " + e.getMessage());
        }
    }

    private void validateTransition(LifecycleStatus currentStatus, LifecycleStatus newStatus) {
        switch (currentStatus) {
            case IN_STUDY:
                if (newStatus != LifecycleStatus.IN_DESIGN) {
                    throw new InvalidStatusTransitionException("Invalid transition from IN_STUDY to " + newStatus);
                }
                break;
            case IN_DESIGN:
                if (newStatus != LifecycleStatus.IN_TEST) {
                    throw new InvalidStatusTransitionException("Invalid transition from IN_DESIGN to " + newStatus);
                }
                break;
            case IN_TEST:
                if (newStatus != LifecycleStatus.ACTIVE && newStatus != LifecycleStatus.REJECTED) {
                    throw new InvalidStatusTransitionException("Invalid transition from IN_TEST to " + newStatus);
                }
                break;
            case ACTIVE:
                if (newStatus != LifecycleStatus.LAUNCHED && newStatus != LifecycleStatus.RETIRED) {
                    throw new InvalidStatusTransitionException("Invalid transition from ACTIVE to " + newStatus);
                }
                break;
            case LAUNCHED:
                if (newStatus != LifecycleStatus.RETIRED) {
                    throw new InvalidStatusTransitionException("Invalid transition from LAUNCHED to " + newStatus);
                }
                break;
            case RETIRED:
                if (newStatus != LifecycleStatus.OBSOLETE) {
                    throw new InvalidStatusTransitionException("Invalid transition from RETIRED to " + newStatus);
                }
                break;
            case REJECTED:
            case OBSOLETE:
                throw new InvalidStatusTransitionException("Cannot transition from " + currentStatus);
            default:
                throw new InvalidStatusTransitionException("Unknown current status: " + currentStatus);
        }
    }
}
