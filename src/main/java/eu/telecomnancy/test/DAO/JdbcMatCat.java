package eu.telecomnancy.test.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eu.telecomnancy.test.Utils;
import eu.telecomnancy.test.base.MatCat;


public class JdbcMatCat {

    // Replace below database url, username and password with your actual database credentials
    private static final String SELECT_QUERY = "SELECT * FROM Categories WHERE isMaterial=? ORDER BY Name";
    

    public List<MatCat> select( boolean isMaterial ) throws SQLException {

    	List<MatCat> categories = new ArrayList<MatCat>();
    	
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY) ) {
            preparedStatement.setBoolean(1, isMaterial);
            ResultSet resultSet = preparedStatement.executeQuery();

            while( resultSet.next() ) {
            	MatCat cat = new MatCat(resultSet.getInt("ID"), resultSet.getString("Name"), resultSet.getInt("SuggestedCost"));
            	categories.add(cat);
            }

        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return categories;
    }

}
