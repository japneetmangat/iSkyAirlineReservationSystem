package se2203b.assignments.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se2203b.assignments.SceneNavigator;
import se2203b.assignments.domain.Role;
import se2203b.assignments.domain.UserAccount;
import se2203b.assignments.service.UserAccountService;
import se2203b.assignments.service.AppSession;

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

    private final UserAccountService userAccountService ;

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
                // Generic message reduces account enumeration risk.
                setStatus("Login failed: Invalid user ID or password.");
                pwd.clear();
                return;
            }

            // Remember username (optional UX)
            boolean remember = chkRememberUser != null && chkRememberUser.isSelected();
            prefs.putBoolean(PREF_REMEMBER, remember);
            if (remember) prefs.put(PREF_USERNAME, username);
            else prefs.remove(PREF_USERNAME);

            AppSession.setCurrentUser(user);
            pwd.clear();

            // MUST CHANGE PASSWORD?
            if (user.isMustChangePassword()) {
                SceneNavigator.setRoot(btnLogin.getScene(), "/se2203b/assignments/views/ChangePassword.fxml", "iSky - Change password form", 500.0, 550.0);
           return;

            }
            // MASTER goes to initial provisioning screen
            if (user.getRole() == Role.MASTER) {
                SceneNavigator.setRoot(btnLogin.getScene(), "/se2203b/assignments/views/MasterProvision.fxml", "iSky - User Role Creations", 1000.0, 740.0);

            }else {
                // All other roles go to their normal main menu
                SceneNavigator.setRoot(btnLogin.getScene(), "/se2203b/assignments/views/Main.fxml", "iSky Portal",1000.0, 740.0);
            }
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
