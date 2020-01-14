package hotel;

import java.sql.Date;

public class Guest {
    private int id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String adress;
    private int zipCode;
    private String country;
    private String phone;
    private String email;
    private int passportNumber;

    public Guest(int id, String firstName, String lastName, Date birthDate, String adress, int zipCode,
                 String country, String phone, String email, int passportNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.adress = adress;
        this.zipCode = zipCode;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.passportNumber = passportNumber;
    }
}
