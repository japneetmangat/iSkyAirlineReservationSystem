package se2203b.assignments.service;

import org.springframework.stereotype.Service;
import se2203b.assignments.domain.Flight;
import se2203b.assignments.repo.FlightRepository;

import java.util.List;

@Service
public class FlightService {

    private final FlightRepository repo;

    public FlightService(FlightRepository repo) {
        this.repo = repo;
    }

    public List<Flight> getAllFlights() {
        return repo.findAll();
    }

    public Flight saveFlight(Flight flight) {
        return repo.save(flight);
    }

    public void deleteFlight(String flightNumber) {
        repo.deleteById(flightNumber);
    }

    public boolean flightExists(String flightNumber) {
        return repo.existsById(flightNumber);
    }
}
