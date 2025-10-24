package repositories;

import models.Beer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BeerRepository {

    private static final String INSERT_BEER_SQL = "INSERT INTO beers (Name, BrewerId, CategoryId, Price, Stock, Alcohol, Version, Image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_BEER_SQL = "UPDATE beers SET Name = ?, BrewerId = ?, CategoryId = ?, Price = ?, Stock = ?, Alcohol = ?, Version = ?, Image = ? WHERE Id = ?";
    private static final String DELETE_BEER_SQL = "DELETE FROM beers WHERE Id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM beers";

    public List<Beer> read() {
        List<Beer> results = new ArrayList<>();

        System.out.println("--------Read--------");

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb", "intec", "intec-123");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {

            while (resultSet.next()) {
                Beer beer = new Beer();
                beer.setId(resultSet.getInt("Id"));
                beer.setName(resultSet.getString("Name"));
                beer.setBrewerId(resultSet.getInt("BrewerId"));
                beer.setCategoryId(resultSet.getInt("CategoryId"));
                beer.setPrice(resultSet.getFloat("Price"));
                beer.setStock(resultSet.getInt("Stock"));
                beer.setAlcohol(resultSet.getFloat("Alcohol"));
                beer.setVersion(resultSet.getInt("Version"));
                beer.setImage(resultSet.getBlob("Image"));

                results.add(beer);
                System.out.println("→ " + beer.getName() + " (ID: " + beer.getId() + ")");
            }

            System.out.println("Totaal aantal bieren gevonden: " + results.size());

        } catch (SQLException e) {
            printSQLException(e);
        }

        return results;
    }

    public void create(String name, int brewerId, int categoryId, float price, int stock, float alcohol, int version, Blob image) {
        System.out.println("--------Create Beer--------");
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BEER_SQL)) {

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, brewerId);
            preparedStatement.setInt(3, categoryId);
            preparedStatement.setFloat(4, price);
            preparedStatement.setInt(5, stock);
            preparedStatement.setFloat(6, alcohol);
            preparedStatement.setInt(7, version);
            preparedStatement.setBlob(8, image);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Bier '" + name + "' succesvol toegevoegd.");
            } else {
                System.out.println("Toevoegen mislukt voor bier '" + name + "'.");
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void createMultiple(List<Object[]> beers) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BEER_SQL)) {

            connection.setAutoCommit(false);
            for (Object[] b : beers) {
                preparedStatement.setString(1, (String) b[0]);      // name
                preparedStatement.setInt(2, (int) b[1]);             // brewerId
                preparedStatement.setInt(3, (int) b[2]);             // categoryId
                preparedStatement.setFloat(4, (float) b[3]);         // price
                preparedStatement.setInt(5, (int) b[4]);             // stock
                preparedStatement.setFloat(6, (float) b[5]);         // alcohol
                preparedStatement.setInt(7, (int) b[6]);             // version
                preparedStatement.setBlob(8, (Blob) b[7]);           // image
                preparedStatement.addBatch();
            }

            int[] result = preparedStatement.executeBatch();
            connection.commit();
            System.out.println("Batch succesvol uitgevoerd. Aantal toegevoegde bieren: " + result.length);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void update(int id, String name, int brewerId, int categoryId, float price, int stock, float alcohol, int version, Blob image) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BEER_SQL)) {

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, brewerId);
            preparedStatement.setInt(3, categoryId);
            preparedStatement.setFloat(4, price);
            preparedStatement.setInt(5, stock);
            preparedStatement.setFloat(6, alcohol);
            preparedStatement.setInt(7, version);
            preparedStatement.setBlob(8, image);
            preparedStatement.setInt(9, id);

            System.out.println("--------Bier wordt aangepast--------");
            System.out.println("ID: " + id);
            System.out.println("Nieuwe gegevens:");
            System.out.println("Naam: " + name);
            System.out.println("Brouwer ID: " + brewerId);
            System.out.println("Categorie ID: " + categoryId);
            System.out.println("Prijs: " + price);
            System.out.println("Voorraad: " + stock);
            System.out.println("Alcohol: " + alcohol);
            System.out.println("Versie: " + version);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bier succesvol aangepast.");
            } else {
                System.out.println("Geen bier gevonden met ID: " + id);
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void delete(int id) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BEER_SQL)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Bier met ID " + id + " succesvol verwijderd.");
            } else {
                System.out.println("Geen bier gevonden met ID " + id + ". Niets verwijderd.");
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void deleteMultiple(int... ids) {
        System.out.println("--------DeleteMultiple--------");
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/thebelgianbrewerydb?useSSL=false", "intec", "intec-123");
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BEER_SQL)) {
            int successCount = 0;
            for (int id : ids) {
                preparedStatement.setInt(1, id);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Bier met ID " + id + " is verwijderd.");
                    successCount++;
                } else {
                    System.out.println("Geen bier gevonden met ID " + id + ".");
                }
                System.out.println("Aantal succesvol verwijderde bieren: " + successCount + " van " + ids.length);
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
