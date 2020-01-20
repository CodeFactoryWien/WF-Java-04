package hotel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;

public class Booking {
    private int bookingId;
    private Guest guest;
    private Room room;
    private Date arrival;
    private Date departure;

    private final SimpleIntegerProperty roomId;
    private final SimpleStringProperty name;
    private final SimpleStringProperty arrivalProperty;
    private final SimpleStringProperty departureProperty;

    public Booking(int bookingId, Guest guest, Room room, Date arrival, Date departure) {
        this.bookingId = bookingId;
        this.guest = guest;
        this.room = room;
        this.arrival = arrival;
        this.departure = departure;
        roomId = new SimpleIntegerProperty(room.getId());
        name = new SimpleStringProperty(guest.getLastName() + ", " +guest.getFirstName());
        arrivalProperty = new SimpleStringProperty(arrival.toString());
        departureProperty = new SimpleStringProperty(departure.toString());
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public int getBookingId() {
        return bookingId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public Date getArrival() {
        return arrival;
    }

    public Date getDeparture() {
        return departure;
    }

    public String toString(){
        return guest.getLastName() + ", " +guest.getFirstName() + " in room number " + room.getId();
    }

   public Integer getRoomId(){
        return roomId.get();
   }

   public String getName(){
        return name.get();
   }

   public String getArrivalProperty(){
        return arrivalProperty.get();
   }

   public String getDepartureProperty(){
        return departureProperty.get();
   }

}
