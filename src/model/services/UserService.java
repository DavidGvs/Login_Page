package model.services;

import model.dao.DaoFactory;
import model.dao.UserDao;
import model.entities.User;

public class UserService {

private UserDao dao = DaoFactory.createUserDao();
	
	public boolean save(User obj) {
		if (dao.findByUsername(obj.getUsername()) == null && dao.findByEmailAdress(obj.getEmail_adress()) == null) {
			dao.insert(obj);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean delete(User obj) {
		if (dao.findByUsername(obj.getUsername()) != null) {
			dao.delete(obj);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean update(User olduser, User newuser) {
		if ((dao.findByUsername(olduser.getUsername()) != null 
				&& dao.findByUsername(newuser.getUsername()) == null 
				&& dao.findByUsername(newuser.getEmail_adress()) == null) 
				|| dao.findByUsername(olduser.getUsername()) == dao.findByUsername(newuser.getUsername())
				|| dao.findByUsername(olduser.getEmail_adress()) == dao.findByUsername(newuser.getEmail_adress())) {
			dao.update(olduser, newuser);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean login(User obj) {
		if (dao.findByUsername(obj.getUsername()) != null && dao.comparePassword(obj.getUsername(), obj.getPassword()) != null) {
			return true;
		} else return false;
	}
	
	public User getUserData(User obj) {
		if (login(obj)) {
			User user = dao.findByUsername(obj.getUsername());
			return user;
		} else {
			return null;
		}
	}
}
