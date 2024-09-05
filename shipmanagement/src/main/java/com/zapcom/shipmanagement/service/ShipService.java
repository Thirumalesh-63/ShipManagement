package com.zapcom.shipmanagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zapcom.common.model.Cruiseline;
import com.zapcom.common.model.DatabaseSequence;
import com.zapcom.common.model.Ship;
import com.zapcom.shipmanagement.exceptionhandler.ShipAlreadyExists;
import com.zapcom.shipmanagement.exceptionhandler.ShipNotfound;
import com.zapcom.shipmanagement.repository.ShipRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ShipService {

	@Autowired
	private ShipRepository shiprepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	private RestTemplate restTemplate;

	public ShipService(RestTemplateBuilder builder) {

		this.restTemplate = builder.build();

	}

	public Ship createShip(Ship ship, int cid) {
		int id = generateSequence("ship");
		ship.setId(id);
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String jwtToken = request.getHeader("Authorization"); // Retrieve the token
		// Create headers and add the JWT token
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", jwtToken); // Use the same token

		// Create the entity with the headers
		HttpEntity<String> entity = new HttpEntity<>(headers);

		// Make the request with the token
		ResponseEntity<Cruiseline> response = restTemplate.exchange(
				"http://localhost:8083/shipmanagement/admin/cruiseline/{cid}", HttpMethod.GET, entity, Cruiseline.class,
				cid);

		// User
		// user=restTemplate.getForObject("http://localhost:8080/userregistry/user/{id}",
		// User.class, id);

		Cruiseline cruiseline = response.getBody();
		System.err.println(cid);
		System.err.println(cruiseline);
		if (cruiseline != null) {
			ship.setCruiseline(cruiseline);
			Ship ship2 = shiprepository.findByName(ship.getName());
			if (ship2 == null) {
				return shiprepository.save(ship);
			} else {
				throw new ShipAlreadyExists("ship already exists with the shipname " + ship.getName());
			}
		}
		throw new ShipNotfound("cruiseline not find with the id " + cid);
	}

	// Retrieve all Ships
	public Page<Ship> getAllShips(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Ship> pages = shiprepository.findAll(pageable);
		if (pages.getContent().size() == 0)
			throw new ShipNotfound("ships not found in this page " + page);
		return pages;
	}

	@Transactional
	public int generateSequence(String seqName) {
		Query query = Query.query(Criteria.where("_id").is(seqName));
		Update update = new Update().inc("seq", 1);

		DatabaseSequence counter = mongoTemplate.findAndModify(query, update,
				FindAndModifyOptions.options().returnNew(true).upsert(true), // Ensure upsert and return the new
																				// document after the update
				DatabaseSequence.class);
		if (counter == null) {
			counter = new DatabaseSequence();
			counter.setId(seqName);
			counter.setSeq(1);
			mongoTemplate.save(counter);
			return 1;
		}

		return counter != null ? (int) counter.getSeq() : 1;
	}

	// Retrieve a specific Ship by ID
	public Optional<Ship> getShipById(int id) {
		Optional<Ship> ship = shiprepository.findById(id);
		if (ship.isPresent())
			return ship;
		else
			throw new ShipNotfound("ship not found with the id " + id);
	}

	// Update an existing Ship
	public Ship updateShip(int id, Ship shipDetails) {
		Optional<Ship> optionalShip = shiprepository.findById(id);
		if (optionalShip.isPresent()) {
			Ship ship = optionalShip.get();
			ship.setName(shipDetails.getName());
			ship.setCruiseline(shipDetails.getCruiseline());
			ship.setCapacity(shipDetails.getCapacity());
			return shiprepository.save(ship);
		} else {
			throw new ShipNotfound("ship not found with the id " + id); // Or throw an exception if not found
		}
	}

	// Delete a specific Ship by ID
	public boolean deleteShip(int id) {
		Optional<Ship> ship = shiprepository.findById(id);
		if (ship.isPresent()) {
			shiprepository.deleteById(id);
			return true;
		} else {
			throw new ShipNotfound("ship found with that id " + id);
		}
	}

}
