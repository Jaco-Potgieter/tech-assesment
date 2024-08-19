package com.pvt.catalog_lifecycle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pvt.catalog_lifecycle.model.CatalogElement;
import com.pvt.catalog_lifecycle.model.LifecycleStatus;

public interface CatalogElementRepository extends JpaRepository<CatalogElement, Long> {

    CatalogElement findByStatus(LifecycleStatus status);
}
