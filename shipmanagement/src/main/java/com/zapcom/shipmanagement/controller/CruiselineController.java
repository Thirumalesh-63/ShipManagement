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

import com.zapcom.common.model.Cruiseline;
import com.zapcom.shipmanagement.service.CruiselineService;

@RestController
@RequestMapping("shipmanagement")
public class CruiselineController {

	@Autowired
    private CruiselineService cruiselineservice;
    // Create a new CruiseLine
    @PostMapping("/cruiseline")
    public ResponseEntity<Cruiseline> createCruiseLine(@RequestBody Cruiseline cruiseLine) {
    	Cruiseline savedCruiseLine = cruiselineservice.saveCruiseLine(cruiseLine);
        return new ResponseEntity<>(savedCruiseLine,HttpStatus.CREATED);
    }

    // Retrieve all CruiseLines
    @GetMapping("/cruiseline")
    public ResponseEntity<List<Cruiseline>> getAllCruiseLines(
    		@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {

        Page<Cruiseline> cruiseLines = cruiselineservice.getAllCruiseLines(page,size);
        
        return ResponseEntity.ok(cruiseLines.getContent());
    }

    // Retrieve a specific CruiseLine by ID
    @GetMapping("/cruiseline/{id}")
    public ResponseEntity<Cruiseline> getCruiseLineById(@PathVariable int id) {
        Optional<Cruiseline> cruiseLine = cruiselineservice.getCruiseLineById(id);
        return new ResponseEntity<Cruiseline>(cruiseLine.get(),HttpStatus.OK);
    }

    // Update an existing CruiseLine
    @PutMapping("/cruiseline/{id}")
    public ResponseEntity<Cruiseline> updateCruiseLine(@PathVariable int id, @RequestBody Cruiseline cruiseLineDetails) {
    	Cruiseline updatedCruiseLine = cruiselineservice.updateCruiseLine(id, cruiseLineDetails);
       return new ResponseEntity<Cruiseline>(updatedCruiseLine,HttpStatus.OK);
    }

    // Delete a specific CruiseLine by ID
    @DeleteMapping("/cruiseline/{id}")
    public ResponseEntity<String> deleteCruiseLine(@PathVariable int id) {
    	
    	 cruiselineservice.deleteCruiseLine(id);;
 	     return new ResponseEntity<>("cruiseline deleted succesfully",HttpStatus.OK);
 	    
       
    }
}
