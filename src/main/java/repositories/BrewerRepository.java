package repositories;

import models.Brewer;
import models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrewerRepository {
    private static final String INSERT_BREWER_SQL = "INSERT INTO brewers (Name, Address, ZipCode, City, Turnover) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_BREWER_SQL = "UPDATE brewers SET Name = ?, Address = ?, ZipCode = ?, City = ?, Turnover = ? WHERE Id = ?";
    private static final String DELETE_BREWER_SQL = "DELETE FROM brewers WHERE Id = ?";
    private static final String SELECT_ALL_SQL = "SELECT Id, Name, Address, ZipCode, City, Turnover FROM brewers";

    public List<Brewer> read() {
        System.out.println("--------Read-------");
        List<Brewer> results = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/thebelgianbrewerydb", "intec", "intec-123"
            );
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL);

            while (resultSet.next()) {
                Brewer brewer = new Brewer();
                brewer.setId(resultSet.getInt("Id"));
                brewer.setName(resultSet.getString("Name"));
                brewer.setAddress(resultSet.getString("Address"));
                brewer.setZipCode(resultSet.getString("ZipCode"));
                brewer.setCity(resultSet.getString("City"));
                brewer.setTurnover(resultSet.getInt("Turnover"));
                results.add(brewer);

                System.out.println("ID: " + brewer.getId());
                System.out.println("Name: " + brewer.getName());
                System.out.println("Address: " + brewer.getAddress());
                System.out.println("ZipCode: " + brewer.getZipCode());
                System.out.println("City: " + brewer.getCity());
                System.out.println("Turnover: " + brewer.getTurnover());
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return results;
    }

    // Een brouwer toevoegen
    public void create(String name, String address, String zipCode, String city, int turnover){
        System.out.println("--------Create--------");
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BREWER_SQL)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, zipCode);
            preparedStatement.setString(4, city);
            preparedStatement.setInt(5, turnover);

            preparedStatement.executeUpdate();

            System.out.println("Brouwer '" + name + "' succesvol toegevoegd.");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    // Meerdere brouwers toevoegen
    public void createMultiple(List<Object[]> brewers) {
        System.out.println("--------CreateMultiple--------");

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BREWER_SQL)) {

            connection.setAutoCommit(false); // Start transactie

            for (Object[] b : brewers) {
                preparedStatement.setString(1, (String) b[0]); // name
                preparedStatement.setString(2, (String) b[1]); // address
                preparedStatement.setString(3, (String) b[2]); // zipCode
                preparedStatement.setString(4, (String) b[3]); // city
                preparedStatement.setInt(5, (int) b[4]);       // turnover
                preparedStatement.addBatch();
                System.out.println("Brouwer '" + b[0] + "' toegevoegd aan batch.");
            }

            preparedStatement.executeBatch(); // Voer batch uit
            connection.commit(); // Commit transactie

            System.out.println("Alle brouwers zijn succesvol toegevoegd.");

        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    // Brouwer aanpassen
    public void update(int id, String name, String address, String zipCode, String city, int turnover) {
        System.out.println("--------Update--------");
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BREWER_SQL)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, zipCode);
            preparedStatement.setString(4, city);
            preparedStatement.setInt(5, turnover);
            preparedStatement.setInt(6, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Brouwer met ID " + id + " is succesvol aangepast.");
                System.out.println("Nieuwe gegevens:");
                System.out.println("Name: " + name);
                System.out.println("Address: " + address);
                System.out.println("ZipCode: " + zipCode);
                System.out.println("City: " + city);
                System.out.println("Turnover: " + turnover);
            } else {
                System.out.println("Geen brouwer gevonden met ID " + id + ".");
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    // Eén brouwer verwijderen
    public void delete(int id) {
        System.out.println("--------Delete--------");
        System.out.println("ID: " + id);

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BREWER_SQL)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Brouwer met ID " + id + " is verwijderd.");
            } else {
                System.out.println("⚠️ Geen brouwer gevonden met ID " + id + ".");
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    // Meerdere brouwers verwijderen
    public void deleteMultiple(int... ids) {
        System.out.println("--------DeleteMultiple--------");

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BREWER_SQL)) {

            for (int id : ids) {
                preparedStatement.setInt(1, id);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Brouwer met ID " + id + " is verwijderd.");
                } else {
                    System.out.println("Geen brouwer gevonden met ID " + id + ".");
                }
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = e.getCause();
                while (t != null) {
                    System.err.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
