package jpa_demo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;


// Allows for CRUD operations since it extends CrudRepository
public interface PersonRepo extends CrudRepository<Person, Long>{
	List<Person> findByLastName(String lastName);
}
