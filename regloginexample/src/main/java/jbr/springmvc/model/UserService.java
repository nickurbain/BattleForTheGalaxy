package jbr.springmvc.model;

import jbr.springmvc.model.User;
import jbr.springmvc.model.Login;
import org.springframework.beans.factory.annotation.Autowired;

import jbr.springmvc.dao.UserDao;

public interface UserService {

	public void register(User user);

	public User validateUser(Login login);

}

public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Override
	public void register(User user) {
		userDao.register(user);
	}

	@Override
	public User validateUser(Login login) {
		return userDao.validateUser(login);
	}
}