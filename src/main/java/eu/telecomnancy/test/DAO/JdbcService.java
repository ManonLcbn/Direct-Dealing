package eu.telecomnancy.test.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import eu.telecomnancy.test.Utils;
import eu.telecomnancy.test.base.Service;

public class JdbcService {

    // Replace below database url, username and password with your actual database credentials
    private static final String INSERT_QUERY = "INSERT INTO Services (UserID,isRequest,Title,Description,CategoryID," +
    		"isAvailable,Localization,Comments,isPonctual,StartDateUTC,EndDateUTC,Day,Hour,Minutes,FCost)" +
    		"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String DELETE_QUERY = "DELETE FROM Services WHERE ID=?";
    private static final String UPDATE_QUERY = "UPDATE Services SET UserID=?,IsRequest=?,Title=?,Description=?,CategoryID=?" +
    		"isAvailable=?,Localization=?,Comments=?,isPonctual=?,StartDateUTC=?,EndDateUTC=?,Day=?,Hour=?,Minutes=?,FCost=?" +
    		"WHERE ID=?";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM Services WHERE ID=?";
    private static final String REservice_INFO_BY_ID_QUERY = "SELECT *, u.Name as Username FROM Services INNER JOIN Users u " +
    		"ON u.ID = Services.UserID WHERE Services.ID=?";
    
    public int insert( Service service) throws SQLException {

    	int serviceId=0;
    	
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            // Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY) ) {
            preparedStatement.setInt(1, service.getUserId());
            preparedStatement.setBoolean(2, service.isIsRequest());
            preparedStatement.setString(3, service.getTitle());
            preparedStatement.setString(4, service.getDescription());
            preparedStatement.setInt(5, service.getCategoryId());
            preparedStatement.setBoolean(6, service.isIsAvailable());
            preparedStatement.setString(7, service.getGeolocalization());
            preparedStatement.setString(8, service.getComments());
            preparedStatement.setBoolean(9, service.isIsPonctual());
            long seconds = Utils.DateTimeToUnixTime( service.getStartDate() );
            preparedStatement.setLong(10, seconds);
            seconds = Utils.DateTimeToUnixTime( service.getEndDate() );
            preparedStatement.setLong(11, seconds);
            preparedStatement.setString(12, service.getDay());
            preparedStatement.setInt(13, service.getHour());
            preparedStatement.setInt(14, service.getMinutes());
            preparedStatement.setInt(15, service.getCost());
            System.out.println(preparedStatement);
            // execute the preparedstatement insert
            preparedStatement.executeUpdate();
            // get the last row ID
            ResultSet rs = connection.prepareStatement("select last_insert_rowid();").executeQuery();
            serviceId = rs.getInt("last_insert_rowid()");
            service.setId(serviceId);
            preparedStatement.close();
        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
        return serviceId;
    }

    public void delete( int id ) throws SQLException {

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL)) {
        	PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setInt(1, id);
            
            System.out.println(preparedStatement);
            // execute the preparedstatement insert
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
    }
    
    public void update( Service service ) throws SQLException {

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY) ) {
            preparedStatement.setInt(1, service.getUserId());
            preparedStatement.setBoolean(2, service.isIsRequest());
            preparedStatement.setString(3, service.getTitle());
            preparedStatement.setString(4, service.getDescription());
            preparedStatement.setInt(5, service.getCategoryId());
            preparedStatement.setBoolean(6, service.isIsAvailable());
            preparedStatement.setString(7, service.getGeolocalization());
            preparedStatement.setString(8, service.getComments());
            preparedStatement.setBoolean(9, service.isIsPonctual());
            long seconds = Utils.DateTimeToUnixTime( service.getStartDate() );
            preparedStatement.setLong(10, seconds);
            seconds = Utils.DateTimeToUnixTime( service.getEndDate() );
            preparedStatement.setLong(11, seconds);
            preparedStatement.setString(12, service.getDay());
            preparedStatement.setInt(13, service.getHour());
            preparedStatement.setInt(14, service.getMinutes());
            preparedStatement.setInt(15, service.getCost());
            preparedStatement.setInt(16, service.getId());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
    }

    public Service selectByID( int ID ) throws SQLException {

    	Service service = null;
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY) ) {
            preparedStatement.setInt(1, ID);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            LocalDate sdate = Utils.UnixTimeToDateTime(resultSet.getLong("StartDateUTC"));
            LocalDate edate;
            if (resultSet.getBoolean("isPonctual")){
                edate=Utils.UnixTimeToDateTime(0);
            }
            else {
                edate = Utils.UnixTimeToDateTime(resultSet.getLong("EndDateUTC"));
            }
            
        	/*public service( int id, int uId, boolean isRequest, String title, int cost,
        			String description, int categoryId, boolean isAvailable, String localization, String comments,
        			LocalDate startDate, int duration, boolean isRepetitive, int endDate ) */

            service = new Service(resultSet.getInt("ID"),
            		resultSet.getInt("UserID"),
            		resultSet.getBoolean("isRequest"),
            		resultSet.getString("Title"),
            		resultSet.getString("Description"),
            		resultSet.getInt("CategoryID"),
                    resultSet.getBoolean("isAvailable"),
            		resultSet.getString("Localization"),
            		resultSet.getString("Comments"),
            		resultSet.getBoolean("isPonctual"),
            		sdate,
                    edate,
            		resultSet.getString("Day"),
            		resultSet.getInt("Hour"),
                    resultSet.getInt("Minutes"),
                    resultSet.getInt("FCost"));

        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return service;
    }
    
    public String readInfoByID( int ID ) throws SQLException {

    	StringBuilder str_info = new StringBuilder();
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(REservice_INFO_BY_ID_QUERY) ) {
            preparedStatement.setInt(1, ID);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            String username = resultSet.getString("Username");
            //LocalDate sdate = Utils.UnixTimeToDateTime(resultSet.getLong("StartDateUTC"));
            //LocalDate edate = Utils.UnixTimeToDateTime(resultSet.getLong("EndDateUTC"));
            
            str_info.append("Annonce créée par " + username);

        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return str_info.toString();
    }

    public static int maxID() throws SQLException {
    	int maxID = 0;
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ID) FROM services") ) {
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            maxID = resultSet.getInt("MAX(ID)");

        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return maxID;
    }


}


