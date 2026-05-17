package se2203b.assignments.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Flight {

    @Id
    @Column(nullable = false, length = 20)
    private String flightNumber;

    @Column(nullable = false, length = 100)
    private String origin;

    @Column(nullable = false, length = 100)
    private String destination;

    @Column(nullable = false, length = 10)
    private String departureTime;   // stored as "H:mm", e.g. "11:10"

    @Column(nullable = false, length = 10)
    private String arrivalTime;     // stored as "H:mm", e.g. "18:30"

    @Column(nullable = false)
    private LocalDate launchDate;

    protected Flight() {}

    public Flight(String flightNumber, String origin, String destination,
                  String departureTime, String arrivalTime, LocalDate launchDate) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.launchDate = launchDate;
    }

    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public LocalDate getLaunchDate() { return launchDate; }

    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setLaunchDate(LocalDate launchDate) { this.launchDate = launchDate; }
}
