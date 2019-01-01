package com.bfg.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bfg.backend.model.Alliance;

@Repository
public interface AllianceRepository extends CrudRepository<Alliance, String>{

	@Query(value =  "SELECT alliance_name FROM Alliance WHERE alliance_name = ?1", nativeQuery = true)
	String findByAlliancename(@Param(value = "alliance_name") String alliance_name);
	
	@Query(value = "SELECT alliance_id FROM Alliance WHERE alliance_name = ?1", nativeQuery = true)
	Long findIDByAllianceName(@Param(value = "alliance_name") String alliance_name);
	
	@Query(value =  "SELECT alliance_name FROM Alliance", nativeQuery = true)
	List<String> retrieveAlliances();
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO Alliance (alliance_name) VALUES (?1)", nativeQuery = true)
	void createAlliance(@Param(value = "alliance_name") String alliance_name);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO Alliance_admiral (user_id, alliance_id) VALUES (?1, ?2)", nativeQuery = true)
	void addAllianceAdmiral(@Param(value="user_id") long user_id, @Param(value = "alliance_name") long alliance_id);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO Alliance_member (alliance_id, user_id) Values(?1,?2)", nativeQuery = true)
	void addMember(@Param(value = "alliance_id") long alliance_id, @Param(value = "user_id") long user_id);
}
