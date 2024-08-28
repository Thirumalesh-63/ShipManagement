package com.zapcom.shipmanagement.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.zapcom.common.model.Cruise;
import com.zapcom.common.model.Cruiseline;
import com.zapcom.common.model.Ship;

@Repository
public interface CruiseRepository extends MongoRepository<Cruise, Integer> {

	public Page<Cruise> findByStartdestination(String startdestination, Pageable pageable);

	public Page<Cruise> findByEnddestination(String enddestination, Pageable pageable);

	public Page<Cruise> findBycruiseline(Cruiseline cr, Pageable pageable);

	public Cruise findByShip(Ship ship);

	@Query("{'startdate': {$gte: ?0}, 'enddate': {$lte: ?1}}")
	public List<Cruise> findCruisesByDates(Date startDate, Date endDate);

	public Optional<Cruise> findBycruiseName(String cruiseName);

}
