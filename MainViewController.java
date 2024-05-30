package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainViewController {
    @FXML private TextField nameInput;
    @FXML private TextField priceInput;
    @FXML private TextField descriptionInput;

    private ProductApp productApp;
    private File selectedImageFile;

    public void setProductApp(ProductApp productApp) {
        this.productApp = productApp;
    }

    @FXML
    private void handleBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        selectedImageFile = fileChooser.showOpenDialog(productApp.getPrimaryStage());
    }

    @FXML
    private void handleAddProduct() {
        String name = nameInput.getText();
        double price = Double.parseDouble(priceInput.getText());
        String description = descriptionInput.getText();

        if (selectedImageFile != null) {
            Product product = new Product(0, name, price, description, selectedImageFile.toURI().toString());
            productApp.saveProductToDatabase(product);

            // Clear the input fields
            nameInput.clear();
            priceInput.clear();
            descriptionInput.clear();
            selectedImageFile = null;
        }
    }

    @FXML
    private void handleDisplayProducts() {
        productApp.displayProducts();
    }

    @FXML
    private void handleDeleteAllProducts() {
        productApp.deleteAllProductsFromDatabase();
    }
}
