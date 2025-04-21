package util.database;

import java.sql.*;

public class Database {
	private Connection connect;
	private static final String DatabaseURL = "jdbc:mysql:///school_management_db";

	// Your database user name
	private static final String Username = "root";

	// Your database password
	private static final String Password = "root123";
	
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(DatabaseURL, Username, Password);
	}
	
	// <T> is an dynamic unknown type
	public <T> T executeQuery(String sqlQuery, ParamSetter paramSetter, ResultProcessor<T> resultSetProcessor) {
		try(
				// help with automatic close of the DB connection
				Connection con = getConnection();
				PreparedStatement stm = con.prepareStatement(sqlQuery);
		)
		{
			// apply parameters on the PrepareStatement if provided
			if (paramSetter != null) {
				paramSetter.setParams(stm);
			}
			// SELECT query
			if (resultSetProcessor != null) {
				ResultSet results = stm.executeQuery(); //retrieve from DB & executed SELECT query
				return resultSetProcessor.process(results);
			} 
			// INSET, UPDATE, or DELETE operation
			else {
				int rows = stm.executeUpdate(); // returns num of affected rows
				// Return True if the num of rows affected is > 0
				return (T)Boolean.valueOf(rows > 0);
			}
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
			return (T)Boolean.valueOf(false);
		}
	}
	
	public interface ParamSetter {
		// Method to set parameters on the PreparedStatement
		// Implemented in the Model Class
		void setParams(PreparedStatement stm) throws SQLException;
	}
	
	public interface ResultProcessor<T> {
		// Method to process a ResultSet into the desired return type
		// Implemented in the Model Class
		T process(ResultSet result) throws SQLException;
	}
}
