package hotel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;
import java.sql.ResultSet;

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
    private final SimpleStringProperty status;

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
        status = null;
    }

    public Booking(ResultSet resultSet){
        String tempStatus = "open";
        Date canceled= null;
        Date checkedIn =null;
        try {
            bookingId = resultSet.getInt("bookingID");
            guest = new Guest(resultSet);
            room = new Room(resultSet);
            arrival = resultSet.getDate("bookingFrom");
            departure = resultSet.getDate("bookingUntil");
            try{
                canceled = resultSet.getDate("bookingCanceled");
            }catch (Exception e){

            }
            try{
                checkedIn = resultSet.getDate("checkedIn");
            }catch (Exception e){

            }
        }catch(Exception e){
            System.out.println("Problem creating booking from this ResultSet");
        }
        if(checkedIn !=null && canceled != null){
            tempStatus = "Checked Out";
        }else if(canceled !=null ){
            tempStatus = "Canceled";
        } else if(checkedIn !=null){
            tempStatus = "Checked In";
        }
        roomId = new SimpleIntegerProperty(room.getId());
        name = new SimpleStringProperty(guest.getLastName() + ", " +guest.getFirstName());
        arrivalProperty = new SimpleStringProperty(arrival.toString());
        departureProperty = new SimpleStringProperty(departure.toString());
        status = new SimpleStringProperty(tempStatus);
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

   public String getStatus(){
        return status.get();
    }

}
