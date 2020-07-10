package model.dao;

import model.entities.User;

public interface UserDao {

	void insert(User obj);
	void delete(User obj);
	User update(User olduser, User newuser);
	User findByUsername(String username);
	User findByEmailAdress(String email_adress);
	User comparePassword(String username, String password);
}
