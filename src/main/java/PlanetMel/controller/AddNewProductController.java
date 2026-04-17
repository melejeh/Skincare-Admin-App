package PlanetMel.controller;

import PlanetMel.domain.Product;
import PlanetMel.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AddNewProductController {

    @Autowired
    private ProductService productService;

    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField categoryField;
    @FXML private TextField imageUrlField;
    @FXML private TextArea descriptionArea;
    @FXML private Label statusLabel;

    private Long editingId = null;

    public void prefillForEdit(Product product) {
        editingId = product.getId();
        nameField.setText(product.getName());
        categoryField.setText(product.getCategory());
        priceField.setText(String.valueOf(product.getPrice()));
        imageUrlField.setText(product.getImageUrl() != null ? product.getImageUrl() : "");
        descriptionArea.setText(product.getDescription() != null ? product.getDescription() : "");
        statusLabel.setText("Editing: " + product.getName());
    }

    @FXML
    public void onAddProduct() {
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String description = descriptionArea.getText().trim();
        String imageUrl = imageUrlField.getText().trim();
        String priceText = priceField.getText().trim();

        if (name.isEmpty() || category.isEmpty() || priceText.isEmpty()) {
            statusLabel.setText("Please fill in name, category and price.");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            if (editingId != null) {
                productService.updateProduct(editingId, name, description, price, category,imageUrl);
                statusLabel.setText("Product updated successfully!");
                editingId = null;
            } else {
                productService.addProduct(name, description, price, category, imageUrl);
                statusLabel.setText("Product added successfully!");
            }
            onClear();
        } catch (NumberFormatException e) {
            statusLabel.setText("Price must be a valid number.");
        }
    }

    @FXML
    public void onClear() {
        editingId = null;
        nameField.clear();
        priceField.clear();
        categoryField.clear();
        imageUrlField.clear();
        descriptionArea.clear();
        statusLabel.setText("");
    }
}
