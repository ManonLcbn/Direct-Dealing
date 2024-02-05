package eu.telecomnancy;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.swing.JOptionPane;

import javafx.scene.control.Alert;
import javafx.stage.Window;

public class Utils {

	public static final String DATABASE_URL = "jdbc:sqlite:src/main/resources/DirectDealing.db";
    
    public static final String SRC_URL = "";
    //public static final String SRC_URL = "/main/resources";	
	
    static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
    
    public static void infBox(String infoMessage, String title) {
        JOptionPane.showConfirmDialog(null,
                infoMessage,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    }

    public static int confirmBox(String infoMessage, String title) {
        int n = JOptionPane.showConfirmDialog(
                null,
                infoMessage,
                title,
                JOptionPane.YES_NO_OPTION);
        return n;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
    
    public static long DateTimeToUnixTime( LocalDate date ) {
    	long timeInSeconds = 0;
    	if( date != null ) {
    		LocalDateTime dt = date.atStartOfDay();
    		timeInSeconds = dt.toEpochSecond(ZoneOffset.UTC);
    	}
        return timeInSeconds;
    }

    public static LocalDate UnixTimeToDateTime( long seconds ) {
    	if( seconds > 0 ) {
	        Instant instant = Instant.ofEpochSecond(seconds);
	        return LocalDate.ofInstant(instant, ZoneOffset.UTC);
    	}
    	return null;
    }

}
