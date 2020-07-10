package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DB;
import db.DbException;
import model.dao.UserDao;
import model.entities.User;

public class UserDaoJDBC implements UserDao {

	private Connection conn;

	public UserDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(User obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"insert into utilizador (username, password, email_adress) values (?, ?, ?)");

			st.setString(1, obj.getUsername());
			st.setString(2, obj.getPassword());
			st.setString(3, obj.getEmail_adress());
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public User findByUsername(String username) {
		PreparedStatement st = null;

		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select username, password, email_adress from utilizador where username = ?");

			st.setString(1, username);
			rs = st.executeQuery();

			if (rs.next()) {
				User user = new User();
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setEmail_adress(rs.getString("email_adress"));
				return user;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public User findByEmailAdress(String email_adress) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select username, email_adress from utilizador where email_adress = ?");

			st.setString(1, email_adress);
			rs = st.executeQuery();

			if (rs.next()) {
				User user = new User();
				user.setUsername(rs.getString("username"));
				user.setEmail_adress(rs.getString("email_adress"));
				return user;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public User comparePassword(String username, String password) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select username, password "
					+ "from utilizador "
					+ "where username = ? and password = ?");

			st.setString(1, username);
			st.setString(2, password);
			rs = st.executeQuery();

			if (rs.next()) {
				User user = new User();
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				return user;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void delete(User obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"delete from utilizador where username = ?");

			st.setString(1, obj.getUsername());
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}
	
	@Override
	public User update(User olduser, User newuser) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"update utilizador "
					+ "set username = ?, password = ?, email_adress = ? "
					+ "where username = ?");

			st.setString(1, newuser.getUsername());
			st.setString(2, newuser.getPassword());
			st.setString(3, newuser.getEmail_adress());
			st.setString(4, olduser.getUsername());
			int rowsAffected = st.executeUpdate();

			if (rowsAffected != 0) {
				User user = new User();
				user.setUsername(findByUsername(newuser.getUsername()).getUsername());
				user.setPassword(findByUsername(newuser.getUsername()).getPassword());
				user.setEmail_adress(findByUsername(newuser.getUsername()).getEmail_adress());
				return user;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
