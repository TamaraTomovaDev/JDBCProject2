package repositories;

import models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BeerRepository {
    public List<Category> read() {
        List<Category> results = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/thebelgianbrewerydb", "intec", "intec-123"
            );
            System.out.println("Connection to DB in made");
            Statement statement = connection.createStatement();
            String query = "SELECT Id, Name FROM beers";

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("Id"));
                System.out.println(" Name: " + resultSet.getString("Name"));
            }
        } catch (SQLException sqlException) {
            System.err.println("SQL Exeprion: " + sqlException.getMessage());
        }
        return results;
    }
}
