package com.zapcom.shipmanagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zapcom.common.model.Cruiseline;
import com.zapcom.shipmanagement.service.CruiselineService;

@RestController
@RequestMapping("shipmanagement")

public class CruiselineController {

	@Autowired
	private CruiselineService cruiselineservice;

	// Create a new CruiseLine
	@PostMapping("/admin/cruiseline")
	public ResponseEntity<Cruiseline> createCruiseLine(@RequestBody Cruiseline cruiseline) {
		Cruiseline savedCruiseLine = cruiselineservice.saveCruiseLine(cruiseline);
		return new ResponseEntity<>(savedCruiseLine, HttpStatus.CREATED);
	}

	// Retrieve all CruiseLines
	@GetMapping("/admin/cruiseline")
	public ResponseEntity<List<Cruiseline>> getAllCruiseLines(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<Cruiseline> cruiselines = cruiselineservice.getAllCruiseLines(page, size);

		return ResponseEntity.ok(cruiselines.getContent());
	}

	// Retrieve a specific CruiseLine by ID
	@GetMapping("/admin/cruiseline/{id}")
	public ResponseEntity<Cruiseline> getCruiseLineById(@PathVariable int id) {
		Optional<Cruiseline> cruiseline = cruiselineservice.getCruiseLineById(id);
		return new ResponseEntity<Cruiseline>(cruiseline.get(), HttpStatus.OK);
	}
	
	// Retrieve a specific CruiseLine by ID
	@GetMapping("/admin/cruiselinebyname/{name}")
	public ResponseEntity<Cruiseline> getCruiseLineByName(@PathVariable String name) {
		Cruiseline cruiseline = cruiselineservice.getCruiseLineByName(name);
		return new ResponseEntity<Cruiseline>(cruiseline, HttpStatus.OK);
	}

	// Update an existing CruiseLine
	@PutMapping("/admin/cruiseline/{id}")
	public ResponseEntity<Cruiseline> updateCruiseLine(@PathVariable int id,
			@RequestBody Cruiseline cruiseLineDetails) {
		Cruiseline updatedCruiseLine = cruiselineservice.updateCruiseLine(id, cruiseLineDetails);
		return new ResponseEntity<Cruiseline>(updatedCruiseLine, HttpStatus.OK);
	}

	// Delete a specific CruiseLine by ID
	@DeleteMapping("/admin/cruiseline/{id}")
	public ResponseEntity<Map<String, String>> deleteCruiseLine(@PathVariable int id) {

		cruiselineservice.deleteCruiseLine(id);
		Map<String, String> response = new HashMap<>();
		  response.put("message", "cruiseline deleted successfully");
		    return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
