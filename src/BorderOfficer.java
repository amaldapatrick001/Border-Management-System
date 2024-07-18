public class BorderOfficer {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private String station;

    public BorderOfficer(int id, String firstName, String lastName, String username, String email, String phoneNumber, String station) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.station = station;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStation() {
        return station;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + username + ")";
    }
}
