package com.pvt.catalog_lifecycle;

import com.pvt.catalog_lifecycle.model.CatalogElement;
import com.pvt.catalog_lifecycle.model.LifecycleStatus;
import com.pvt.catalog_lifecycle.repository.CatalogElementRepository;
import com.pvt.catalog_lifecycle.services.CatalogElementService;
import com.pvt.catalog_lifecycle.utilities.custom_exceptions.CatalogElementNotFoundException;
import com.pvt.catalog_lifecycle.utilities.custom_exceptions.InvalidStatusTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CatalogElementServiceTest {

    @Mock
    private CatalogElementRepository repository;

    @InjectMocks
    private CatalogElementService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCatalogElementWhenFound() {
        CatalogElement element = new CatalogElement(1L, LifecycleStatus.IN_STUDY);
        when(repository.findById(1L)).thenReturn(Optional.of(element));

        CatalogElement result = service.getCatalogElementById(1L);

        assertEquals(element, result);
    }

    @Test
    void shouldThrowCatalogElementNotFoundExceptionWhenCatalogElementNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CatalogElementNotFoundException.class, () -> service.getCatalogElementById(1L));
    }

    @Test
    void shouldUpdateStatusWhenValidTransition() {
        CatalogElement element = new CatalogElement(1L, LifecycleStatus.IN_STUDY);
        when(repository.findById(1L)).thenReturn(Optional.of(element));

        service.validateAndUpdateStatus(element, LifecycleStatus.IN_DESIGN);

        verify(repository).save(element);
        assertEquals(LifecycleStatus.IN_DESIGN, element.getStatus());
    }

    @Test
    void shouldTransitionFromInDesignToInTest() {
        CatalogElement element = new CatalogElement(1L, LifecycleStatus.IN_DESIGN);
        when(repository.findById(1L)).thenReturn(Optional.of(element));

        service.validateAndUpdateStatus(element, LifecycleStatus.IN_TEST);

        verify(repository).save(element);
        assertEquals(LifecycleStatus.IN_TEST, element.getStatus());
    }

    @Test
    void shouldTransitionFromInTestToActive() {
        CatalogElement element = new CatalogElement(1L, LifecycleStatus.IN_TEST);
        when(repository.findById(1L)).thenReturn(Optional.of(element));

        service.validateAndUpdateStatus(element, LifecycleStatus.ACTIVE);

        verify(repository).save(element);
        assertEquals(LifecycleStatus.ACTIVE, element.getStatus());
    }

    @Test
    void shouldTransitionFromInTestToRejected() {
        CatalogElement element = new CatalogElement(1L, LifecycleStatus.IN_TEST);
        when(repository.findById(1L)).thenReturn(Optional.of(element));

        service.validateAndUpdateStatus(element, LifecycleStatus.REJECTED);

        verify(repository).save(element);
        assertEquals(LifecycleStatus.REJECTED, element.getStatus());
    }

    @Test
    void shouldTransitionFromActiveToLaunched() {
        CatalogElement element = new CatalogElement(1L, LifecycleStatus.ACTIVE);
        when(repository.findById(1L)).thenReturn(Optional.of(element));

        service.validateAndUpdateStatus(element, LifecycleStatus.LAUNCHED);

        verify(repository).save(element);
        assertEquals(LifecycleStatus.LAUNCHED, element.getStatus());
    }

    @Test
    void shouldTransitionFromLaunchedToRetired() {
        CatalogElement element = new CatalogElement(1L, LifecycleStatus.LAUNCHED);
        when(repository.findById(1L)).thenReturn(Optional.of(element));

        service.validateAndUpdateStatus(element, LifecycleStatus.RETIRED);

        verify(repository).save(element);
        assertEquals(LifecycleStatus.RETIRED, element.getStatus());
    }

    @Test
    void shouldTransitionFromRetiredToObsolete() {
        CatalogElement element = new CatalogElement(1L, LifecycleStatus.RETIRED);
        when(repository.findById(1L)).thenReturn(Optional.of(element));

        service.validateAndUpdateStatus(element, LifecycleStatus.OBSOLETE);

        verify(repository).save(element);
        assertEquals(LifecycleStatus.OBSOLETE, element.getStatus());
    }

    @Test
    void shouldThrowInvalidStatusTransitionExceptionForInvalidTransitionFromInStudyToActive() {
        CatalogElement element = new CatalogElement(1L, LifecycleStatus.IN_STUDY);
        when(repository.findById(1L)).thenReturn(Optional.of(element));

        assertThrows(InvalidStatusTransitionException.class, () -> service.validateAndUpdateStatus(element, LifecycleStatus.ACTIVE));
        verify(repository, never()).save(element);
    }

    @Test
    void shouldThrowInvalidStatusTransitionExceptionWhenTransitionFromRejected() {
        CatalogElement element = new CatalogElement(1L, LifecycleStatus.REJECTED);
        when(repository.findById(1L)).thenReturn(Optional.of(element));

        assertThrows(InvalidStatusTransitionException.class, () -> service.validateAndUpdateStatus(element, LifecycleStatus.IN_STUDY));
        verify(repository, never()).save(element);
    }

    @Test
    void shouldThrowInvalidStatusTransitionExceptionWhenTransitionFromObsolete() {
        CatalogElement element = new CatalogElement(1L, LifecycleStatus.OBSOLETE);
        when(repository.findById(1L)).thenReturn(Optional.of(element));

        assertThrows(InvalidStatusTransitionException.class, () -> service.validateAndUpdateStatus(element, LifecycleStatus.ACTIVE));
        verify(repository, never()).save(element);
    }
}
