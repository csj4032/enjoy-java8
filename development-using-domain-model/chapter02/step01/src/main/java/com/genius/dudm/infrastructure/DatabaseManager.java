package com.genius.dudm.infrastructure;

import java.sql.*;

public class DatabaseManager {

	private DatabaseManager() {

	}

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:log4jdbc:mariadb://localhost:3306/primavera?user=primavera&password=primavera");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void close(ResultSet resultSet) {
		if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
	}

	public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
		if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
		if (preparedStatement != null) try { preparedStatement.close(); } catch (SQLException e) { e.printStackTrace(); }
		if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
	}

	public static void execute(String sql) throws SQLException {
		getConnection().prepareStatement(sql).execute();
	}
}
