package com.zapcom.shipmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.zapcom.common.model.Ship;

public interface ShipRepository extends MongoRepository<Ship, Integer> {

	Ship findByName(String shipname);

}
