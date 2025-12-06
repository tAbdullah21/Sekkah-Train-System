package sekkah;

public class Admin extends User {

    // Admin-specific information
    private String staffId;
    private String department;

    // Constructor with validation, role is fixed to ADMIN
    public Admin(String id, String name, String username, String password,
                 String staffId, String department) {

        // All admins have role ADMIN
        super(id, name, username, password, Role.ADMIN);

        if (staffId == null || staffId.trim().isEmpty())
            throw new IllegalArgumentException("Staff ID must not be empty");

        if (department == null || department.trim().isEmpty())
            throw new IllegalArgumentException("Department must not be empty");

        this.staffId = staffId.trim();
        this.department = department.trim();
    }

    // Basic getters
    public String getStaffId() {
        return staffId;
    }

    public String getDepartment() {
        return department;
    }

    // Allows updating the department safely
    public void updateDepartment(String newDepartment) {
        if (newDepartment == null || newDepartment.trim().isEmpty())
            throw new IllegalArgumentException("Department must not be empty");
        this.department = newDepartment.trim();
    }

    // Admin actions: input is validated before performing the action
    public void addTrain(String trainId) {
        if (trainId == null || trainId.trim().isEmpty()) {
            System.out.println("Cannot add train: invalid train ID.");
            return;
        }
        System.out.println("Admin " + getName() + " is adding train with ID: " + trainId.trim());
        // Later: connect to BookingManager to actually store the train
    }

    public void removeTrain(String trainId) {
        if (trainId == null || trainId.trim().isEmpty()) {
            System.out.println("Cannot remove train: invalid train ID.");
            return;
        }
        System.out.println("Admin " + getName() + " is removing train with ID: " + trainId.trim());
        // Later: connect to BookingManager to actually remove the train
    }

    public void addTrip(String tripId) {
        if (tripId == null || tripId.trim().isEmpty()) {
            System.out.println("Cannot add trip: invalid trip ID.");
            return;
        }
        System.out.println("Admin " + getName() + " is adding trip with ID: " + tripId.trim());
        // Later: connect to BookingManager to actually store the trip
    }

    public void removeTrip(String tripId) {
        if (tripId == null || tripId.trim().isEmpty()) {
            System.out.println("Cannot remove trip: invalid trip ID.");
            return;
        }
        System.out.println("Admin " + getName() + " is removing trip with ID: " + tripId.trim());
        // Later: connect to BookingManager to actually remove the trip
    }

    @Override
public String toString() {
    return  "\n========== ADMIN ==========" +
            "\nID         : " + getId() +
            "\nName       : " + getName() +
            "\nUsername   : " + getUsername() +
            "\nStaff ID   : " + staffId +
            "\nDepartment : " + department +
            "\nRole       : " + getRole() +
            "\n===========================";
}

}
