package PlanetMel.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import PlanetMel.domain.UserAccount;
import PlanetMel.PlanetMelApplication;
import PlanetMel.service.AppSession;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class MainController implements Initializable {

    @FXML private Label lblWelcome;
    @FXML private Label lblRole;
    @FXML private Label lblBody;
    @FXML private MenuItem mnuLogout;
    @FXML private MenuItem mnuExit;
    @FXML private MenuItem logoutMenuItem;
    @FXML private Menu userMenuItem;
    @FXML private MenuItem changePasswordMenuItem;
    @FXML private Menu menuBusiness01;
    @FXML private Menu menuBusiness02;
    @FXML private Menu menuBusiness03;
    @FXML private Menu menuBusiness04;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserAccount u = AppSession.getCurrentUser();
        if (u == null) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        PlanetMelApplication.class.getResource("/PlanetMel/views/Login.fxml"));
                loader.setControllerFactory(PlanetMelApplication.getSpringContext()::getBean);
                Parent root = loader.load();
                mnuLogout.getParentPopup().getOwnerWindow().getScene().setRoot(root);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        lblWelcome.setText("Welcome, " + u.getUsername());
        lblRole.setText("Role: " + u.getRole());
        userMenuItem.setVisible(true);
        userMenuItem.setText(u.getUsername());
        logoutMenuItem.setDisable(false);

        menuBusiness01.setVisible(true);
        menuBusiness02.setVisible(true);
        menuBusiness03.setVisible(true);
        menuBusiness04.setVisible(true);
    }

    @FXML
    private void handleLogout() {
        AppSession.clear();
        try {
            FXMLLoader loader = new FXMLLoader(
                    PlanetMelApplication.class.getResource("/PlanetMel/views/Login.fxml"));
            loader.setControllerFactory(PlanetMelApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            lblWelcome.getScene().setRoot(root);
            Stage stage = (Stage) lblWelcome.getScene().getWindow();
            stage.setMinWidth(400.0);
            stage.setMinHeight(500.0);
            stage.setMaxWidth(400.0);
            stage.setMaxHeight(500.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        lblWelcome.getScene().getWindow().hide();
    }

    @FXML
    private void handleBusinessManagerTask() {
        lblBody.setText("Coming soon...");
    }

    @FXML
    public void handleManageProducts() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    PlanetMelApplication.class.getResource("/PlanetMel/views/manageProducts-view.fxml"));
            loader.setControllerFactory(PlanetMelApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Manage Products");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            lblBody.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddNewProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    PlanetMelApplication.class.getResource("/PlanetMel/views/addNewProduct-view.fxml"));
            loader.setControllerFactory(PlanetMelApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Product");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            lblBody.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void changePassword() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    PlanetMelApplication.class.getResource("/PlanetMel/views/ChangePassword.fxml"));
            loader.setControllerFactory(PlanetMelApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            lblWelcome.getScene().setRoot(root);
            Stage stage = (Stage) lblWelcome.getScene().getWindow();
            stage.setMinWidth(500.0);
            stage.setMinHeight(550.0);
            stage.setMaxWidth(500.0);
            stage.setMaxHeight(550.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
