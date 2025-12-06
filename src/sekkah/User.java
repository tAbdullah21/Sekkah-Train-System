package sekkah;

public abstract class User {

    // Minimum password length requirement
    public static final int MIN_PASSWORD_LENGTH = 6;

    // Basic user attributes
    private String id;
    private String name;
    private String username;
    private String password;
    private Role role;

    // Constructor: validates and initializes user data
    public User(String id, String name, String username,
                String password, Role role) {

        if (id == null || id.isEmpty())
            throw new IllegalArgumentException("Invalid id");

        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid name");

        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Invalid username");

        if (role == null)
            throw new IllegalArgumentException("Role cannot be null");

        if (!isValidPassword(password))
            throw new IllegalArgumentException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters."
            );

        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Basic getters for user information
    public String getId() { return id; }

    public String getName() { return name; }

    public String getUsername() { return username; }

    public Role getRole() { return role; }

    // Verifies username and password match this user's credentials
    public boolean authenticate(String username, String password) {
        if (username == null || password == null)
            return false;

        if (!this.username.equals(username))
            return false;

        return this.password.equals(password);
    }

    // Checks password rule
    public static boolean isValidPassword(String pw) {
        return pw != null && pw.length() >= MIN_PASSWORD_LENGTH;
    }

    @Override
public String toString() {
    return  "\n========== USER ==========" +
            "\nID       : " + id +
            "\nName     : " + name +
            "\nUsername : " + username +
            "\nRole     : " + role +
            "\n==========================";
}

}
