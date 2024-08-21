package com.pvt.catalog_lifecycle.utilities.custom_exceptions;

public class CatalogElementNotFoundException extends RuntimeException {
    public CatalogElementNotFoundException(Long id) {
        super("Catalog element with ID " + id + " not found");
    }
}

