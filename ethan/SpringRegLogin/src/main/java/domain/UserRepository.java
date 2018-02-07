package domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	public User findbyUserName(String username);
}
