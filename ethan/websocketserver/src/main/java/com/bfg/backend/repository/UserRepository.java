package com.bfg.backend.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bfg.backend.User;

//This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
//CRUD refers Create, Read, Update, Delete

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	@Query(value =  "SELECT user_name FROM user WHERE user_name = ?1", nativeQuery = true)
	String findByUsername(@Param(value = "user_name") String user_name);
	
	@Query(value =  "SELECT user_pass FROM user WHERE user_pass = ?1", nativeQuery = true)
	String findByPass(@Param(value = "user_pass") String user_pass);
	
	
	/* ?1 = var_1, ?2 = var_2 */
	@Query(value =  "SELECT user_id FROM user WHERE user_name = ?1 AND user_pass = ?2", nativeQuery = true)
	Long findByLogin(@Param(value = "user_name") String user_name, @Param(value = "user_pass") String user_pass);
	
	// TODO: Test this
	@Query(value = "INSERT INTO users (user_id, user_name, user_pass) VALUES (?1, ?2, ?3)", nativeQuery = true)
	void createUser(@Param(value = "user_name") String user_name, @Param(value = "user_pass") String user_pass);
	
}
