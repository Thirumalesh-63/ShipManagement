package com.zapcom.shipmanagement.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zapcom.common.model.Cruise;
import com.zapcom.common.model.Cruiseline;
import com.zapcom.common.model.DatabaseSequence;
import com.zapcom.common.model.Ship;
import com.zapcom.shipmanagement.exceptionhandler.CruiseNotFound;
import com.zapcom.shipmanagement.exceptionhandler.ShipNotfound;
import com.zapcom.shipmanagement.repository.CruiseRepository;
import com.zapcom.shipmanagement.repository.CruiselineRepository;
import com.zapcom.shipmanagement.repository.ShipRepository;

@Service
public class CruiseService {

	Logger log=LoggerFactory.getLogger(CruiseService.class);


	@Autowired
	private CruiseRepository cruiseRepository;

	@Autowired
	private CruiselineRepository cruiselinerepo;

	@Autowired
	private ShipRepository shiprepository;


	@Autowired
	private MongoTemplate mongoTemplate;


	// Create a new Cruise
	public Cruise createCruise(Cruise cruise, int sid) {
		Optional<Ship> ship=shiprepository.findById(sid);
		if(ship.isPresent())
		{
			int id=generateSequence("Cruise");
			cruise.setId(id);
			cruise.setShip(ship.get());
			cruise.setCruiseline(ship.get().getCruiseline());

			return cruiseRepository.save(cruise);
		}
		throw new ShipNotfound("ship not found with id "+ sid);
	}

	// Retrieve all Cruises
	public Page<Cruise> getAllCruises(int page,int size) {

		Pageable pageable = PageRequest.of(page, size);
		return cruiseRepository.findAll(pageable);
	}


	// Update an existing Cruise
	public Cruise updateCruise(int id, Cruise cruiseDetails) {
		Optional<Cruise> optionalCruise = cruiseRepository.findById(id);

		Optional<Ship> ship=shiprepository.findById(cruiseDetails.getShip().getId());
		if (optionalCruise.isPresent()) {
			Cruise cruise = optionalCruise.get();
			cruise.setShip(cruiseDetails.getShip());
			cruise.setCruiseline(ship.get().getCruiseline());
			cruise.setEnddestination(cruiseDetails.getEnddestination());
			cruise.setStartdestination(cruiseDetails.getStartdestination());
			cruise.setStartdate(cruiseDetails.getStartdate());
			cruise.setEnddate(cruiseDetails.getStartdate());
			return cruiseRepository.save(cruise);
		} else {
			throw new CruiseNotFound("cruise is not found with the id "+id); // Or throw an exception if not found
		}
	}


	@Transactional
	public int generateSequence(String seqName) {
		Query query = Query.query(Criteria.where("_id").is(seqName));
		Update update = new Update().inc("seq", 1);

		DatabaseSequence counter = mongoTemplate.findAndModify(
				query,
				update,
				FindAndModifyOptions.options().returnNew(true).upsert(true), // Ensure upsert and return the new document after the update
				DatabaseSequence.class
				);
		if (counter == null) {
			System.err.println(counter.getSeq());
			counter = new DatabaseSequence();
			counter.setId(seqName);
			counter.setSeq(1);
			mongoTemplate.save(counter);
			return 1;
		}

		return counter != null ? (int) counter.getSeq() : 1;
	}


	// Delete a specific Cruise by ID
	public boolean deleteCruise(int id) {

		Optional<Cruise> ship=cruiseRepository.findById(id);
		if(ship.isPresent())
		{
			cruiseRepository.deleteById(id);
			return true;
		}
		else {

			return false;
			//throw new usernotfoundexception("usernot found with that id " +id);
		}
	}

	public Page<Cruise> getAllCruisesByStartdestination(String startdestination,int page,int size) {
		Pageable pageable = PageRequest.of(page, size);
		// TODO Auto-generated method stub
		return cruiseRepository.findByStartdestination(startdestination,pageable);
	}

	public Page<Cruise> getAllCruisesBYenddestination(String enddestination,int page,int size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page, size);
		// TODO Auto-generated method stub
		return cruiseRepository.findByEnddestination(enddestination,pageable);
	}

	public Page<Cruise> getAllCruisesBYcruiseline(String cruiseline,int page,int size) {

		Cruiseline cr=cruiselinerepo.findByName(cruiseline);
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page, size);

		return cruiseRepository.findBycruiseline(cr,pageable);
	}

	public List<String> getshipitinerary(String shipname) {
		// TODO Auto-generated method stub
		Ship ship=shiprepository.findByName(shipname);
		Cruise cruise= cruiseRepository.findByShip(ship);
		return cruise.getItinerary();
	}

	public Cruise getAllByship(String shipname) {
		Ship ship=shiprepository.findByName(shipname);
		return cruiseRepository.findByShip(ship);
	}

	public List<Cruise> getCruiseBetweenDates(Date startDate, Date endDate) {
		System.err.println(startDate);
		System.err.println(endDate);
		return cruiseRepository.findCruisesByDates(startDate, endDate);
	}

	public Cruise getCruiseById(int id) {
		// TODO Auto-generated method stub
		Optional<Cruise> cruise=cruiseRepository.findById(id);
		if(cruise.isPresent()) {
			return cruise.get();
		}
		else
			throw new CruiseNotFound("cruise not found with the id "+id);
	}




}
