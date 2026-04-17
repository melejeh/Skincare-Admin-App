package PlanetMel.controller;

import PlanetMel.PlanetMelApplication;
import PlanetMel.service.UserAccountService;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import PlanetMel.domain.UserAccount;
import PlanetMel.service.AppSession;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

@Component
public class LoginController implements Initializable {

    @FXML private TextField txtUser;
    @FXML private PasswordField pwd;
    @FXML private CheckBox chkRememberUser;
    @FXML private Label lblStatus;
    @FXML private Button btnLogin;

    private final UserAccountService userAccountService;

    @Autowired
    public LoginController(UserAccountService service) {
        this.userAccountService = service;
    }

    private final BooleanProperty busy = new SimpleBooleanProperty(false);
    private final Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
    private static final String PREF_REMEMBER = "rememberUser";
    private static final String PREF_USERNAME = "lastUsername";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setStatus("");
        AppSession.clear();

        BooleanBinding invalid = Bindings.createBooleanBinding(
                () -> nte(txtUser.getText()).trim().isEmpty() || nte(pwd.getText()).isEmpty(),
                txtUser.textProperty(),
                pwd.textProperty()
        );
        btnLogin.disableProperty().bind(invalid.or(busy));

        boolean remember = prefs.getBoolean(PREF_REMEMBER, false);
        if (chkRememberUser != null) chkRememberUser.setSelected(remember);
        if (remember) {
            String last = prefs.get(PREF_USERNAME, "");
            txtUser.setText(last);
            if (!last.isBlank()) pwd.requestFocus();
        }
    }

    @FXML
    private void handleLogin() {
        busy.set(true);
        try {
            setStatus("");
            String username = txtUser.getText().trim();
            String password = pwd.getText();

            UserAccount user = userAccountService.authenticate(username, password);
            if (user == null) {
                setStatus("Login failed: Invalid user ID or password.");
                pwd.clear();
                return;
            }

            boolean remember = chkRememberUser != null && chkRememberUser.isSelected();
            prefs.putBoolean(PREF_REMEMBER, remember);
            if (remember) prefs.put(PREF_USERNAME, username);
            else prefs.remove(PREF_USERNAME);

            AppSession.setCurrentUser(user);
            pwd.clear();

            if (user.isMustChangePassword()) {
                FXMLLoader loader = new FXMLLoader(
                        PlanetMelApplication.class.getResource("/PlanetMel/views/ChangePassword.fxml"));
                loader.setControllerFactory(PlanetMelApplication.getSpringContext()::getBean);
                Parent root = loader.load();
                btnLogin.getScene().setRoot(root);
                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.setMinWidth(1000.0);
                stage.setMinHeight(740.0);
                stage.setMaxWidth(Double.MAX_VALUE);
                stage.setMaxHeight(Double.MAX_VALUE);
                stage.setResizable(true);
                return;
            }

            FXMLLoader loader = new FXMLLoader(
                    PlanetMelApplication.class.getResource("/PlanetMel/views/Main.fxml"));
            loader.setControllerFactory(PlanetMelApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            btnLogin.getScene().setRoot(root);
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setMinWidth(1000.0);
            stage.setMinHeight(740.0);
            stage.setMaxWidth(Double.MAX_VALUE);
            stage.setMaxHeight(Double.MAX_VALUE);
            stage.setResizable(true);

        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Error: " + e.getMessage());
        } finally {
            busy.set(false);
        }
    }

    @FXML
    private void handleExit() {
        if (btnLogin != null && btnLogin.getScene() != null && btnLogin.getScene().getWindow() != null) {
            btnLogin.getScene().getWindow().hide();
        }
    }

    private void setStatus(String msg) {
        if (lblStatus != null) lblStatus.setText(msg == null ? "" : msg);
    }

    private static String nte(String s) {
        return s == null ? "" : s;
    }
}