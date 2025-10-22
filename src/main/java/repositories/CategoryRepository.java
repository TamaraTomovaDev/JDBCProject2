package repositories;

import models.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    public List<Category> read() {
        List<Category> results = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/thebelgianbrewerydb", "intec", "intec-123"
            );
            System.out.println("Connection to DB in made");
            Statement statement = connection.createStatement();
            String query = "SELECT Id, Category FROM categories";

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("Id"));
                System.out.println(" Category: " + resultSet.getString("Category"));
            }
        } catch (SQLException sqlException) {
            System.err.println("SQL Exeprion: " + sqlException.getMessage());
        }
        return results;
    }
}
