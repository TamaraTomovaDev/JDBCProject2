package repositories;

import models.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private static final String INSERT_CATEGORY_SQL = "INSERT INTO categories (Category) VALUES (?)";
    private static final String UPDATE_CATEGORY_SQL = "UPDATE categories SET Category = ? WHERE Category = ?";
    private static final String DELETE_CATEGORY_SQL = "DELETE FROM categories WHERE Category = ?";
    // Alle categorieën lezen
    public List<Category> read() {
        System.out.println("--------Read-------");
        List<Category> results = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/thebelgianbrewerydb", "intec", "intec-123"
            );
            Statement statement = connection.createStatement();
            String query = "SELECT Id, Category FROM categories";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("Id"));
                System.out.println(" Categorie: " + resultSet.getString("Category"));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }return results;
    }

    // Een categorie toevoegen
    public void create(String title) {
        System.out.println("--------Create--------");
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY_SQL)) {

            preparedStatement.setString(1, title);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Categorie '" + title + "' succesvol toegevoegd.");
            } else {
                System.out.println("Toevoegen mislukt voor categorie '" + title + "'.");
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
    }


    // Meerdere categorieën toevoegen
    public void createMultiple(String... titles) {
        System.out.println("--------CreateMultiple--------");
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY_SQL)) {
            connection.setAutoCommit(false); // Start transactie
            for (String title : titles) {
                preparedStatement.setString(1, title);
                preparedStatement.addBatch();
                System.out.println("Categorie '" + title + "' toegevoegd.");
            }

            int[] results = preparedStatement.executeBatch(); // Voer batch uit
            connection.commit(); // Commit transactie
            System.out.println("Aantal toegevoegde categorieën: " + results.length);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    // Titel van een categorie wijzigen
    public void update(String newTitle, String oldTitle) {
        System.out.println("--------Update--------");
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CATEGORY_SQL)) {

            preparedStatement.setString(1, newTitle);
            preparedStatement.setString(2, oldTitle);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Categorie '" + oldTitle + "' is gewijzigd naar '" + newTitle + "'.");
            } else {
                System.out.println("Geen categorie gevonden met de titel '" + oldTitle + "'.");
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    // Categorie verwijderen op basis van de titel
    public void delete(String title) {
        System.out.println("--------Delete--------");

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CATEGORY_SQL)) {

            preparedStatement.setString(1, title);
            int rowsAffected = preparedStatement.executeUpdate(); // Verwijder de categorie

            if (rowsAffected > 0) {
                System.out.println("Categorie '" + title + "' succesvol verwijderd.");
            } else {
                System.out.println("Geen categorie gevonden met de titel '" + title + "'.");
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    // Meerdere categorieën verwijderen
    public void deleteMultiple(String... titles) {
        System.out.println("--------DeleteMultiple--------");

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CATEGORY_SQL)) {

            for (String title : titles) {
                preparedStatement.setString(1, title);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Categorie '" + title + "' is verwijderd.");
                } else {
                    System.out.println("Geen categorie gevonden met de titel '" + title + "'.");
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

