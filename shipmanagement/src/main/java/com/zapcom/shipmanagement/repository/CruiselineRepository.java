package com.zapcom.shipmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.zapcom.common.model.Cruiseline;

@Repository
public interface CruiselineRepository extends MongoRepository<Cruiseline, Integer>{

	Cruiseline findByName(String cruiseline);

}
