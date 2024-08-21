package com.pvt.catalog_lifecycle.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import com.pvt.catalog_lifecycle.model.CatalogElement;
import com.pvt.catalog_lifecycle.model.LifecycleStatus;
import com.pvt.catalog_lifecycle.repository.CatalogElementRepository;

@Service
public class DataInitializerService {

    @Autowired
    private CatalogElementRepository catalogElementRepository;

    @PostConstruct
    @Transactional
    public void init() {
        // List of statuses to be inserted
        List<LifecycleStatus> statuses = List.of(
            LifecycleStatus.IN_STUDY,
            LifecycleStatus.IN_DESIGN,
            LifecycleStatus.IN_TEST,
            LifecycleStatus.ACTIVE,
            LifecycleStatus.LAUNCHED,
            LifecycleStatus.RETIRED,
            LifecycleStatus.OBSOLETE
        );

        // Insert each status into the database if not already present
        for (LifecycleStatus status : statuses) {
            if (catalogElementRepository.findByStatus(status) == null) {
                CatalogElement element = new CatalogElement(status);
                catalogElementRepository.save(element);
            }
        }
    }
}
