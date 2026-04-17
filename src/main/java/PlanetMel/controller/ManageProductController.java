package PlanetMel.controller;

import PlanetMel.PlanetMelApplication;
import PlanetMel.domain.Product;
import PlanetMel.service.ProductService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ManageProductController {

    @Autowired
    private ProductService productService;

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Long> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, String> colDescription;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        loadProducts();
    }

    private void loadProducts() {
        productTable.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
    }

    @FXML
    public void onDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a product to delete.");
            return;
        }
        productService.deleteProduct(selected.getId());
        statusLabel.setText("Product deleted successfully.");
        loadProducts();
    }

    @FXML
    public void onRefresh() {
        loadProducts();
        statusLabel.setText("Refreshed.");
    }
    @FXML
    public void onEditProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a product to edit.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    PlanetMelApplication.class.getResource("/PlanetMel/views/addNewProduct-view.fxml"));
            loader.setControllerFactory(PlanetMelApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            AddNewProductController controller = loader.getController();
            controller.prefillForEdit(selected);
            Stage stage = new Stage();
            stage.setTitle("Edit Product");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}