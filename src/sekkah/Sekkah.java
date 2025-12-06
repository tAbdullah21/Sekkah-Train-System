package sekkah;

import java.util.Scanner;

public class Sekkah {

    // Shared scanner for user input
    private static Scanner scanner = new Scanner(System.in);

    // Allowed cities in the system
    private static final String[] CITIES = {
            "DAMMAM", "JEDDAH", "RIYADH", "ABHA", "MADINAH", "HAIL", "AL-BAHA"
    };

    // Users (both Admin and Passenger)
    private static User[] users = new User[50];
    private static int userCount = 0;

    // Data managed by BookingManager
    private static Train[] trains = new Train[50];
    private static Trip[] trips = new Trip[100];
    private static Ticket[] tickets = new Ticket[200];

    private static BookingManager bookingManager = new BookingManager(trains, trips, tickets);

    public static void main(String[] args) {

        initDummyData(); // initial admin, train, trip

        System.out.println("=== Welcome to Sekkah Train System ===");

        boolean running = true;
        while (running) {
            System.out.println("\n================================");
            System.out.println("Main Menu");
            System.out.println("1) Login");
            System.out.println("2) Sign up (Passenger)");
            System.out.println("3) Exit");
            System.out.println("================================");

            int choice = readIntInRange("Choose an option: ", 1, 3);

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleSignup();
                    break;
                case 3:
                    System.out.println("Exiting Sekkah. Goodbye.");
                    running = false;
                    break;
            }
        }
    }

    // Creates initial data so the system is not empty
    private static void initDummyData() {
    // Default admin
    Admin admin = new Admin(
            "A001",
            "System Admin",
            "admin",
            "admin123",
            "STF001",
            "Operations"
    );
    users[userCount++] = admin;

    // One train
    Train t1 = new Train(
            "TR1",
            "Sekkah Express",
            "Model-X",
            100,   // economy
            20,    // business
            TrainStatus.OPERATIONAL
    );
    bookingManager.addTrain(t1);

    // Second train (for extra trips)
    Train t2 = new Train(
            "TR2",
            "Gulf Runner",
            "Model-Z",
            120,
            30,
            TrainStatus.OPERATIONAL
    );
    bookingManager.addTrain(t2);

    // -------------------------
    // TRIP 1 
    // -------------------------
    Trip trip1 = new Trip(
            "TP1",
            "TR1",
            "Riyadh",
            "Dammam",
            "2025-12-10 09:00",
            "2025-12-10 12:00",
            80.0,     // economy price
            150.0,    // business price
            100,      // economy left
            20,       // business left
            TripStatus.SCHEDULED
    );
    bookingManager.addTrip(trip1);

    // -------------------------
    // TRIP 2 
    // -------------------------
    Trip trip2 = new Trip(
            "TP2",
            "TR2",
            "Jeddah",     
            "Riyadh",    
            "2025-12-11 14:00",
            "2025-12-11 16:30",
            60.0,     // economy price
            120.0,    // business price
            90,       // economy available
            15,       // business available
            TripStatus.SCHEDULED
    );
    bookingManager.addTrip(trip2);
    // Done
}

    // Handles user login for both Admin and Passenger
    private static void handleLogin() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = findUserByUsername(username);
        if (user == null || !user.authenticate(username, password)) {
            System.out.println("Invalid username or password.");
            return;
        }

        System.out.println("Login successful. Welcome, " + user.getName() + ".");

        if (user.getRole() == Role.ADMIN) {
            adminMenu((Admin) user);
        } else {
            passengerMenu((Passenger) user);
        }
    }

    // Handles passenger signup
    private static void handleSignup() {
        System.out.println("\n--- Passenger Signup ---");

        if (userCount >= users.length) {
            System.out.println("Cannot register more users. User limit reached.");
            return;
        }

        String id = "P" + (userCount + 1);

        String name = readNonEmptyString("Enter name: ");

        String username;
        while (true) {
            username = readNonEmptyString("Enter username: ");
            if (findUserByUsername(username) != null) {
                System.out.println("Username already taken. Choose another one.");
            } else {
                break;
            }
        }

        String password;
        do {
            System.out.print("Enter password (min 6 chars): ");
            password = scanner.nextLine();
            if (!User.isValidPassword(password)) {
                System.out.println("Invalid password. Try again.");
            }
        } while (!User.isValidPassword(password));

        int age = readIntInRange("Enter age: ", 0, 120);

        // Email optional
        System.out.print("Enter email (optional, press Enter to skip): ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            email = null;
        }

        // Phone 10 digits
        String phone;
        while (true) {
            System.out.print("Enter phone number (10 digits): ");
            phone = scanner.nextLine().trim();
            if (phone.matches("\\d{10}")) {
                break;
            }
            System.out.println("Phone must be exactly 10 digits.");
        }

        String nationalId = readNonEmptyString("Enter national ID: ");

        try {
            Passenger p = new Passenger(id, name, username, password, age, email, phone, nationalId);
            users[userCount++] = p;
            System.out.println("Signup successful. You can now log in.");
        } catch (Exception e) {
            System.out.println("Error during signup: " + e.getMessage());
        }
    }

    // Menu for passenger actions
    private static void passengerMenu(Passenger passenger) {
        boolean stay = true;
        while (stay) {
            System.out.println("\n--- Passenger Menu ---");
            System.out.println("1) View all trips");
            System.out.println("2) Search trips");
            System.out.println("3) Book ticket");
            System.out.println("4) My tickets");
            System.out.println("5) Cancel ticket");
            System.out.println("6) Refund ticket");
            System.out.println("7) Logout");

            int choice = readIntInRange("Choose an option: ", 1, 7);

            switch (choice) {
                case 1:
                    viewAllTrips();
                    break;
                case 2:
                    handleSearchTrips();
                    break;
                case 3:
                    handleBookTicket(passenger);
                    break;
                case 4:
                    viewMyTickets(passenger);
                    break;
                case 5:
                    handleCancelTicket(passenger);
                    break;
                case 6:
                    handleRefundTicket(passenger);
                    break;
                case 7:
                    stay = false;
                    System.out.println("Logging out...");
                    break;
            }
        }
    }

    // Menu for admin actions
    private static void adminMenu(Admin admin) {
        boolean stay = true;
        while (stay) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1) Add train");
            System.out.println("2) Add trip");
            System.out.println("3) Logout");

            int choice = readIntInRange("Choose an option: ", 1, 3);

            switch (choice) {
                case 1:
                    handleAddTrain(admin);
                    break;
                case 2:
                    handleAddTrip(admin);
                    break;
                case 3:
                    stay = false;
                    System.out.println("Logging out...");
                    break;
            }
        }
    }

    // Shows all scheduled trips
    private static void viewAllTrips() {
        System.out.println("\n--- All Scheduled Trips ---");
        boolean any = false;
        for (Trip t : trips) {
            if (t != null && t.getStatus() == TripStatus.SCHEDULED) {
                System.out.println(t);
                any = true;
            }
        }
        if (!any) {
            System.out.println("No trips available at the moment.");
        }
    }

    // Simple trip search
    private static void handleSearchTrips() {
        System.out.println("\n--- Search Trips ---");

        printCities();

        String origin = readCity("Enter origin: ");

        String destination;
        while (true) {
            destination = readCity("Enter destination: ");
            if (!destination.equalsIgnoreCase(origin)) {
                break;
            }
            System.out.println("Origin and destination cannot be the same city. Try again.");
        }

        Trip[] result = bookingManager.searchTrips(origin, destination);
        if (result.length == 0) {
            System.out.println("No matching trips found.");
            return;
        }

        System.out.println("\nAvailable trips:");
        for (int i = 0; i < result.length; i++) {
            System.out.println((i + 1) + ") " + result[i]);
        }
    }

    // Booking flow: user chooses trip by ID directly
    private static void handleBookTicket(Passenger passenger) {
        System.out.println("\n--- Book Ticket ---");

        // Show all available trips so the user can see IDs
        viewAllTrips();

        // Check if there is at least one scheduled trip
        boolean any = false;
        for (Trip t : trips) {
            if (t != null && t.getStatus() == TripStatus.SCHEDULED) {
                any = true;
                break;
            }
        }
        if (!any) {
            System.out.println("No trips available to book.");
            return;
        }

        // Ask for trip ID directly
        Trip chosen = null;
        while (chosen == null) {
            String tripIdInput = readNonEmptyString("Enter Trip ID to book: ");
            chosen = findTripByIdInMain(tripIdInput);

            if (chosen == null || chosen.getStatus() != TripStatus.SCHEDULED) {
                System.out.println("Invalid or unavailable Trip ID. Please enter a valid Trip ID from the list above.");
                chosen = null;
            }
        }

        System.out.println("Selected trip:");
        System.out.println(chosen);

        System.out.println("Seat class: 1) ECONOMY  2) BUSINESS");
        int classChoice = readIntInRange("Choose seat class: ", 1, 2);
        SeatClass seatClass = (classChoice == 1) ? SeatClass.ECONOMY : SeatClass.BUSINESS;

        int qty = readIntInRange("Enter quantity: ", 1, 20);

        try {
            Ticket ticket = bookingManager.createTicket(passenger, chosen, seatClass, qty);
            System.out.println("Ticket booked successfully:");
            System.out.println(ticket);
        } catch (Exception e) {
            System.out.println("Error while booking ticket: " + e.getMessage());
        }
    }

    // Shows all tickets that belong to the logged-in passenger
    private static void viewMyTickets(Passenger passenger) {
        System.out.println("\n--- My Tickets ---");

        boolean any = false;
        for (Ticket t : tickets) {
            if (t != null && t.getPassengerId().equals(passenger.getId())) {
                System.out.println(t);
                any = true;
            }
        }

        if (!any) {
            System.out.println("You have no tickets.");
        }
    }

    // Cancels one of the passenger's tickets by ID
    private static void handleCancelTicket(Passenger passenger) {
        System.out.println("\n--- Cancel Ticket ---");

        // Show current tickets first
        boolean any = false;
        for (Ticket t : tickets) {
            if (t != null && t.getPassengerId().equals(passenger.getId())) {
                System.out.println(t);
                any = true;
            }
        }

        if (!any) {
            System.out.println("You have no tickets to cancel.");
            return;
        }

        String ticketId = readNonEmptyString("Enter Ticket ID to cancel: ");

        Ticket ticket = findTicketByIdInMain(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        if (!ticket.getPassengerId().equals(passenger.getId())) {
            System.out.println("This ticket does not belong to you.");
            return;
        }

        boolean success = bookingManager.cancelTicket(ticketId);
        if (!success) {
            System.out.println("Ticket was not cancelled.");
        }
    }

    // Refunds one of the passenger's tickets by ID
    private static void handleRefundTicket(Passenger passenger) {
        System.out.println("\n--- Refund Ticket ---");

        boolean any = false;
        for (Ticket t : tickets) {
            if (t != null && t.getPassengerId().equals(passenger.getId())) {
                System.out.println(t);
                any = true;
            }
        }

        if (!any) {
            System.out.println("You have no tickets to refund.");
            return;
        }

        String ticketId = readNonEmptyString("Enter Ticket ID to refund: ");

        Ticket ticket = findTicketByIdInMain(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        if (!ticket.getPassengerId().equals(passenger.getId())) {
            System.out.println("This ticket does not belong to you.");
            return;
        }

        boolean success = bookingManager.refundTicket(ticketId);
        if (!success) {
            System.out.println("Ticket was not refunded.");
        }
    }

    // Admin: add train
    private static void handleAddTrain(Admin admin) {
        System.out.println("\n--- Add Train ---");

        String trainId = readNonEmptyString("Enter train ID: ");
        String name = readNonEmptyString("Enter train name: ");
        String model = readNonEmptyString("Enter train model: ");
        int ecoCap = readIntInRange("Enter economy capacity: ", 0, 500);
        int busCap = readIntInRange("Enter business capacity: ", 0, 500);

        TrainStatus status = TrainStatus.OPERATIONAL;

        try {
            Train train = new Train(trainId, name, model, ecoCap, busCap, status);
            if (bookingManager.addTrain(train)) {
                System.out.println("Train added successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error while adding train: " + e.getMessage());
        }
    }

    // Admin: add trip
    private static void handleAddTrip(Admin admin) {
        System.out.println("\n--- Add Trip ---");

        String tripId = readNonEmptyString("Enter trip ID: ");
        String trainId = readNonEmptyString("Enter existing train ID: ");

        printCities();

        String origin = readCity("Enter origin: ");

        String destination;
        while (true) {
            destination = readCity("Enter destination: ");
            if (!destination.equalsIgnoreCase(origin)) {
                break;
            }
            System.out.println("Origin and destination cannot be the same city. Try again.");
        }

        String depTime = readNonEmptyString("Enter departure time (text): ");
        String arrTime = readNonEmptyString("Enter arrival time (text): ");

        double ecoPrice = readDoubleMin("Enter economy price: ", 0);
        double busPrice = readDoubleMin("Enter business price: ", 0);
        int ecoSeats = readIntInRange("Enter economy seats: ", 0, 500);
        int busSeats = readIntInRange("Enter business seats: ", 0, 500);

        try {
            Trip trip = new Trip(tripId, trainId, origin, destination,
                    depTime, arrTime, ecoPrice, busPrice, ecoSeats, busSeats, TripStatus.SCHEDULED);
            if (bookingManager.addTrip(trip)) {
                System.out.println("Trip added successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error while adding trip: " + e.getMessage());
        }
    }

    // Finds a user by username
    private static User findUserByUsername(String username) {
        if (username == null) return null;
        for (int i = 0; i < userCount; i++) {
            if (users[i] != null && users[i].getUsername().equals(username)) {
                return users[i];
            }
        }
        return null;
    }

    // Finds a trip by its ID (case-insensitive) using the main trips array
    private static Trip findTripByIdInMain(String tripId) {
        if (tripId == null) return null;
        String id = tripId.trim();
        if (id.isEmpty()) return null;

        for (Trip t : trips) {
            if (t != null && t.getTripId().equalsIgnoreCase(id)) {
                return t;
            }
        }
        return null;
    }

    // Finds a ticket in the main tickets array by its ID (case-insensitive)
    private static Ticket findTicketByIdInMain(String ticketId) {
        if (ticketId == null) return null;
        String id = ticketId.trim();
        if (id.isEmpty()) return null;

        for (Ticket t : tickets) {
            if (t != null && t.getTicketId().equalsIgnoreCase(id)) {
                return t;
            }
        }
        return null;
    }

    // Reads a non-empty string with a prompt
    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) {
                return s;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    // Reads an int within a range
    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val < min || val > max) {
                    System.out.println("Value must be between " + min + " and " + max + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Reads a double with minimum value
    private static double readDoubleMin(String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(line);
                if (val < min) {
                    System.out.println("Value must be at least " + min + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Prints available cities
    private static void printCities() {
        System.out.println("Available cities:");
        for (String c : CITIES) {
            System.out.println(" - " + c);
        }
    }

    // Validates that a city is in the list
    private static boolean isValidCity(String city) {
        if (city == null) return false;
        String c = city.trim().toUpperCase();
        for (String allowed : CITIES) {
            if (allowed.equals(c)) {
                return true;
            }
        }
        return false;
    }

    // Reads a valid city name from the list
    private static String readCity(String prompt) {
        while (true) {
            System.out.print(prompt);
            String city = scanner.nextLine().trim();
            if (isValidCity(city)) {
                return city;
            }
            System.out.println("Invalid city. Please choose from the list above.");
            printCities();
        }
    }
}
