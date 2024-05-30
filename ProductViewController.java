package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class ProductViewController {
    @FXML private TilePane tilePane;

    private ProductApp productApp;

    public void setProductApp(ProductApp productApp) {
        this.productApp = productApp;
        displayProductsInTilePane();
    }

    @FXML
    private void initialize() {
        // Nothing to do here
    }

    @FXML
    private void handleRefreshProducts() {
        productApp.loadProductsFromDatabase();
        tilePane.getChildren().clear();
        displayProductsInTilePane();
    }

    private void displayProductsInTilePane() {
        for (Product product : productApp.getProductList()) {
            // Create a product card with a delete button
            VBox productCardWithDeleteButton = createProductCardWithDeleteButton(product);
            tilePane.getChildren().add(productCardWithDeleteButton);
        }
    }

    private VBox createProductCardWithDeleteButton(Product product) {
        VBox card = new VBox();
        card.setStyle("-fx-border-color: black; -fx-background-color: white; -fx-padding: 10;");
        card.setPrefSize(180, 350);

        ImageView productImage = new ImageView(new Image(product.getImagePath()));
        productImage.setFitWidth(160);
        productImage.setFitHeight(120);

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        nameLabel.setWrapText(true);

        Label priceLabel = new Label(String.format("\nPrice: $%.2f", product.getPrice()));
        priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label descriptionHeadingLabel = new Label("Description:");
        descriptionHeadingLabel.setStyle("-fx-font-weight: bold;");
        Label descriptionLabel = new Label("\t\t    "+ product.getDescription());
        descriptionLabel.setWrapText(true);

        Button detailsButton = new Button("See details");
        detailsButton.setStyle("-fx-background-color: #ff9900; -fx-text-fill: white;");

        card.getChildren().addAll(productImage, nameLabel, priceLabel, descriptionHeadingLabel, descriptionLabel, detailsButton);

        HBox deleteButtonBox = new HBox();
        deleteButtonBox.setStyle("-fx-alignment: center; -fx-padding: 10;");
        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            handleDeleteProduct(product, card);
        });
        deleteButtonBox.getChildren().add(deleteButton);

        VBox cardWithDeleteButton = new VBox(card, deleteButtonBox);
        return cardWithDeleteButton;
    }

    private void handleDeleteProduct(Product product, VBox productCard) {
        // Remove the product from the database
        productApp.deleteProductFromDatabase(product);
        // Remove the product card from the TilePane
        tilePane.getChildren().remove(productCard);
    }
}
