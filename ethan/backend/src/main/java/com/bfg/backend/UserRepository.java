package com.bfg.backend;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


//This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
//CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {
	
	User findById(@Param("UserId") Integer UserId);
	

}
