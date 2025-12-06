package sekkah;

public class Passenger extends User {

    // Passenger-specific information
    private int age;
    private String email;        
    private String phoneNumber;  
    private String nationalId;

    // Constructor with input validation
    public Passenger(String id, String name, String username, String password,
                     int age, String email, String phoneNumber, String nationalId) {

        // Assigns core user fields and locks role to PASSENGER
        super(id, name, username, password, Role.PASSENGER);

        // Age validation
        if (age < 0 || age > 120)
            throw new IllegalArgumentException("Age must be between 0 and 120");

        // Email is optional; if empty or null, store as null
        if (email != null && email.trim().isEmpty())
            email = null;

        // Phone must be exactly 10 digits
        if (!isValidPhoneNumber(phoneNumber))
            throw new IllegalArgumentException("Phone number must be 10 digits");

        // National ID validation
        if (nationalId == null || nationalId.trim().isEmpty())
            throw new IllegalArgumentException("National ID must not be empty");

        this.age = age;
        this.email = (email == null ? null : email.trim());
        this.phoneNumber = phoneNumber.trim();
        this.nationalId = nationalId.trim();
    }

    // Basic getters
    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email; // may be null (optional)
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getNationalId() {
        return nationalId;
    }

    // Safe update of contact information
    public void updateContactInfo(String email, String phoneNumber) {

        // Email can be optional again when updating
        if (email != null && email.trim().isEmpty())
            email = null;

        // Phone must still be exactly 10 digits
        if (!isValidPhoneNumber(phoneNumber))
            throw new IllegalArgumentException("Phone number must be 10 digits");

        this.email = (email == null ? null : email.trim());
        this.phoneNumber = phoneNumber.trim();
    }

    // Helper: validates that phone number is exactly 10 digits
    private boolean isValidPhoneNumber(String num) {
        if (num == null) return false;
        num = num.trim();
        if (num.length() != 10) return false;
        return num.matches("\\d{10}");
    }

    @Override
public String toString() {
    return  "\n========== PASSENGER ==========" +
            "\nID          : " + getId() +
            "\nName        : " + getName() +
            "\nUsername    : " + getUsername() +
            "\nAge         : " + age +
            "\nEmail       : " + (email == null ? "-" : email) +
            "\nPhone       : " + phoneNumber +
            "\nNational ID : " + nationalId +
            "\nRole        : " + getRole() +
            "\n================================";
}

}
