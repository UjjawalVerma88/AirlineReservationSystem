# Airline Reservation System

A simple console-based Airline Reservation System written in Java. Flight and
booking data is persisted to CSV files, so your data survives between runs.

## Project Structure

```
AirlineReservationSystem/
├── src/                          # Java source files (one class per file)
│   ├── Person.java               # Base class for a person
│   ├── Passenger.java            # A passenger booked on a flight (extends Person)
│   ├── ReservationException.java # Custom checked exception for booking errors
│   ├── Flight.java               # Flight details + passenger management
│   ├── FlightManager.java        # Loads/saves flights & bookings from CSV
│   └── AirlineReservationSystem.java  # Main class / console menu
├── resources/                    # Data files (CSV storage)
│   ├── flights.csv
│   └── bookings.csv
├── .gitignore
└── README.md
```

## How to Compile & Run

From the project root:

```bash
# Compile all source files into an "out" directory
javac -d out src/*.java

# Run the program (must be run from the project root so it can find resources/)
java -cp out AirlineReservationSystem
```

## Features

1. List all flights
2. Add a new flight
3. Book a ticket (validates duplicate passenger IDs and duplicate seat numbers)
4. Cancel a ticket
5. View passengers for a given flight
6. Export/save current state to CSV files
7. Exit (auto-saves)

## Notes

- Flight and booking data is stored in `resources/flights.csv` and
  `resources/bookings.csv`. If `flights.csv` doesn't exist yet, the program
  creates one with sample flights automatically.
- Compiled `.class` files and IDE-specific folders (`.idea/`, `out/`) are
  intentionally excluded via `.gitignore` — they're build artifacts, not
  source, and shouldn't be checked into version control or shipped.
