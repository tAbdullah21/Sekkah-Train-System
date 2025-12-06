package sekkah;

public class Train {

    // Maximum allowed total capacity (economy + business)
    public static final int MAX_TOTAL_CAPACITY = 500;

    // Basic train information
    private String trainId;
    private String name;
    private String model;

    // Seats available per class
    private int economyCapacity;
    private int businessCapacity;

    // Current status of the train
    private TrainStatus status;

    // Constructor with validation
    public Train(String trainId, String name, String model,
                 int economyCapacity, int businessCapacity,
                 TrainStatus status) {

        if (trainId == null || trainId.trim().isEmpty())
            throw new IllegalArgumentException("Train ID must not be empty");

        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Train name must not be empty");

        if (model == null || model.trim().isEmpty())
            throw new IllegalArgumentException("Train model must not be empty");

        if (economyCapacity < 0)
            throw new IllegalArgumentException("Economy capacity cannot be negative");

        if (businessCapacity < 0)
            throw new IllegalArgumentException("Business capacity cannot be negative");

        int total = economyCapacity + businessCapacity;
        if (total > MAX_TOTAL_CAPACITY)
            throw new IllegalArgumentException("Total capacity exceeds limit of " + MAX_TOTAL_CAPACITY);

        if (status == null)
            throw new IllegalArgumentException("Train status must not be null");

        this.trainId = trainId.trim();
        this.name = name.trim();
        this.model = model.trim();
        this.economyCapacity = economyCapacity;
        this.businessCapacity = businessCapacity;
        this.status = status;
    }

    // Basic getters
    public String getTrainId() {
        return trainId;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public int getEconomyCapacity() {
        return economyCapacity;
    }

    public int getBusinessCapacity() {
        return businessCapacity;
    }

    public TrainStatus getStatus() {
        return status;
    }

    // Allows updating the train status safely
    public void setStatus(TrainStatus status) {
        if (status == null)
            throw new IllegalArgumentException("Train status must not be null");
        this.status = status;
    }

    // Checks if the train has at least qty seats available in the given class
    public boolean hasSeats(SeatClass seatClass, int qty) {
        if (seatClass == null || qty <= 0)
            return false;

        switch (seatClass) {
            case ECONOMY:
                return economyCapacity >= qty;
            case BUSINESS:
                return businessCapacity >= qty;
            default:
                return false;
        }
    }

    // Reserves seats by reducing available capacity
    public void reserveSeats(SeatClass seatClass, int qty) {
        if (!hasSeats(seatClass, qty))
            throw new IllegalArgumentException("Not enough seats available for reservation");

        if (seatClass == SeatClass.ECONOMY) {
            economyCapacity -= qty;
        } else if (seatClass == SeatClass.BUSINESS) {
            businessCapacity -= qty;
        }
    }

    // Releases seats (for cancellations) and ensures capacity stays valid
    public void releaseSeats(SeatClass seatClass, int qty) {
        if (seatClass == null || qty <= 0)
            return;

        if (seatClass == SeatClass.ECONOMY) {
            economyCapacity += qty;
        } else if (seatClass == SeatClass.BUSINESS) {
            businessCapacity += qty;
        }

        if (economyCapacity < 0 || businessCapacity < 0)
            throw new IllegalStateException("Seat capacity became negative");

        int total = economyCapacity + businessCapacity;
        if (total > MAX_TOTAL_CAPACITY)
            throw new IllegalStateException("Total capacity exceeded maximum after releasing seats");
    }

    @Override
public String toString() {
    return  "\n========== TRAIN ==========" +
            "\nTrain ID        : " + trainId +
            "\nName            : " + name +
            "\nModel           : " + model +
            "\nEconomy Seats   : " + economyCapacity +
            "\nBusiness Seats  : " + businessCapacity +
            "\nStatus          : " + status +
            "\n===========================";
}

}
