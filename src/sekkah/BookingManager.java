package sekkah;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingManager {

    // Simple counters for statistics / ID generation
    public static int trainCounter;
    public static int tripCounter;
    public static int ticketCounter;

    // Storage for system data
    private Train[] trains;
    private Trip[] trips;
    private Ticket[] tickets;

    // Constructor: receives pre-allocated arrays
    public BookingManager(Train[] trains, Trip[] trips, Ticket[] tickets) {
        if (trains == null || trips == null || tickets == null)
            throw new IllegalArgumentException("Arrays must not be null");

        this.trains = trains;
        this.trips = trips;
        this.tickets = tickets;

        trainCounter = countNonNull(trains);
        tripCounter = countNonNull(trips);
        ticketCounter = countNonNull(tickets);
    }

    // Counts non-null elements in an array
    private int countNonNull(Object[] arr) {
        int count = 0;
        for (Object o : arr) {
            if (o != null) count++;
        }
        return count;
    }

    // Adds a train to the array if there is space
    public boolean addTrain(Train train) {
        if (train == null)
            throw new IllegalArgumentException("Train must not be null");

        for (int i = 0; i < trains.length; i++) {
            if (trains[i] == null) {
                trains[i] = train;
                trainCounter++;
                return true;
            }
        }
        System.out.println("No space available to add more trains.");
        return false;
    }

    // Adds a trip to the array if there is space
    public boolean addTrip(Trip trip) {
        if (trip == null)
            throw new IllegalArgumentException("Trip must not be null");

        for (int i = 0; i < trips.length; i++) {
            if (trips[i] == null) {
                trips[i] = trip;
                tripCounter++;
                return true;
            }
        }
        System.out.println("No space available to add more trips.");
        return false;
    }

    // Searches for scheduled trips by origin and destination (case-insensitive)
    public Trip[] searchTrips(String origin, String destination) {
        if (origin == null || destination == null)
            return new Trip[0];

        String originTrim = origin.trim().toLowerCase();
        String destTrim = destination.trim().toLowerCase();

        if (originTrim.isEmpty() || destTrim.isEmpty())
            return new Trip[0];

        int matches = 0;
        for (Trip t : trips) {
            if (t != null &&
                t.getStatus() == TripStatus.SCHEDULED &&
                t.getOrigin().trim().equalsIgnoreCase(originTrim) &&
                t.getDestination().trim().equalsIgnoreCase(destTrim)) {
                matches++;
            }
        }

        Trip[] result = new Trip[matches];
        int idx = 0;
        for (Trip t : trips) {
            if (t != null &&
                t.getStatus() == TripStatus.SCHEDULED &&
                t.getOrigin().trim().equalsIgnoreCase(originTrim) &&
                t.getDestination().trim().equalsIgnoreCase(destTrim)) {
                result[idx++] = t;
            }
        }

        return result;
    }

    // Calculates total price for a given trip, seat class, and quantity
    public double calculatePrice(Trip trip, SeatClass seatClass, int quantity) {
        if (trip == null)
            throw new IllegalArgumentException("Trip must not be null");

        if (seatClass == null)
            throw new IllegalArgumentException("Seat class must not be null");

        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");

        double unitPrice = trip.getPrice(seatClass);
        return unitPrice * quantity;
    }

    // Creates a ticket and updates seat availability
    public Ticket createTicket(Passenger passenger, Trip trip,
                               SeatClass seatClass, int quantity) {

        if (passenger == null)
            throw new IllegalArgumentException("Passenger must not be null");

        if (trip == null)
            throw new IllegalArgumentException("Trip must not be null");

        if (seatClass == null)
            throw new IllegalArgumentException("Seat class must not be null");

        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");

        if (trip.getStatus() != TripStatus.SCHEDULED)
            throw new IllegalStateException("Cannot book a ticket for a non-scheduled trip");

        if (!trip.hasSeats(seatClass, quantity))
            throw new IllegalArgumentException("Not enough seats available for this trip");

        double totalPrice = calculatePrice(trip, seatClass, quantity);

        String ticketId = nextTicketId();
        String purchaseTime = currentTimeString();

        Ticket ticket = new Ticket(
                ticketId,
                passenger.getId(),
                trip.getTripId(),
                seatClass,
                quantity,
                totalPrice,
                purchaseTime,
                TicketStatus.ACTIVE
        );

        // Reserve seats in the trip and store the ticket
        trip.reserveSeats(seatClass, quantity);
        addTicketToArray(ticket);

        System.out.println("Ticket created successfully: " + ticketId);
        return ticket;
    }

    // Attempts to cancel a ticket and release seats
    public boolean cancelTicket(String ticketId) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            System.out.println("Invalid ticket ID.");
            return false;
        }

        Ticket ticket = findTicketById(ticketId.trim());
        if (ticket == null) {
            System.out.println("Ticket not found: " + ticketId);
            return false;
        }

        if (ticket.getStatus() != TicketStatus.ACTIVE) {
            System.out.println("Ticket is not active and cannot be cancelled.");
            return false;
        }

        Trip trip = findTripById(ticket.getTripId());
        if (trip != null) {
            trip.releaseSeats(ticket.getSeatClass(), ticket.getQuantity());
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        System.out.println("Ticket " + ticketId + " has been cancelled.");
        return true;
    }

    // Attempts to refund a cancelled ticket
    public boolean refundTicket(String ticketId) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            System.out.println("Invalid ticket ID.");
            return false;
        }

        Ticket ticket = findTicketById(ticketId.trim());
        if (ticket == null) {
            System.out.println("Ticket not found: " + ticketId);
            return false;
        }

        if (ticket.getStatus() != TicketStatus.CANCELLED) {
            System.out.println("Ticket must be cancelled before it can be refunded.");
            return false;
        }

        ticket.setStatus(TicketStatus.REFUNDED);
        System.out.println("Ticket " + ticketId + " has been refunded.");
        return true;
    }

    // Generates a new ticket ID
    public static String nextTicketId() {
        ticketCounter++;
        return "T" + ticketCounter;
    }

    // Stores a ticket in the tickets array
    private void addTicketToArray(Ticket ticket) {
        for (int i = 0; i < tickets.length; i++) {
            if (tickets[i] == null) {
                tickets[i] = ticket;
                return;
            }
        }
        throw new IllegalStateException("No space available to store more tickets.");
    }

    // Finds a ticket by ID
    private Ticket findTicketById(String ticketId) {
        for (Ticket t : tickets) {
            if (t != null && t.getTicketId().equals(ticketId)) {
                return t;
            }
        }
        return null;
    }

    // Finds a trip by ID
    private Trip findTripById(String tripId) {
        for (Trip t : trips) {
            if (t != null && t.getTripId().equals(tripId)) {
                return t;
            }
        }
        return null;
    }

    // Helper: returns current time as a simple string
    private String currentTimeString() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return now.format(fmt);
    }
}
