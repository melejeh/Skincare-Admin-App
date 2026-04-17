package PlanetMel.controller;

import PlanetMel.service.UserAccountService;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import PlanetMel.SceneNavigator;
import PlanetMel.domain.UserAccount;
import PlanetMel.service.AppSession;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype") // a new controller per FXML load
public class ChangePasswordController implements Initializable {

    @FXML private Label lblUserInfo;

    @FXML private PasswordField pwdCurrent;
    @FXML private PasswordField pwdNew;
    @FXML private PasswordField pwdConfirm;

    @FXML private Label lblStatus;
    @FXML private Button btnChange;

    private final UserAccountService userAccountService ;

    @Autowired
    public ChangePasswordController(UserAccountService service) {
        this.userAccountService = service;
    }
    private final BooleanProperty busy = new SimpleBooleanProperty(false);

    private static final int MIN_LEN = 12;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setStatus("");

        UserAccount u = AppSession.getCurrentUser();
        if (u == null) {
            // Not logged in -> go back to login
            SceneNavigator.setRoot(btnChange.getScene(), "/PlanetMel/views/Login.fxml", "iSky - Authentication", 400.0, 500.0);
            return;
        }

        if (lblUserInfo != null) {
            lblUserInfo.setText("Signed in as: " + u.getUsername() + " (" + u.getRole() + ")");
        }

        BooleanBinding invalid = Bindings.createBooleanBinding(
                () -> nte(pwdCurrent.getText()).isEmpty()
                        || nte(pwdNew.getText()).isEmpty()
                        || nte(pwdConfirm.getText()).isEmpty(),
                pwdCurrent.textProperty(),
                pwdNew.textProperty(),
                pwdConfirm.textProperty()
        );

        btnChange.disableProperty().bind(invalid.or(busy));
    }

    @FXML
    private void handleChangePassword() {
        UserAccount u = AppSession.getCurrentUser();
        if (u == null) {
            SceneNavigator.setRoot(btnChange.getScene(), "/PlanetMel/views/Login.fxml","iSky - Authentication", 400.0, 500.0);
            return;
        }

        String current = pwdCurrent.getText();
        String nw = pwdNew.getText();
        String confirm = pwdConfirm.getText();

        if (!nw.equals(confirm)) {
            setStatus("New passwords do not match.");
            pwdNew.clear();
            pwdConfirm.clear();
            return;
        }
        if (nw.length() < MIN_LEN) {
            setStatus("New password must be at least " + MIN_LEN + " characters.");
            return;
        }

        busy.set(true);
        try {

            userAccountService.changePassword(u.getUsername(), current, nw);

            AppSession.clear();
            SceneNavigator.setRoot(btnChange.getScene(), "/PlanetMel/views/Login.fxml", "iSky - Change password form", 500.0, 550.0);
        } catch (IllegalArgumentException badCurrent) {
            setStatus("Current password is incorrect.");
            pwdCurrent.clear();
        } catch (Exception ex) {
            setStatus("Unable to change password. Please try again.");
            pwdCurrent.clear();
            pwdNew.clear();
            pwdConfirm.clear();
        } finally {
            busy.set(false);
        }
    }

    @FXML
    private void handleLogout() {
        AppSession.clear();
        SceneNavigator.setRoot(btnChange.getScene(), "/PlanetMel/views/Login.fxml", "iSky - Authentication", 400.0, 500.0);
    }

    private void setStatus(String msg) {
        if (lblStatus != null) lblStatus.setText(msg == null ? "" : msg);
    }

    private static String nte(String s) {
        return s == null ? "" : s;
    }
}
