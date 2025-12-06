package sekkah;

public class Trip {

    // Basic trip information
    private String tripId;
    private String trainId;
    private String origin;
    private String destination;
    private String departureTime;
    private String arrivalTime;

    // Prices per seat class
    private double economyPrice;
    private double businessPrice;

    // Seats available for the trip
    private int economyAvailable;
    private int businessAvailable;

    // Current trip status
    private TripStatus status;

    // Constructor with full validation
    public Trip(String tripId, String trainId,
                String origin, String destination,
                String departureTime, String arrivalTime,
                double economyPrice, double businessPrice,
                int economyAvailable, int businessAvailable,
                TripStatus status) {

        // Required text fields
        this.tripId = requireNonEmpty(tripId, "Trip ID must not be empty");
        this.trainId = requireNonEmpty(trainId, "Train ID must not be empty");
        this.origin = requireNonEmpty(origin, "Origin must not be empty");
        this.destination = requireNonEmpty(destination, "Destination must not be empty");
        this.departureTime = requireNonEmpty(departureTime, "Departure time must not be empty");
        this.arrivalTime = requireNonEmpty(arrivalTime, "Arrival time must not be empty");

        // Price validation
        if (economyPrice < 0)
            throw new IllegalArgumentException("Economy price cannot be negative");

        if (businessPrice < 0)
            throw new IllegalArgumentException("Business price cannot be negative");

        // Seat availability must be non-negative
        if (economyAvailable < 0)
            throw new IllegalArgumentException("Economy seats cannot be negative");

        if (businessAvailable < 0)
            throw new IllegalArgumentException("Business seats cannot be negative");

        // Status validation
        if (status == null)
            throw new IllegalArgumentException("Trip status must not be null");

        this.economyPrice = economyPrice;
        this.businessPrice = businessPrice;
        this.economyAvailable = economyAvailable;
        this.businessAvailable = businessAvailable;
        this.status = status;
    }

    // Basic getters
    public String getTripId() {
        return tripId;
    }

    public String getTrainId() {
        return trainId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        if (status == null)
            throw new IllegalArgumentException("Trip status must not be null");
        this.status = status;
    }

    // Seat availability check
    public boolean hasSeats(SeatClass seatClass, int qty) {
        if (seatClass == null || qty <= 0)
            return false;

        switch (seatClass) {
            case ECONOMY:
                return economyAvailable >= qty;
            case BUSINESS:
                return businessAvailable >= qty;
            default:
                return false;
        }
    }

    // Reserve seats for booking
    public void reserveSeats(SeatClass seatClass, int qty) {
        if (!hasSeats(seatClass, qty))
            throw new IllegalArgumentException("Not enough seats available for reservation");

        if (seatClass == SeatClass.ECONOMY) {
            economyAvailable -= qty;
        } else {
            businessAvailable -= qty;
        }
    }

    // Release seats when a ticket is cancelled
    public void releaseSeats(SeatClass seatClass, int qty) {
        if (seatClass == null || qty <= 0)
            return;

        if (seatClass == SeatClass.ECONOMY) {
            economyAvailable += qty;
        } else {
            businessAvailable += qty;
        }

        // Basic sanity check
        if (economyAvailable < 0 || businessAvailable < 0)
            throw new IllegalStateException("Seat availability became negative");
    }

    // Price getter based on seat type
    public double getPrice(SeatClass seatClass) {
        if (seatClass == SeatClass.ECONOMY)
            return economyPrice;

        if (seatClass == SeatClass.BUSINESS)
            return businessPrice;

        throw new IllegalArgumentException("Invalid seat class");
    }

    // Helper: ensures a String is non-null and not blank
    private String requireNonEmpty(String value, String errorMessage) {
        if (value == null)
            throw new IllegalArgumentException(errorMessage);
        String trimmed = value.trim();
        if (trimmed.isEmpty())
            throw new IllegalArgumentException(errorMessage);
        return trimmed;
    }

    @Override
public String toString() {
    return  "\n----------------------------------------" +
            "\nTrip ID        : " + tripId +
            "\nTrain ID       : " + trainId +
            "\nFrom           : " + origin +
            "\nTo             : " + destination +
            "\nDeparture Time : " + departureTime +
            "\nArrival Time   : " + arrivalTime +
            "\nEconomy Price  : " + economyPrice +
            "\nBusiness Price : " + businessPrice +
            "\nEconomy Seats  : " + economyAvailable +
            "\nBusiness Seats : " + businessAvailable +
            "\nStatus         : " + status +
            "\n----------------------------------------";
}

}
