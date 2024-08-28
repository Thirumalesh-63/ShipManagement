package com.zapcom.shipmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zapcom.common.model.Ship;
import com.zapcom.shipmanagement.service.ShipService;

@RestController
@RequestMapping("shipmanagement")
public class ShipController {

	@Autowired
	private ShipService shipService;

	// Create a new Ship
	@PostMapping("/admin/ship/{id}")
	public ResponseEntity<Ship> createShip(@RequestBody Ship ship,@PathVariable int id) {
		Ship createdShip = shipService.createShip(ship,id);
		return new ResponseEntity<>(createdShip,HttpStatus.CREATED);
	}

	// Retrieve all Ships
	@GetMapping("/admin/ship")
	public ResponseEntity<List<Ship>> getAllShips(
			@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
		Page<Ship> ships = shipService.getAllShips(page,size);
		System.err.println(size);
		return ResponseEntity.ok(ships.getContent());
	}

	// Retrieve a specific Ship by ID
	@GetMapping("/admin/ship/{id}")
	public ResponseEntity<Ship> getShipById(@PathVariable int id) {
		Optional<Ship> ship = shipService.getShipById(id);
		return new ResponseEntity<Ship>(ship.get(),HttpStatus.OK);
	}

	// Update an existing Ship
	@PutMapping("/admin/ship/{id}")
	public ResponseEntity<Ship> updateShip(@PathVariable int id, @RequestBody Ship shipDetails) {
		Ship updatedShip = shipService.updateShip(id, shipDetails);
		return ResponseEntity.ok(updatedShip);

	}

	// Delete a specific Ship by ID
	@DeleteMapping("/admin/ship/{id}")
	public ResponseEntity<String> deleteShip(@PathVariable int id) {

		shipService.deleteShip(id);

		return new ResponseEntity<>("ship deleted succesfully",HttpStatus.OK);


	}


}
