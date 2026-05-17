package se2203b.assignments.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se2203b.assignments.domain.Flight;

public interface FlightRepository extends JpaRepository<Flight, String> {
}
