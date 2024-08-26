package com.zapcom.shipmanagement;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zapcom.common.model.Cruiseline;
import com.zapcom.shipmanagement.controller.CruiselineController;
import com.zapcom.shipmanagement.service.CruiselineService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CruiseLineTest {
	

    @Mock
    private CruiselineService cruiselineService;

    @InjectMocks
    private CruiselineController cruiselineController;

    private Cruiseline cruiseline;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cruiseline = new Cruiseline(); // Initialize with necessary fields
    }

    @Test
    public void testCreateCruiseLine() {
        when(cruiselineService.saveCruiseLine(any(Cruiseline.class))).thenReturn(cruiseline);

        ResponseEntity<Cruiseline> response = cruiselineController.createCruiseLine(cruiseline);

        verify(cruiselineService, times(1)).saveCruiseLine(any(Cruiseline.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cruiseline, response.getBody());
    }

    @Test
    public void testGetAllCruiseLines() {
        Page<Cruiseline> page = new PageImpl<>(Arrays.asList(cruiseline));
        when(cruiselineService.getAllCruiseLines(0, 10)).thenReturn(page);

        ResponseEntity<List<Cruiseline>> response = cruiselineController.getAllCruiseLines(0, 10);

        verify(cruiselineService, times(1)).getAllCruiseLines(0, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page.getContent(), response.getBody());
    }

    @Test
    public void testGetCruiseLineById() {
        when(cruiselineService.getCruiseLineById(1)).thenReturn(Optional.of(cruiseline));

        ResponseEntity<Cruiseline> response = cruiselineController.getCruiseLineById(1);

        verify(cruiselineService, times(1)).getCruiseLineById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cruiseline, response.getBody());
    }

    @Test
    public void testUpdateCruiseLine() {
        when(cruiselineService.updateCruiseLine(eq(1), any(Cruiseline.class))).thenReturn(cruiseline);

        ResponseEntity<Cruiseline> response = cruiselineController.updateCruiseLine(1, cruiseline);

        verify(cruiselineService, times(1)).updateCruiseLine(eq(1), any(Cruiseline.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cruiseline, response.getBody());
    }

    @Test
    public void testDeleteCruiseLine() {
        doNothing().when(cruiselineService).deleteCruiseLine(1);

        ResponseEntity<String> response = cruiselineController.deleteCruiseLine(1);

        verify(cruiselineService, times(1)).deleteCruiseLine(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("cruiseline deleted succesfully", response.getBody());
    }
}


