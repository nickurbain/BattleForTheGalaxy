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
 * UserRepository is used for interfacing with the database. It extends
 * CrudRepository for full CRUD operations.
 * This is autowired by Spring in the SocketHandler class.
 * 
 * @author jln, emball
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * Searches the database for a matching username and returns it if exists
	 * 
	 * @param user_name
	 * @return 
	 * 		a string of the user's name if it exists
	 */
	@Query(value =  "SELECT user_name FROM user WHERE user_name = ?1", nativeQuery = true)
	String findByUsername(@Param(value = "user_name") String user_name);
	
	@Query(value =  "SELECT user_id FROM user WHERE user_name = ?1", nativeQuery = true)
	long findIdByUsername(@Param(value = "user_name") String user_name);
	
	@Query(value =  "SELECT a.alliance_name FROM Alliance_member m, Alliance a WHERE user_id = ?1 and m.alliance_id = a.alliance_id", nativeQuery = true)
	String findAllianceById(@Param(value = "user_id") Long user_id);
	
	@Query(value =  "SELECT doubloons FROM user WHERE user_name = ?1", nativeQuery = true)
	int getDoubloonsByUsername(@Param(value = "user_name") String user_name);
	
	/**
	 * Queries the database for a matching user pass and returns it if exists
	 * 
	 * @param user_pass The password
	 * @return 
	 * 		the matching password if exists
	 */
	@Query(value =  "SELECT user_pass FROM user WHERE user_pass = ?1", nativeQuery = true)
	String findByPass(@Param(value = "user_pass") String user_pass);
	
	/**
	 * Finds a user by their username and password. Returns their id if exists. ?1 = var_1, ?2 = var_2
	 * @param user_name The user name entered
	 * @param user_pass The password entered
	 * @return true if the user name and password exist, false otherwise
	 */
	@Query(value =  "SELECT user_id FROM user WHERE user_name = ?1 AND user_pass = ?2", nativeQuery = true)
	Long findByLogin(@Param(value = "user_name") String user_name, @Param(value = "user_pass") String user_pass);

	/**
	 * Inserts a user into the database with the parameters given
	 * @param user_name The user name to place in the database
	 * @param user_pass The password for the user being entered
	 */
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO user (user_name, user_pass) VALUES (?1, ?2)", nativeQuery = true)
	void createUser(@Param(value = "user_name") String user_name, @Param(value = "user_pass") String user_pass);
	
	
	/**
	 * Inserts a user into the database with the parameters given
	 * @param user_name The user name to place in the database
	 * @param user_pass The password for the user being entered
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE user SET doubloons = ?1 WHERE user_name = ?2", nativeQuery = true)
	void addDoubloons(@Param(value = "doubloons") Integer doubloons, @Param(value = "user_name") String user_name);

}