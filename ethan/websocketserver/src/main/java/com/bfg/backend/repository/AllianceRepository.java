package com.bfg.backend.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bfg.backend.model.Alliance;

import antlr.collections.List;

@Repository
public interface AllianceRepository extends CrudRepository<Alliance, String>{

	@Query(value =  "SELECT alliance_name FROM Alliance WHERE alliance_name = ?1", nativeQuery = true)
	String findByAlliancename(@Param(value = "alliance_name") String alliance_name);
	
	@Query(value =  "SELECT * FROM Alliance", nativeQuery = true)
	List retrieveAlliances();
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO Alliance (alliance_name, admiral) VALUES (?1, ?2)", nativeQuery = true)
	void createAlliance(@Param(value = "alliance_name") String alliance_name, @Param(value = "admiral") String admiral);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO Alliance (alliance_name, member) VALUES (?1, ?2)", nativeQuery = true)
	void addMember(@Param(value = "alliance_name") String alliance_name, @Param(value = "member") String member);
}
