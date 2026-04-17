package PlanetMel.controller;
import PlanetMel.service.UserAccountService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import PlanetMel.SceneNavigator;
import PlanetMel.domain.Role;
import PlanetMel.domain.UserAccount;
import PlanetMel.service.AppSession;
import javafx.scene.control.*;
import PlanetMel.service.PasswordUtil;

import java.net.URL;
import java.util.ResourceBundle;


@Component
@Scope("prototype") // a new controller per FXML load
public class MasterProvisionController  implements Initializable {

    @FXML private Label lblMasterInfo;

    @FXML private Label lblStatusBM;
    @FXML private Label lblStatusNA;
    @FXML private Label lblStatusLOB;

    @FXML private TextField txtUserBM;
    @FXML private PasswordField pwdBM;

    @FXML private TextField txtUserNA;
    @FXML private PasswordField pwdNA;

    @FXML private TextField txtUserLOB;
    @FXML private PasswordField pwdLOB;

    @FXML private Button btnCreateBM;
    @FXML private Button btnCreateNA;
    @FXML private Button btnCreateLOB;

    @FXML private Button btnContinue;
    @FXML private Label lblGlobalStatus;

    private final UserAccountService userAccountService;

    @Autowired
    public MasterProvisionController(UserAccountService service) {
        this.userAccountService = service;
    }
    private static final int MIN_LEN = 12;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserAccount current = AppSession.getCurrentUser();
        if (current == null || current.getRole() != Role.MASTER) {
            // Only master should see this; otherwise bounce to login
            if (btnContinue != null && btnContinue.getScene() != null) {
                SceneNavigator.setRoot(btnContinue.getScene(), "/PlanetMel/views/Login.fxml","iSky Authentication", 400.0, 500.0); // scene swap

            }
            return;
        }

        if (lblMasterInfo != null) {
            lblMasterInfo.setText("You are signed in as: " + current.getUsername() + " (MASTER)");
        }

        refreshStatus();
    }

    private void refreshStatus() {
        // Simple counts per role

            long bm = userAccountService.businessManagersCount();
            long na = userAccountService.networkAdministratorsCount();
            long lob = userAccountService.lineOfBusinessesCount();

            setRoleStatus(lblStatusBM, btnCreateBM, bm);
            setRoleStatus(lblStatusNA, btnCreateNA, na);
            setRoleStatus(lblStatusLOB, btnCreateLOB, lob);

            if (bm > 0 && na > 0 && lob > 0) {
                lblGlobalStatus.setText("All initial role accounts exist. You may continue to the main menu.");
                btnContinue.setDisable(false);
            } else {
                lblGlobalStatus.setText("At least one role is still missing an account.");
                btnContinue.setDisable(true);
            }

    }


    private void setRoleStatus(Label lbl, Button btn, long count) {
        if (lbl != null) lbl.setText(count > 0 ? "Exists (" + count + ")" : "Missing");
        if (btn != null) btn.setDisable(count > 0); // prevent multiple “initial” creations
    }

    @FXML
    private void handleCreateBM() { createRoleAccount(Role.BUSINESS_MANAGER, txtUserBM, pwdBM, lblStatusBM, btnCreateBM); }

    @FXML
    private void handleCreateNA() { createRoleAccount(Role.NETWORK_ADMINISTRATOR, txtUserNA, pwdNA, lblStatusNA, btnCreateNA); }

    @FXML
    private void handleCreateLOB() { createRoleAccount(Role.LINE_OF_BUSINESS_EXECUTIVE, txtUserLOB, pwdLOB, lblStatusLOB, btnCreateLOB); }

    private void createRoleAccount(Role role, TextField txtUser, PasswordField pwd, Label lblStatusRole, Button btnCreateRole) {
        lblGlobalStatus.setText("");

        String username = (txtUser == null ? "" : txtUser.getText()).trim();
        String password = (pwd == null ? "" : pwd.getText());

        if (username.isEmpty()) {
            lblGlobalStatus.setText("Please enter a username for " + role + ".");
            return;
        }
        if (password.length() < MIN_LEN) {
            lblGlobalStatus.setText("Temporary password must be at least " + MIN_LEN + " characters.");
            return;
        }

        try {

            String hash = PasswordUtil.hashPassword(password);

            userAccountService.createUser(username, password, role);



            if (txtUser != null) txtUser.clear();
            if (pwd != null) pwd.clear();

            refreshStatus();
        } catch (Exception ex) {
            lblGlobalStatus.setText("Unable to create account for " + role + ".");
        }
    }

    @FXML
    private void handleContinue() {
        SceneNavigator.setRoot(btnContinue.getScene(), "/PlanetMel/views/Main.fxml", "iSky - User Role Creations", 1000.0, 740.0);
    }
}