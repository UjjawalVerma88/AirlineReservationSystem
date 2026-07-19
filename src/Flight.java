import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Flight {
    String flightId;
    String source;
    String destination;
    double price;
    int capacity;
    List<Passenger> passengers;

    Flight(String flightId, String source, String destination, double price, int capacity) {
        this.flightId = flightId;
        this.source = source;
        this.destination = destination;
        this.price = price;
        this.capacity = capacity;
        this.passengers = new ArrayList<>();
    }

    String toCsv() {
        return flightId + "," + escapeCsv(source) + "," + escapeCsv(destination) + "," + price + "," + capacity;
    }

    static Flight fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 5) return null;
        String fid = parts[0];
        String src = parts[1];
        String dst = parts[2];
        double price = Double.parseDouble(parts[3]);
        int cap = Integer.parseInt(parts[4]);
        return new Flight(fid, src, dst, price, cap);
    }

    boolean isFull() {
        return passengers.size() >= capacity;
    }

    boolean seatTaken(String seatNo) {
        for (Passenger p : passengers) {
            if (p.seatNo.equalsIgnoreCase(seatNo)) return true;
        }
        return false;
    }

    boolean passengerExists(String passengerId) {
        for (Passenger p : passengers) {
            if (p.id.equals(passengerId)) return true;
        }
        return false;
    }

    void addPassenger(Passenger p) throws ReservationException {
        if (isFull()) throw new ReservationException("Flight " + flightId + " is full.");
        if (!isAlphanumeric(p.id)) throw new ReservationException("Invalid Passenger ID! Only alphanumeric allowed.");
        if (passengerExists(p.id)) throw new ReservationException("Duplicate ID! Passenger ID " + p.id + " already exists on this flight.");
        if (seatTaken(p.seatNo)) throw new ReservationException("Seat " + p.seatNo + " is already booked on flight " + flightId + ".");
        passengers.add(p);
    }

    void cancelPassenger(String passengerId) throws ReservationException {
        Iterator<Passenger> it = passengers.iterator();
        boolean found = false;
        while (it.hasNext()) {
            Passenger p = it.next();
            if (p.id.equals(passengerId)) {
                it.remove();
                System.out.println("Ticket canceled for " + p.name + " (Flight: " + flightId + ")");
                found = true;
                break;
            }
        }
        if (!found) throw new ReservationException("Passenger with ID " + passengerId + " not found on flight " + flightId + ".");
    }

    void showPassengers() {
        if (passengers.isEmpty()) {
            System.out.println("No passengers booked for flight " + flightId + " (" + source + "->" + destination + ").");
            return;
        }
        System.out.println("---- Passenger List for flight " + flightId + " (" + source + "->" + destination + ") ----");
        for (Passenger p : passengers) p.displayInfo();
    }

    private static boolean isAlphanumeric(String id) {
        return id.matches("[a-zA-Z0-9]+");
    }

    private static String escapeCsv(String s) {
        return s.replace(",", " ");
    }
}
