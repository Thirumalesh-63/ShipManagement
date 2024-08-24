package com.zapcom.shipmanagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.zapcom.common.model.Cruiseline;
import com.zapcom.common.model.DatabaseSequence;
import com.zapcom.shipmanagement.exceptionhandler.CruiselineNotFound;
import com.zapcom.shipmanagement.repository.CruiselineRepository;

@Service
public class CruiselineService {

	@Autowired
	private CruiselineRepository cruiselinerepo;
	

	@Autowired
	private MongoTemplate mongoTemplate;


    // Create or Update a CruiseLine
    public Cruiseline saveCruiseLine(Cruiseline cruiseLine) {
    	int id=generateSequence("CruiseLine");
    	cruiseLine.setId(id);
        return cruiselinerepo.save(cruiseLine);
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
    
    // Retrieve all CruiseLines
    public Page<Cruiseline> getAllCruiseLines(int page,int size) {
    	
    	Pageable pageable = PageRequest.of(page, size);
        return cruiselinerepo.findAll(pageable);
    }

    // Retrieve a CruiseLine by ID
    public Optional<Cruiseline> getCruiseLineById(int id) {
    	
    	Optional<Cruiseline> cruiseline=cruiselinerepo.findById(id);
    	if(cruiseline.isPresent()) {
    		return cruiseline;
    	}
    	else
    		throw new CruiselineNotFound("cruiseline not found with the id "+id);


    }

    // Update a CruiseLine by ID
    public Cruiseline updateCruiseLine(int id, Cruiseline updatedCruiseLine) {
        Optional<Cruiseline> existingCruiseLine = cruiselinerepo.findById(id);
        if (existingCruiseLine.isPresent()) {
        	Cruiseline cruiseLine = existingCruiseLine.get();
            cruiseLine.setName(updatedCruiseLine.getName());
            cruiseLine.setHeadquarters(updatedCruiseLine.getHeadquarters());
            cruiseLine.setContactnumber(updatedCruiseLine.getContactnumber());
            return cruiselinerepo.save(cruiseLine);
        } else {
            throw new RuntimeException("CruiseLine not found with id: " + id);
        }
    }

    // Delete a CruiseLine by ID
    public void deleteCruiseLine(int id) {
    	
    	Optional<Cruiseline> user=cruiselinerepo.findById(id);
		if(user.isPresent())
		{
			cruiselinerepo.deleteById(id);
			
		}
		else {
		
			throw new CruiselineNotFound("cruiseline not found with the id "+id);

		}
	}
    	
    
}
