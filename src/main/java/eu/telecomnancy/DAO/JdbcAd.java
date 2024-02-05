package eu.telecomnancy.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import eu.telecomnancy.Utils;
import eu.telecomnancy.base.Ad;

public class JdbcAd {

    // Replace below database url, username and password with your actual database credentials
    private static final String INSERT_QUERY = "INSERT INTO Ads (IsRequest,UserId,IsMaterial,Title,FCost,Description," +
    		"CategoryID,isAvailable,Localization,Comments,StartDateUTC,DurationInDay,IsRepetitive,EndDateUTC, Picture)" +
    		"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String DELETE_QUERY = "DELETE FROM Ads WHERE ID=?";
    private static final String UPDATE_QUERY = "UPDATE Ads SET IsRequest=?,UserId=?,IsMaterial=?,Title=?,FCost=?,Description=?," +
    		"CategoryID=?,isAvailable=?,Localization=?,Comments=?," +
    		"StartDateUTC=?,DurationInDay=?,IsRepetitive=?,EndDateUTC=?, Picture=? WHERE ID=?";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM Ads WHERE ID=?";
    private static final String READ_INFO_BY_ID_QUERY = "SELECT *, u.Name as Username FROM Ads INNER JOIN Users u " +
    		"ON u.ID = Ads.UserID WHERE Ads.ID=?";
    
    public int insert( Ad ad ) throws SQLException {

    	int adId=0;
    	
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            // Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY) ) {
            preparedStatement.setBoolean(1, ad.isIsRequest());
            preparedStatement.setInt(2, ad.getUserId());
            preparedStatement.setBoolean(3, ad.isMaterial());
            preparedStatement.setString(4, ad.getTitle());
            preparedStatement.setInt(5, ad.getCost());
            preparedStatement.setString(6, ad.getDescription());
            preparedStatement.setInt(7, ad.getCategoryId());
            preparedStatement.setBoolean(8, ad.isIsAvailable());
            preparedStatement.setString(9, ad.getGeolocalization());
            preparedStatement.setString(10, ad.getComments());
            long seconds = Utils.DateTimeToUnixTime( ad.getStartDate() );
            preparedStatement.setLong(11, seconds);
            preparedStatement.setInt(12, ad.getDurationInDay());
            preparedStatement.setBoolean(13, ad.isIsRepetitive());
            seconds = Utils.DateTimeToUnixTime( ad.getEndDate() );
            preparedStatement.setLong(14, seconds);
            preparedStatement.setString(15, ad.getPicture());
            System.out.println(preparedStatement);
            // execute the preparedstatement insert
            preparedStatement.executeUpdate();
            // get the last row ID
            ResultSet rs = connection.prepareStatement("select last_insert_rowid();").executeQuery();
            adId = rs.getInt("last_insert_rowid()");
            ad.setId(adId);
            preparedStatement.close();
        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
        return adId;
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
    
    public void update( Ad ad ) throws SQLException {

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY) ) {
            preparedStatement.setBoolean(1, ad.isIsRequest());
            preparedStatement.setInt(2, ad.getUserId());
            preparedStatement.setBoolean(3, ad.isMaterial());
            preparedStatement.setString(4, ad.getTitle());
            preparedStatement.setInt(5, ad.getCost());
            preparedStatement.setString(6, ad.getDescription());
            preparedStatement.setInt(7, ad.getCategoryId());
            preparedStatement.setBoolean(8, ad.isIsAvailable());
            preparedStatement.setString(9, ad.getGeolocalization());
            preparedStatement.setString(10, ad.getComments());
            long seconds = Utils.DateTimeToUnixTime( ad.getStartDate() );
            preparedStatement.setLong(11, seconds);
            preparedStatement.setInt(12, ad.getDurationInDay());
            preparedStatement.setBoolean(13, ad.isIsRepetitive());
            seconds = Utils.DateTimeToUnixTime( ad.getEndDate() );
            preparedStatement.setLong(14, seconds);
            preparedStatement.setString(15, ad.getPicture());
            preparedStatement.setInt(16, ad.getId());
            
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
    }

    public Ad selectByID( int ID ) throws SQLException {

    	Ad ad = null;
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY) ) {
            preparedStatement.setInt(1, ID);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            LocalDate sdate = Utils.UnixTimeToDateTime(resultSet.getLong("StartDateUTC"));
            LocalDate edate = Utils.UnixTimeToDateTime(resultSet.getLong("EndDateUTC"));
            
        	/*public Ad( int id, int uId, boolean isRequest, String title, int cost,
        			String description, int categoryId, boolean isAvailable, String localization, String comments,
        			LocalDate startDate, int duration, boolean isRepetitive, int endDate ) */

            ad = new Ad(resultSet.getInt("ID"),
            		resultSet.getInt("UserID"),
            		resultSet.getBoolean("isMaterial"),
            		resultSet.getBoolean("isRequest"),
            		resultSet.getString("Title"),
            		resultSet.getInt("FCost"),
            		resultSet.getString("Description"),
            		resultSet.getInt("CategoryID"),
            		resultSet.getBoolean("isAvailable"),
            		resultSet.getString("Localization"),
            		resultSet.getString("Comments"),
            		sdate,
            		resultSet.getInt("DurationInDay"),
            		resultSet.getBoolean("isRepetitive"),
            		edate,
                    resultSet.getString("Picture"));

        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return ad;
    }
    
    public String readInfoByID( int ID ) throws SQLException {

    	StringBuilder str_info = new StringBuilder();
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(READ_INFO_BY_ID_QUERY) ) {
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

    public int selectUserIdByID(int ID) throws SQLException {
        int userId = -1; // Valeur par défaut en cas d'échec

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT UserID FROM Ads WHERE ID=?")) {
            preparedStatement.setInt(1, ID);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("UserID");
            }

        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }

        return userId;
    }


}
