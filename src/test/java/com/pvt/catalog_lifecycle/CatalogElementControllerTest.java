package com.pvt.catalog_lifecycle;

import com.pvt.catalog_lifecycle.controllers.CatalogElementController;
import com.pvt.catalog_lifecycle.model.CatalogElement;
import com.pvt.catalog_lifecycle.model.LifecycleStatus;
import com.pvt.catalog_lifecycle.services.CatalogElementService;
import com.pvt.catalog_lifecycle.utilities.custom_exceptions.CatalogElementNotFoundException;
import com.pvt.catalog_lifecycle.utilities.custom_exceptions.InvalidStatusTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CatalogElementControllerTest {

    @InjectMocks
    private CatalogElementController catalogElementController;

    @Mock
    private CatalogElementService catalogElementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateStatus_Success() {
        // Arrange
        Long catalogElementId = 1L;
        LifecycleStatus newStatus = LifecycleStatus.ACTIVE;
        CatalogElement catalogElement = new CatalogElement(); // Create a dummy CatalogElement instance

        when(catalogElementService.getCatalogElementById(anyLong())).thenReturn(catalogElement);
        doNothing().when(catalogElementService).validateAndUpdateStatus(any(CatalogElement.class), any(LifecycleStatus.class));

        // Act
        catalogElementController.updateStatus(catalogElementId, newStatus);

        // Assert
        verify(catalogElementService, times(1)).getCatalogElementById(catalogElementId);
        verify(catalogElementService, times(1)).validateAndUpdateStatus(catalogElement, newStatus);
    }

    @Test
    void testUpdateStatus_CatalogElementNotFoundException() {
        // Arrange
        Long catalogElementId = 1L;
        LifecycleStatus newStatus = LifecycleStatus.ACTIVE;

        when(catalogElementService.getCatalogElementById(anyLong())).thenThrow(new CatalogElementNotFoundException(catalogElementId));

        // Act & Assert
        try {
            catalogElementController.updateStatus(catalogElementId, newStatus);
        } catch (CatalogElementNotFoundException e) {
            verify(catalogElementService, times(1)).getCatalogElementById(catalogElementId);
            verify(catalogElementService, times(0)).validateAndUpdateStatus(any(CatalogElement.class), any(LifecycleStatus.class));
        }
    }

    @Test
    void testUpdateStatus_InvalidStatusTransitionException() {
        // Arrange
        Long catalogElementId = 1L;
        LifecycleStatus newStatus = LifecycleStatus.ACTIVE;
        CatalogElement catalogElement = new CatalogElement(); // Create a dummy CatalogElement instance

        when(catalogElementService.getCatalogElementById(anyLong())).thenReturn(catalogElement);
        doThrow(new InvalidStatusTransitionException("Invalid status transition")).when(catalogElementService)
            .validateAndUpdateStatus(any(CatalogElement.class), any(LifecycleStatus.class));

        // Act & Assert
        try {
            catalogElementController.updateStatus(catalogElementId, newStatus);
        } catch (InvalidStatusTransitionException e) {
            verify(catalogElementService, times(1)).getCatalogElementById(catalogElementId);
            verify(catalogElementService, times(1)).validateAndUpdateStatus(catalogElement, newStatus);
        }
    }
}