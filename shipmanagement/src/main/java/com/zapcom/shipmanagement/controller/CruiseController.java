package com.zapcom.shipmanagement.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.zapcom.common.model.Cruise;
import com.zapcom.shipmanagement.service.CruiseService;

@RestController
@RequestMapping("shipmanagement")
public class CruiseController {
	
	
	@Autowired
	
	private CruiseService cruiseService;

    // Create a new Cruise
    @PostMapping("/cruise/{sid}")
    public ResponseEntity<Cruise> createCruise(@RequestBody Cruise cruise, @PathVariable int sid) {
    	Cruise createdCruise = cruiseService.createCruise(cruise,sid);
        return new ResponseEntity<>(createdCruise,HttpStatus.CREATED);
    }
    
    // Retrieve all Cruises
    @GetMapping("/cruises")
    public ResponseEntity<List<Cruise>> getAllCruises(	
    		@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        Page<Cruise> cruises = cruiseService.getAllCruises(page,size);
        return ResponseEntity.ok(cruises.getContent());
    }
    
    @GetMapping("/cruisesBystartdestination/{startdestination}")
    public ResponseEntity<List<Cruise>> getAllCruisesByStartdestination(@PathVariable String startdestination,
    		@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        Page<Cruise> cruises = cruiseService.getAllCruisesByStartdestination(startdestination,page,size);
        return ResponseEntity.ok(cruises.getContent());
    }
    
    @GetMapping("/cruise/{id}")
    public ResponseEntity<Cruise> getCruiseById(@PathVariable int id){
    		
        Cruise cruise = cruiseService.getCruiseById(id);
        return ResponseEntity.ok(cruise);
    }


    @GetMapping("/cruisesByenddestination/{enddestination}")
    public ResponseEntity<List<Cruise>> getAllCruisesByenddestination(@PathVariable String enddestination,
    		@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        Page<Cruise> cruises = cruiseService.getAllCruisesBYenddestination(enddestination,page,size);
        return ResponseEntity.ok(cruises.getContent());
    }
    @GetMapping("/cruisesBycruiseline/{cruiseline}")
    public ResponseEntity<List<Cruise>> getAllCruisesBycruiseline(@PathVariable String cruiseline,
    		@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        Page<Cruise> cruises = cruiseService.getAllCruisesBYcruiseline(cruiseline,page,size);
        return ResponseEntity.ok(cruises.getContent());
    }
    @GetMapping("/cruisesByship/{shipname}")
    public ResponseEntity<Cruise> getCruiseByship(@PathVariable String shipname ) {
        Cruise cruises = cruiseService.getAllByship(shipname);
        return ResponseEntity.ok(cruises);
    }
    
    @GetMapping("/cruises/itinerary/{ship}")
    public ResponseEntity<List<String>> getshipitinerary(@PathVariable String ship) {
        List<String> cruises = cruiseService.getshipitinerary(ship);
        return ResponseEntity.ok(cruises);
    }

    
    @GetMapping("/cruises/BybetweenDates")
    public ResponseEntity<List<Cruise>> getCruiseBetweenDates(
    		  @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") String startDateStr,
              @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") String endDateStr) throws ParseException {

          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

          // Parse the input strings
          Date startDate = sdf.parse(startDateStr);
          Date endDate = sdf.parse(endDateStr);

          // Set endDate to cover the entire day
          endDate = new Date(endDate.getTime() + (24 * 60 * 60 * 1000) - 1);

          List<Cruise> cruises = cruiseService.getCruiseBetweenDates(startDate, endDate);
          return ResponseEntity.ok(cruises);
    }


    // Update an existing Cruise
    @PutMapping("/cruise/{id}")
    public ResponseEntity<Cruise> updateCruise(@PathVariable int id, @RequestBody Cruise cruiseDetails) {
    	Cruise updatedCruise = cruiseService.updateCruise(id, cruiseDetails);
        if (updatedCruise != null) {
            return ResponseEntity.ok(updatedCruise);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a specific Cruise by ID
    @DeleteMapping("/cruise/{id}")
    public ResponseEntity<String> deleteCruise(@PathVariable int id) {
        
        boolean isDeleted = cruiseService.deleteCruise(id);
	    if (isDeleted) {
	        return new ResponseEntity<>("cruise deleted succesfully",HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
    }
}