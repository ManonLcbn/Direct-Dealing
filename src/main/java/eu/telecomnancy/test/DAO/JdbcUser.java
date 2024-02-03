package eu.telecomnancy.test.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eu.telecomnancy.test.Utils;
import eu.telecomnancy.test.base.User;

public class JdbcUser {

    // Replace below database url, username and password with your actual database credentials
    private static final String SELECT_EXIST_QUERY = "SELECT * FROM Users WHERE email=?";
    private static final String SELECT_QUERY = "SELECT * FROM Users WHERE email=? AND password=?";
    private static final String INSERT_QUERY = "INSERT INTO Users (name,email,password,isDisable,Availability, Picture, admin)" +
    		"VALUES(?,?,?,?,?,?,?)";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM Users WHERE ID=?";
    private static final String UPDATE_QUERY = "UPDATE Users SET name=?, email=?, password=?, isDisable=?, Availability=?, Picture=?, admin=? " +
    		"WHERE ID = ?";
    private static final String SELECT_NAME_BY_ID_QUERY = "SELECT Name FROM Users WHERE ID = ?";
    private static final String UPDATE_FLORAINS_QUERY="UPDATE Users SET FAmount=? WHERE ID=?";

    public static String getUserNameById(int userId) {
        String userName = null;

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NAME_BY_ID_QUERY)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userName = resultSet.getString("Name");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }

        return userName;
    }
    
    public boolean isExists(String email) throws SQLException {

    	boolean isInDb = false;
        // Step 1: Establishing a Connection and 
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXIST_QUERY) ) {
            preparedStatement.setString(1, email);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            isInDb = resultSet.next();
            preparedStatement.close();

        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
        return isInDb;
    }

    public User validate(String emailId, String password) throws SQLException {

    	User user = null;
        // Step 1: Establishing a Connection and 
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY) ) {
            preparedStatement.setString(1, emailId);
            preparedStatement.setString(2, password);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            user = new User( resultSet.getInt("ID"), resultSet.getString("Name"), resultSet.getString("Email"),
            		resultSet.getString("Password"), resultSet.getBoolean("isDisable"),
            		resultSet.getString("Availability"), resultSet.getInt("FAmount"), resultSet.getString("Picture"), resultSet.getInt("admin"));
            preparedStatement.close();

        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
        return user;
    }

    public int insert( User usr ) throws SQLException {

    	int userId=0;
    	
        // Step 1: Establishing a Connection and 
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY) ) {
            preparedStatement.setString(1, usr.getName());       	
            preparedStatement.setString(2, usr.getEmail());
            preparedStatement.setString(3, usr.getPassword());
            preparedStatement.setBoolean(4, usr.isDisable());
            preparedStatement.setString(5, usr.getAvailability());
            preparedStatement.setString(6, usr.getPicture());
            preparedStatement.setInt(7, usr.getAdmin());
            System.out.println(preparedStatement);
            // execute the preparedstatement insert
            preparedStatement.executeUpdate();
            // get the last row ID
            ResultSet rs = connection.prepareStatement("select last_insert_rowid();").executeQuery();
            userId = rs.getInt("last_insert_rowid()");
            usr.setId(userId);
            preparedStatement.close();
        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
        return userId;
    }

    public User selectByID( int ID ) throws SQLException {

    	User user = null;
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY) ) {
            preparedStatement.setInt(1, ID);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            user = new User(resultSet.getInt("ID"),
            		resultSet.getString("Name"),
            		resultSet.getString("Email"),
            		resultSet.getString("Password"),
            		resultSet.getBoolean("isDisable"),
            		resultSet.getString("Availability"),
            		resultSet.getInt("FAmount"),
                    resultSet.getString("Picture"),
                    resultSet.getInt("admin"));

        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return user;
    }

    public void update( User usr ) throws SQLException {

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY) ) {
            preparedStatement.setString(1, usr.getName());
            preparedStatement.setString(2, usr.getEmail());
            preparedStatement.setString(3, usr.getPassword());
            preparedStatement.setBoolean(4, usr.isDisable());
            preparedStatement.setString(5, usr.getAvailability());
            preparedStatement.setString(6, usr.getPicture());
            preparedStatement.setInt(7, usr.getAdmin());
            preparedStatement.setInt(8, usr.getId());
            
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
    }

    public int selectAmountByID(int userID){
        int amount=0;

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                amount = resultSet.getInt("FAmount");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
        return amount;
    }
    
    public void updateFlorains(int userID,int changeAmount){
        int amount=selectAmountByID(userID);
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_FLORAINS_QUERY) ) {
            preparedStatement.setInt(1, amount+changeAmount);
            preparedStatement.setInt(2,userID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
    }

    public List<Integer> getAdmin() {
        List<Integer> adminList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID FROM Users WHERE admin=1")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                adminList.add(resultSet.getInt("ID"));
            }
        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
        return adminList;

    }
}
