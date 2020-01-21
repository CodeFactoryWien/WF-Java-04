package hotel;

import java.sql.ResultSet;
import java.time.LocalDate;

public class Guest {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private int zipCode;
    private String country;
    private String phone;
    private String email;
    private String passportNumber;

    public Guest(int id, String firstName, String lastName, LocalDate birthDate, String address, int zipCode,
                 String country, String phone, String email, String passportNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.zipCode = zipCode;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.passportNumber = passportNumber;
    }

    public Guest(ResultSet resultSet){
        try {
            id = resultSet.getInt("guestID");
            firstName = resultSet.getString("firstname");
            lastName = resultSet.getString("lastName");
            birthDate = resultSet.getDate("birthDate").toLocalDate();
            address = resultSet.getString("address");
            zipCode = resultSet.getInt("zipCode");
            country = resultSet.getString("country");
            phone = resultSet.getString("phoneNumber");
            email = resultSet.getString("email");
            passportNumber = resultSet.getString("passportNr");
        } catch (Exception e){
            System.out.println("Can't create Guest from this ResultSet");
        }
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassportNumber() {
        return passportNumber;
    }
}