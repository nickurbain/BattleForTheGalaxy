package com.tests.jpaserver;
import org.springframework.data.repository.CrudRepository;

import com.tests.jpaserver.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {

//	User findOne(String string);

	
//	public User findByName(String name);
}