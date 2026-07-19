class Passenger extends Person {
    String id;
    String seatNo;
    String flightId;

    Passenger(String id, String name, int age, String seatNo, String flightId) {
        super(name, age);
        this.id = id;
        this.seatNo = seatNo;
        this.flightId = flightId;
    }

    void displayInfo() {
        System.out.println("Passenger ID: " + id + ", Name: " + name +
                ", Age: " + age + ", Seat: " + seatNo + ", Flight: " + flightId);
    }

    String toCsv() {
        return id + "," + escapeCsv(name) + "," + age + "," + seatNo + "," + flightId;
    }

    static Passenger fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 5) return null;
        String id = parts[0];
        String name = unescapeCsv(parts[1]);
        int age = Integer.parseInt(parts[2]);
        String seatNo = parts[3];
        String flightId = parts[4];
        return new Passenger(id, name, age, seatNo, flightId);
    }

    private static String escapeCsv(String s) {
        return s.replace("\n", " ").replace(",", " ");
    }

    private static String unescapeCsv(String s) {
        return s;
    }
}
