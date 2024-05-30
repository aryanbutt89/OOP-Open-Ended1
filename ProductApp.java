package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

public class ProductApp extends Application {

    private ObservableList<Product> productList;
    private Connection connection;
    private Stage primaryStage;

    public ObservableList<Product> getProductList() {
        return productList;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            MainViewController controller = loader.getController();
            controller.setProductApp(this);

            primaryStage.setTitle("Product Management");
            primaryStage.setScene(new Scene(root, 800, 600)); // Adjusted the scene size to 800x600 for better display
            primaryStage.show();

            connectToDatabase();
            productList = FXCollections.observableArrayList();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/product_db";
        String user = "root";
        String password = "S@fyan82";  // Replace with your MySQL password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveProductToDatabase(Product product) {
        String query = "INSERT INTO products (name, price, description, image_path) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setString(3, product.getDescription());
            statement.setString(4, product.getImagePath());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                product.setId(generatedKeys.getInt(1));
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayProducts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductView.fxml"));
            Parent root = loader.load();
            ProductViewController controller = loader.getController();
            controller.setProductApp(this);

            Stage displayStage = new Stage();
            displayStage.setTitle("Products");
            displayStage.setScene(new Scene(root, 600, 400));
            displayStage.show();

            loadProductsFromDatabase();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProductFromDatabase(Product product) {
        String query = "DELETE FROM products WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, product.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllProductsFromDatabase() {
        String query = "DELETE FROM products";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            productList.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadProductsFromDatabase() {
        String query = "SELECT * FROM products";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            productList.clear();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String description = resultSet.getString("description");
                String imagePath = resultSet.getString("image_path");

                Product product = new Product(id, name, price, description, imagePath);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
