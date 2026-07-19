import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FlightManager {
    private Map<String, Flight> flights = new HashMap<>();
    private static final String FLIGHTS_FILE = "resources/flights.csv";
    private static final String BOOKINGS_FILE = "resources/bookings.csv";

    FlightManager() {
        loadFlights();
        loadBookings();
    }

    Collection<Flight> getAllFlights() {
        return flights.values();
    }

    Flight getFlight(String flightId) {
        return flights.get(flightId);
    }

    void addFlight(Flight f) {
        flights.put(f.flightId, f);
        saveFlights();
    }

    void loadFlights() {
        Path p = Paths.get(FLIGHTS_FILE);
        if (!Files.exists(p)) {
            try {
                if (p.getParent() != null) Files.createDirectories(p.getParent());
                List<String> sample = new ArrayList<>();
                sample.add("F101,Delhi,Mumbai,3500.0,5");
                sample.add("F102,Mumbai,Bangalore,4200.0,6");
                sample.add("F103,Chennai,Hyderabad,3000.0,4");
                Files.write(p, sample, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                System.out.println("Created sample flights file: " + FLIGHTS_FILE);
            } catch (IOException e) {
                System.out.println("Error creating sample flights file: " + e.getMessage());
            }
        }

        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                Flight f = Flight.fromCsv(line);
                if (f != null) flights.put(f.flightId, f);
            }
        } catch (IOException e) {
            System.out.println("Error reading flights file: " + e.getMessage());
        }
    }

    void saveFlights() {
        Path p = Paths.get(FLIGHTS_FILE);
        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Flight f : flights.values()) {
                bw.write(f.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving flights: " + e.getMessage());
        }
    }

    void loadBookings() {
        Path p = Paths.get(BOOKINGS_FILE);
        if (!Files.exists(p)) {
            return;
        }
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                Passenger passenger = Passenger.fromCsv(line);
                if (passenger == null) continue;
                Flight f = flights.get(passenger.flightId);
                if (f != null) {
                    if (!f.passengerExists(passenger.id) && !f.seatTaken(passenger.seatNo) && f.passengers.size() < f.capacity) {
                        f.passengers.add(passenger);
                    } else {
                        System.out.println("Warning: Could not load booking for passenger " + passenger.id + " on flight " + passenger.flightId);
                    }
                } else {
                    System.out.println("Warning: Flight " + passenger.flightId + " not found for booking " + passenger.id);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading bookings file: " + e.getMessage());
        }
    }

    void appendBookingToFile(Passenger p) {
        Path pth = Paths.get(BOOKINGS_FILE);
        try (BufferedWriter bw = Files.newBufferedWriter(pth, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(p.toCsv());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error appending booking: " + e.getMessage());
        }
    }

    void saveAllBookings() {
        Path p = Paths.get(BOOKINGS_FILE);
        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Flight f : flights.values()) {
                for (Passenger pas : f.passengers) {
                    bw.write(pas.toCsv());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }
}
