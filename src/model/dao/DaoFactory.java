package model.dao;

import db.DB;
import model.dao.impl.UserDaoJDBC;

public class DaoFactory {

	public static UserDao createUserDao() {
		return new UserDaoJDBC(DB.getConnection());
	}
}
