package controller;

public class mainController {

    // Create new Guest //
    public void call_createGuestController() throws Exception {
       createGuestController C = new createGuestController();
       C.start();
    }

    // Create new Room //
    public void call_createRoomController() throws Exception {
        createRoomController C = new createRoomController();
        C.start();
    }

    // Create new Booking //
    public void call_createBookingController() throws Exception {
        createBookingController C = new createBookingController();
        C.start();
    }
}
