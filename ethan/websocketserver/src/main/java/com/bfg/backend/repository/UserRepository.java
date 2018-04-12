package com.bfg.backend.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bfg.backend.model.User;

//This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
//CRUD refers Create, Read, Update, Delete

/**
 * Methods used to traverse the user database
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * Locates if a user exists in the database
	 * @param user_name The user name to find
	 * @return true if the user exists, false otherwise
	 */
	@Query(value =  "SELECT user_name FROM user WHERE user_name = ?1", nativeQuery = true)
	String findByUsername(@Param(value = "user_name") String user_name);
	
	/**
	 * Finds a user by password. ?1 = the password entered
	 * @param user_pass The password to find
	 * @return true if the password exist, false otherwise
	 */
	@Query(value =  "SELECT user_pass FROM user WHERE user_pass = ?1", nativeQuery = true)
	String findByPass(@Param(value = "user_pass") String user_pass);
	
	/**
	 * Find a user by login. ?1 = var_1(the user name entered), ?2 = var_2 (the password entered)
	 * @param user_name The user name entered
	 * @param user_pass The password entered
	 * @return true if the user name and password exist, false otherwise
	 */
	@Query(value =  "SELECT user_id FROM user WHERE user_name = ?1 AND user_pass = ?2", nativeQuery = true)
	Long findByLogin(@Param(value = "user_name") String user_name, @Param(value = "user_pass") String user_pass);

	/**
	 * Inserts a user into the database
	 * @param user_name The user name to place in the database
	 * @param user_pass The password for the user being entered
	 */
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO user (user_name, user_pass) VALUES (?1, ?2)", nativeQuery = true)
	void createUser(@Param(value = "user_name") String user_name, @Param(value = "user_pass") String user_pass);	
}