package se2203b.assignments.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se2203b.assignments.domain.Flight;
import se2203b.assignments.service.FlightService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class AddNewFlightController implements Initializable {

    @FXML private TextField txtFlightNumber;
    @FXML private TextField txtOrigin;
    @FXML private TextField txtDestination;
    @FXML private TextField txtDepartureTime;
    @FXML private TextField txtArrivalTime;
    @FXML private DatePicker dpLaunchDate;
    @FXML private Label lblStatus;

    private final FlightService flightService;
    private ManageFlightController manageFlightController;
    private Flight editingFlight;  // null when adding new

    @Autowired
    public AddNewFlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setManageFlightController(ManageFlightController controller) {
        this.manageFlightController = controller;
    }

    /** Populate form fields for editing an existing flight. */
    public void setFlight(Flight flight) {
        this.editingFlight = flight;
        txtFlightNumber.setText(flight.getFlightNumber());
        txtFlightNumber.setDisable(true);   // flight number is the PK, cannot be changed
        txtOrigin.setText(flight.getOrigin());
        txtDestination.setText(flight.getDestination());
        txtDepartureTime.setText(flight.getDepartureTime());
        txtArrivalTime.setText(flight.getArrivalTime());
        dpLaunchDate.setValue(flight.getLaunchDate());
    }

    @FXML
    private void handleSave() {
        String flightNumber = txtFlightNumber.getText().trim();
        String origin = txtOrigin.getText().trim();
        String destination = txtDestination.getText().trim();
        String departureTime = txtDepartureTime.getText().trim();
        String arrivalTime = txtArrivalTime.getText().trim();
        LocalDate launchDate = dpLaunchDate.getValue();

        if (flightNumber.isEmpty() || origin.isEmpty() || destination.isEmpty()
                || departureTime.isEmpty() || arrivalTime.isEmpty() || launchDate == null) {
            lblStatus.setText("All fields are required.");
            return;
        }

        if (editingFlight == null && flightService.flightExists(flightNumber)) {
            lblStatus.setText("Flight number already exists.");
            return;
        }

        Flight flight;
        if (editingFlight != null) {
            editingFlight.setOrigin(origin);
            editingFlight.setDestination(destination);
            editingFlight.setDepartureTime(departureTime);
            editingFlight.setArrivalTime(arrivalTime);
            editingFlight.setLaunchDate(launchDate);
            flight = editingFlight;
        } else {
            flight = new Flight(flightNumber, origin, destination, departureTime, arrivalTime, launchDate);
        }

        flightService.saveFlight(flight);

        if (manageFlightController != null) {
            manageFlightController.refreshTable();
        }

        txtFlightNumber.getScene().getWindow().hide();
    }

    @FXML
    private void handleCancel() {
        txtFlightNumber.getScene().getWindow().hide();
    }
}
