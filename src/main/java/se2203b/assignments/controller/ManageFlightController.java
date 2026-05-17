package se2203b.assignments.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se2203b.assignments.domain.Flight;
import se2203b.assignments.iSkyApplication;
import se2203b.assignments.service.FlightService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ManageFlightController implements Initializable {

    @FXML private TableView<Flight> tableFlights;
    @FXML private TableColumn<Flight, String> colFlightNumber;
    @FXML private TableColumn<Flight, String> colOrigin;
    @FXML private TableColumn<Flight, String> colDestination;
    @FXML private TableColumn<Flight, String> colDepartureTime;
    @FXML private TableColumn<Flight, String> colArrivalTime;
    @FXML private TableColumn<Flight, LocalDate> colLaunchDate;

    private final FlightService flightService;

    @Autowired
    public ManageFlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colFlightNumber.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        colOrigin.setCellValueFactory(new PropertyValueFactory<>("origin"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));

        colDepartureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        colDepartureTime.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : formatTime(item));
            }
        });

        colArrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        colArrivalTime.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : formatTime(item));
            }
        });

        colLaunchDate.setCellValueFactory(new PropertyValueFactory<>("launchDate"));
        colLaunchDate.setCellFactory(col -> new TableCell<>() {
            private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy");
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.format(fmt));
            }
        });

        // Double-click a row to edit
        tableFlights.setRowFactory(tv -> {
            TableRow<Flight> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    openFlightDialog(row.getItem());
                }
            });
            return row;
        });

        refreshTable();
    }

    private String formatTime(String time) {
        try {
            LocalTime lt = LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm"));
            int hour = lt.getHour();
            int minute = lt.getMinute();
            String ampm = hour < 12 ? "a.m." : "p.m.";
            int displayHour = hour % 12;
            if (displayHour == 0) displayHour = 12;
            return String.format("%d:%02d %s", displayHour, minute, ampm);
        } catch (Exception e) {
            return time;
        }
    }

    public void refreshTable() {
        tableFlights.setItems(FXCollections.observableArrayList(flightService.getAllFlights()));
    }

    @FXML
    private void handleAddNewFlight() {
        openFlightDialog(null);
    }

    private void openFlightDialog(Flight flight) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/se2203b/assignments/views/addNewFlight-view.fxml"));
            loader.setControllerFactory(iSkyApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            AddNewFlightController controller = loader.getController();
            controller.setManageFlightController(this);
            if (flight != null) {
                controller.setFlight(flight);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(flight == null ? "Add New Airline Flight" : "Edit Airline Flight");
            stage.getIcons().add(new Image("file:src/main/resources/se2203b/assignments/images/WesternLogo.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemove() {
        Flight selected = tableFlights.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a flight to remove.").showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Remove flight " + selected.getFlightNumber() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirm Removal");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                flightService.deleteFlight(selected.getFlightNumber());
                refreshTable();
            }
        });
    }

    @FXML
    private void handleExit() {
        tableFlights.getScene().getWindow().hide();
    }
}
