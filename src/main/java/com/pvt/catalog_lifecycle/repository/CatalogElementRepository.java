package com.pvt.catalog_lifecycle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pvt.catalog_lifecycle.model.CatalogElement;

public interface CatalogElementRepository extends JpaRepository<CatalogElement, Long> {

}
