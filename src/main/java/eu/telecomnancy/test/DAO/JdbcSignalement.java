package eu.telecomnancy.test.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.telecomnancy.test.Utils;
import eu.telecomnancy.test.base.ServiceRow;
import eu.telecomnancy.test.base.Signalement;

public class JdbcSignalement {

    private static final String SELECT_ALL_QUERY = "SELECT * FROM Signalement";
    private static final String INSERT_QUERY = "INSERT INTO Signalement (message) VALUES (?)";

    public List<Signalement> selectAll( ) throws SQLException {

        List<Signalement> signalements = new ArrayList<Signalement>();

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            Statement stmt = connection.createStatement() ) {
            ResultSet resultSet = stmt.executeQuery(SELECT_ALL_QUERY);
            while( resultSet.next() ) {
                Signalement signalement = new Signalement(resultSet.getInt("ID"),
                        resultSet.getString("message"));
                signalements.add(signalement);
            }

        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return signalements;
    }

    public void insert(Signalement signalement) {
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, signalement.getDescription());

            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                signalement.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }
}
