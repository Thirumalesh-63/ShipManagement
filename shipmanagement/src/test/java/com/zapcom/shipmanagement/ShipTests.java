package com.zapcom.shipmanagement;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zapcom.common.model.Ship;
import com.zapcom.shipmanagement.controller.ShipController;
import com.zapcom.shipmanagement.service.ShipService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@SpringBootTest
public class ShipTests {

	

    @Mock
    private ShipService shipService;

    @InjectMocks
    private ShipController shipController;

    private Ship ship;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ship = new Ship(); // Initialize with necessary fields
    }

    @Test
    public void testCreateShip() {
        when(shipService.createShip(any(Ship.class))).thenReturn(ship);

        ResponseEntity<Ship> response = shipController.createShip(ship);

        verify(shipService, times(1)).createShip(any(Ship.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ship, response.getBody());
    }

    @Test
    public void testGetAllShips() {
        Page<Ship> page = new PageImpl<>(Arrays.asList(ship));
        when(shipService.getAllShips(0, 10)).thenReturn(page);

        ResponseEntity<List<Ship>> response = shipController.getAllShips(0, 10);

        verify(shipService, times(1)).getAllShips(0, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page.getContent(), response.getBody());
    }

    @Test
    public void testGetShipById() {
        when(shipService.getShipById(1)).thenReturn(Optional.of(ship));

        ResponseEntity<Ship> response = shipController.getShipById(1);

        verify(shipService, times(1)).getShipById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ship, response.getBody());
    }

    @Test
    public void testUpdateShip() {
        when(shipService.updateShip(eq(1), any(Ship.class))).thenReturn(ship);

        ResponseEntity<Ship> response = shipController.updateShip(1, ship);

        verify(shipService, times(1)).updateShip(eq(1), any(Ship.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ship, response.getBody());
    }

    @Test
    public void testDeleteShip() {
    	 when(shipService.deleteShip(1)).thenReturn(true);

        ResponseEntity<String> response = shipController.deleteShip(1);

        verify(shipService, times(1)).deleteShip(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ship deleted succesfully", response.getBody());
    }

}
