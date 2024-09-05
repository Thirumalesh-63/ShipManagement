package com.zapcom.shipmanagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zapcom.common.model.Cruiseline;
import com.zapcom.common.model.DatabaseSequence;
import com.zapcom.shipmanagement.exceptionhandler.CruiseLineAlreadyExists;
import com.zapcom.shipmanagement.exceptionhandler.CruiselineNotFound;
import com.zapcom.shipmanagement.repository.CruiselineRepository;

@Service
public class CruiselineService {

	@Autowired
	private CruiselineRepository cruiselinerepo;

	@Autowired
	private MongoTemplate mongoTemplate;

	// Create or Update a CruiseLine
	public Cruiseline saveCruiseLine(Cruiseline cruiseline) {
		int id = generateSequence("CruiseLine");
		cruiseline.setId(id);
		Cruiseline cruiseline2 = cruiselinerepo.findByName(cruiseline.getName());
		if (cruiseline2 == null)
			return cruiselinerepo.save(cruiseline);
		else
			throw new CruiseLineAlreadyExists("cruiseline already exists with the name " + cruiseline.getName());

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
			System.err.println(counter.getSeq());
			counter = new DatabaseSequence();
			counter.setId(seqName);
			counter.setSeq(1);
			mongoTemplate.save(counter);
			return 1;
		}

		return counter != null ? (int) counter.getSeq() : 1;
	}

	// Retrieve all CruiseLines
	public Page<Cruiseline> getAllCruiseLines(int page, int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Cruiseline> cruiselines = cruiselinerepo.findAll(pageable);
		if (cruiselines.getContent().size() == 0)
			throw new CruiselineNotFound("cruiselines not found  in this page " + page);
		return cruiselines;
	}

	// Retrieve a CruiseLine by ID
	public Optional<Cruiseline> getCruiseLineById(int id) {

		Optional<Cruiseline> cruiseline = cruiselinerepo.findById(id);
		if (cruiseline.isPresent()) {
			return cruiseline;
		} else
			throw new CruiselineNotFound("cruiseline not found with the id " + id);
	}
	// Retrieve a CruiseLine by ID
		public Cruiseline getCruiseLineByName(String name) {

			Cruiseline cruiseline = cruiselinerepo.findByName(name);
			if (cruiseline!=null) {
				return cruiseline;
			} else
				throw new CruiselineNotFound("cruiseline not found with the name " + name);
		}

	// Update a CruiseLine by ID
	public Cruiseline updateCruiseLine(int id, Cruiseline updatedCruiseLine) {
		Optional<Cruiseline> existingCruiseLine = cruiselinerepo.findById(id);
		if (existingCruiseLine.isPresent()) {
			Cruiseline cruiseline = existingCruiseLine.get();
			cruiseline.setName(updatedCruiseLine.getName());
			cruiseline.setHeadquarters(updatedCruiseLine.getHeadquarters());
			cruiseline.setContactnumber(updatedCruiseLine.getContactnumber());
			return cruiselinerepo.save(cruiseline);
		} else {
			throw new CruiselineNotFound("CruiseLine not found with id: " + id);
		}
	}

	// Delete a CruiseLine by ID
	public void deleteCruiseLine(int id) {

		Optional<Cruiseline> user = cruiselinerepo.findById(id);
		if (user.isPresent()) {
			cruiselinerepo.deleteById(id);
		} else {
			throw new CruiselineNotFound("cruiseline not found with the id " + id);

		}
	}

}
