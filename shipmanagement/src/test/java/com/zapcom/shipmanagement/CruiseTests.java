package com.zapcom.shipmanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zapcom.common.model.Cruise;
import com.zapcom.shipmanagement.controller.CruiseController;
import com.zapcom.shipmanagement.service.CruiseService;

@SpringBootTest
public class CruiseTests {

	@Mock
	private CruiseService cruiseService;

	@InjectMocks
	private CruiseController cruiseController;

	private Cruise cruise;

	@BeforeEach
	public void setUp() {

		cruise = new Cruise(); // Initialize with necessary fields
		cruise.setCruiseName("cruise");
	}

	@Test
	public void testCreateCruise() {
		when(cruiseService.createCruise(cruise, 1)).thenReturn(cruise);

		ResponseEntity<Cruise> response = cruiseController.createCruise(cruise, 1);

		verify(cruiseService, times(1)).createCruise(cruise, 1);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(cruise, response.getBody());
	}

	@Test
	public void testGetAllCruises() {
		Page<Cruise> page = new PageImpl<>(Arrays.asList(cruise));
		when(cruiseService.getAllCruises(0, 10)).thenReturn(page);

		ResponseEntity<List<Cruise>> response = cruiseController.getAllCruises(0, 10);

		verify(cruiseService, times(1)).getAllCruises(0, 10);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(page.getContent(), response.getBody());
	}

	@Test
	public void testGetCruiseById() {
		when(cruiseService.getCruiseById(1)).thenReturn(cruise);

		ResponseEntity<Cruise> response = cruiseController.getCruiseById(1);

		verify(cruiseService, times(1)).getCruiseById(1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(cruise, response.getBody());
	}

	@Test
	public void testGetCruiseBetweenDates() throws Exception {

		when(cruiseService.getCruiseBetweenDates(any(Date.class), any(Date.class))).thenReturn(Arrays.asList(cruise));

		ResponseEntity<List<Cruise>> response = cruiseController.getCruiseBetweenDates("2024-01-01", "2024-01-31");

		verify(cruiseService, times(1)).getCruiseBetweenDates(any(Date.class), any(Date.class));
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Arrays.asList(cruise), response.getBody());
	}

	@Test
	public void testUpdateCruise() {
		when(cruiseService.updateCruise(eq(1), any(Cruise.class))).thenReturn(cruise);

		ResponseEntity<Cruise> response = cruiseController.updateCruise(1, cruise);

		verify(cruiseService, times(1)).updateCruise(1, cruise);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(cruise, response.getBody());
	}

//	@Test
//	public void testDeleteCruise() {
//		when(cruiseService.deleteCruise(1)).thenReturn(true);
//
//		ResponseEntity<String> response = cruiseController.deleteCruise(1);
//
//		verify(cruiseService, times(1)).deleteCruise(1);
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals("cruise deleted succesfully", response.getBody());
//	}

}
