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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.zapcom.common.model.DatabaseSequence;
import com.zapcom.common.model.Ship;
import com.zapcom.shipmanagement.exceptionhandler.ShipNotfound;
import com.zapcom.shipmanagement.repository.ShipRepository;

@Service
public class ShipService {
	
	@Autowired
	private ShipRepository shiprepository;
	

	@Autowired
	private MongoTemplate mongoTemplate;

	
	 public Ship createShip(Ship ship) {
		 int id=generateSequence("ship");
		 ship.setId(id);
	        return shiprepository.save(ship);
	    }

	    // Retrieve all Ships
	    public Page<Ship> getAllShips(int page,int size) {
	    	Pageable pageable = PageRequest.of(page, size);
	        return shiprepository.findAll(pageable);
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
	    	Optional<Ship> ship= shiprepository.findById(id);
	    	if(ship.isPresent())
	    		return ship;
	    	else
	    		throw new ShipNotfound("ship not found with the id "+id);
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
	            throw new ShipNotfound("ship not found with the id "+ id); // Or throw an exception if not found
	        }
	    }

	    // Delete a specific Ship by ID
	    public boolean deleteShip(int id) {
	    	Optional<Ship> ship=shiprepository.findById(id);
			if(ship.isPresent())
			{
				shiprepository.deleteById(id);
				return true;
			}
			else {
				throw new ShipNotfound("ship found with that id " +id);
			}
		}
	    

}
