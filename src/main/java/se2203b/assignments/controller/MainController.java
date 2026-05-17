package se2203b.assignments.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se2203b.assignments.SceneNavigator;
import se2203b.assignments.domain.Role;
import se2203b.assignments.domain.UserAccount;
import se2203b.assignments.iSkyApplication;
import se2203b.assignments.service.AppSession;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype") // a new controller per FXML load
public class MainController implements Initializable {

    @FXML private Label lblWelcome;
    @FXML private Label lblRole;
    @FXML private Label lblBody;

    @FXML private MenuItem mnuLogout;
    @FXML
    private MenuItem mnuViewReports;
    @FXML
    private Menu userMenuItem;
    @FXML
    private MenuItem mnuCreateAccount;
    @FXML
    private MenuItem changePasswordMenuItem;
    @FXML
    private MenuItem aboutusMenuItem;
    @FXML
    private Menu aboutMenu;

    @FXML
    private MenuItem mnuRequestAccount;
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private MenuItem mnuExit;
    @FXML
    private Menu menuBusiness01;
    @FXML
    private Menu menuBusiness02;
    @FXML
    private Menu menuBusiness03;
    @FXML
    private Menu menuBusiness04;
    @FXML
    private Menu menuNetwork04;
    @FXML
    private Menu menuNetwork03;
    @FXML
    private Menu menuNetwork02;
    @FXML
    private Menu menuNetwork01;
    @FXML
    private Menu menuLob01;
    @FXML
    private Menu menuLob03;
    @FXML
    private Menu menuLob02;
    @FXML
    private Menu menuLob04;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserAccount u = AppSession.getCurrentUser();
        if (u == null) {
            SceneNavigator.setRoot(mnuLogout.getParentPopup().getOwnerWindow().getScene(), "/se2203b/assignments/views/Login.fxml", "iSky - Authentication",400.0, 500.0);
            return;
        }

        lblWelcome.setText("Welcome, " + u.getUsername());
        lblRole.setText("Role: " + u.getRole());

        applyRoleVisibility(u.getRole());

        ImageView face = new ImageView(new Image("file:src/main/resources/se2203b/assignments/images/UserIcon.png",20,20,true,true));
        userMenuItem.setGraphic(face);
        userMenuItem.setVisible(true);
        logoutMenuItem.setDisable(false);
        userMenuItem.setText(u.getUsername());
    }

    private void applyRoleVisibility(Role role) {
        // UI tailoring only; real authorization should also be enforced in service methods.
        menuBusiness01.setVisible(role == Role.BUSINESS_MANAGER);
        menuBusiness02.setVisible(role == Role.BUSINESS_MANAGER);
        menuBusiness03.setVisible(role == Role.BUSINESS_MANAGER);
        menuBusiness04.setVisible(role == Role.BUSINESS_MANAGER);

        menuNetwork01.setVisible(role == Role.NETWORK_ADMINISTRATOR);
        menuNetwork02.setVisible(role == Role.NETWORK_ADMINISTRATOR);
        menuNetwork03.setVisible(role == Role.NETWORK_ADMINISTRATOR);
        menuNetwork04.setVisible(role == Role.NETWORK_ADMINISTRATOR);

        menuLob01.setVisible(role == Role.LINE_OF_BUSINESS_EXECUTIVE);
        menuLob02.setVisible(role == Role.LINE_OF_BUSINESS_EXECUTIVE);
        menuLob03.setVisible(role == Role.LINE_OF_BUSINESS_EXECUTIVE);
        menuLob04.setVisible(role == Role.LINE_OF_BUSINESS_EXECUTIVE);
    }

    @FXML
    private void handleLogout() {
        AppSession.clear();
        SceneNavigator.setRoot(lblWelcome.getScene(), "/se2203b/assignments/views/Login.fxml", "iSky - Authentication", 400.0, 500.0);
    }

    @FXML
    private void handleExit() {
        lblWelcome.getScene().getWindow().hide();
    }

    @FXML
    private void handleBusinessManagerTask() {
        lblBody.setText("Business Manager Functionalities (stub).");
    }

    @FXML
    public void handleManageFlight() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/se2203b/assignments/views/manageFlights-view.fxml"));
            loader.setControllerFactory(iSkyApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Airline Flights");
            stage.getIcons().add(new Image("file:src/main/resources/se2203b/assignments/images/WesternLogo.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNetworkAdministratorTask() {
        lblBody.setText("Network Admin Functionalities (stub).");
    }

    @FXML
    private void handleLOBTask() {
        lblBody.setText("LOB Exec Functionalities (stub).");
    }

    @FXML
    public void showAbout() throws Exception {
       FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/se2203b/assignments/views/about-view.fxml"));
       loader.setControllerFactory(iSkyApplication.getSpringContext()::getBean); // <-- uses springContext
       Parent root = loader.load();


        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:se2203b/assignments/images/WesternLogo.png"));
        stage.setTitle("About Us");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void changePassword() {
        SceneNavigator.setRoot(mnuLogout.getParentPopup().getOwnerWindow().getScene(), "/se2203b/assignments/views/ChangePassword.fxml", "iSky - Change password form", 500.0, 550.0);

    }
}
