package sekkah;

public class Ticket {

    // Ticket information
    private String ticketId;
    private String passengerId;
    private String tripId;
    private SeatClass seatClass;
    private int quantity;
    private double totalPrice;
    private String purchaseTime;
    private TicketStatus status;

    // Constructor with full validation
    public Ticket(String ticketId, String passengerId, String tripId,
                  SeatClass seatClass, int quantity, double totalPrice,
                  String purchaseTime, TicketStatus status) {

        if (ticketId == null || ticketId.trim().isEmpty())
            throw new IllegalArgumentException("Ticket ID must not be empty");

        if (passengerId == null || passengerId.trim().isEmpty())
            throw new IllegalArgumentException("Passenger ID must not be empty");

        if (tripId == null || tripId.trim().isEmpty())
            throw new IllegalArgumentException("Trip ID must not be empty");

        if (seatClass == null)
            throw new IllegalArgumentException("Seat class must not be null");

        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");

        if (totalPrice < 0)
            throw new IllegalArgumentException("Total price cannot be negative");

        if (purchaseTime == null || purchaseTime.trim().isEmpty())
            throw new IllegalArgumentException("Purchase time must not be empty");

        if (status == null)
            throw new IllegalArgumentException("Ticket status must not be null");

        this.ticketId = ticketId.trim();
        this.passengerId = passengerId.trim();
        this.tripId = tripId.trim();
        this.seatClass = seatClass;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.purchaseTime = purchaseTime.trim();
        this.status = status;
    }

    // Basic getters
    public String getTicketId() {
        return ticketId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public String getTripId() {
        return tripId;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public TicketStatus getStatus() {
        return status;
    }

    // Change ticket status (general use)
    public void setStatus(TicketStatus newStatus) {
        if (newStatus == null)
            throw new IllegalArgumentException("Ticket status must not be null");
        this.status = newStatus;
    }

    // Cancels the ticket safely
    public void cancelTicket() {
        if (this.status == TicketStatus.CANCELLED) {
            System.out.println("Ticket " + ticketId + " is already cancelled.");
        } else {
            this.status = TicketStatus.CANCELLED;
            System.out.println("Ticket " + ticketId + " is now cancelled.");
        }
    }

    // Refunds the ticket only if already cancelled
    public void refundTicket() {
        if (this.status == TicketStatus.CANCELLED) {
            this.status = TicketStatus.REFUNDED;
            System.out.println("Ticket " + ticketId + " has been refunded.");
        } else {
            System.out.println("Ticket cannot be refunded unless it is cancelled.");
        }
    }

    @Override
public String toString() {
    return  "\n********** TICKET DETAILS **********" +
            "\nTicket ID     : " + ticketId +
            "\nPassenger ID  : " + passengerId +
            "\nTrip ID       : " + tripId +
            "\nSeat Class    : " + seatClass +
            "\nQuantity      : " + quantity +
            "\nTotal Price   : " + totalPrice +
            "\nPurchase Time : " + purchaseTime +
            "\nStatus        : " + status +
            "\n*************************************";
}

}
