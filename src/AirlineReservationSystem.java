import java.util.Scanner;

// Main System
public class AirlineReservationSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FlightManager fm = new FlightManager();

        int choice;
        do {
            System.out.println("\n--- Airline Reservation System (CSV-backed) ---");
            System.out.println("1. List Flights");
            System.out.println("2. Add Flight");
            System.out.println("3. Book Ticket");
            System.out.println("4. Cancel Ticket");
            System.out.println("5. View Passengers for a Flight");
            System.out.println("6. Export/Save current state to files");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            while (!sc.hasNextInt()) {
                System.out.print("Please enter a number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        listFlights(fm);
                        break;

                    case 2:
                        addFlightInteractive(fm, sc);
                        break;

                    case 3:
                        bookTicketInteractive(fm, sc);
                        break;

                    case 4:
                        cancelTicketInteractive(fm, sc);
                        break;

                    case 5:
                        viewPassengersInteractive(fm, sc);
                        break;

                    case 6:
                        fm.saveFlights();
                        fm.saveAllBookings();
                        System.out.println("Saved flights and bookings to files.");
                        break;

                    case 7:
                        fm.saveFlights();
                        fm.saveAllBookings();
                        System.out.println("Exiting... Saved data. Thank you!");
                        break;

                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            } catch (ReservationException e) {
                System.out.println("Reservation Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Try again.");
            }
        } while (choice != 7);

        sc.close();
    }

    private static void listFlights(FlightManager fm) {
        System.out.println("---- Available Flights ----");
        for (Flight f : fm.getAllFlights()) {
            System.out.printf("FlightID: %s | %s -> %s | Price: %.2f | Capacity: %d | Booked: %d\n",
                    f.flightId, f.source, f.destination, f.price, f.capacity, f.passengers.size());
        }
    }

    private static void addFlightInteractive(FlightManager fm, Scanner sc) {
        System.out.print("Enter Flight ID (e.g., F200): ");
        String fid = sc.nextLine().trim();
        System.out.print("Enter Source: ");
        String src = sc.nextLine().trim();
        System.out.print("Enter Destination: ");
        String dst = sc.nextLine().trim();
        System.out.print("Enter Price: ");
        double price = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Enter Capacity (number of seats): ");
        int cap = Integer.parseInt(sc.nextLine().trim());

        if (fm.getFlight(fid) != null) {
            System.out.println("Flight with ID " + fid + " already exists. Choose another ID.");
            return;
        }

        Flight f = new Flight(fid, src, dst, price, cap);
        fm.addFlight(f);
        System.out.println("Flight added successfully.");
    }

    private static void bookTicketInteractive(FlightManager fm, Scanner sc) throws ReservationException {
        System.out.print("Enter Flight ID to book: ");
        String fid = sc.nextLine().trim();
        Flight flight = fm.getFlight(fid);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }
        System.out.print("Enter Passenger ID (Alphanumeric): ");
        String id = sc.nextLine().trim();
        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Age: ");
        int age = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Enter Seat No (e.g., A1, B2): ");
        String seatNo = sc.nextLine().trim();

        Passenger p = new Passenger(id, name, age, seatNo, fid);
        flight.addPassenger(p);

        fm.appendBookingToFile(p);

        System.out.println("Ticket booked successfully for " + name + " on flight " + fid + ". Price: " + flight.price);
    }

    private static void cancelTicketInteractive(FlightManager fm, Scanner sc) throws ReservationException {
        System.out.print("Enter Flight ID for cancellation: ");
        String fid = sc.nextLine().trim();
        Flight flight = fm.getFlight(fid);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }
        System.out.print("Enter Passenger ID to cancel: ");
        String pid = sc.nextLine().trim();
        flight.cancelPassenger(pid);

        fm.saveAllBookings();
    }

    private static void viewPassengersInteractive(FlightManager fm, Scanner sc) {
        System.out.print("Enter Flight ID to view passengers: ");
        String fid = sc.nextLine().trim();
        Flight flight = fm.getFlight(fid);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }
        flight.showPassengers();
    }
}
