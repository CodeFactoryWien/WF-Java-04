package controller;

public class mainController {

    public void call_createGuestController() throws Exception {
       createGuestController C = new createGuestController();
       C.start();
    }

    public void call_createRoomController() throws Exception {
        createRoomController C = new createRoomController();
        C.start();
    }

    public void call_createBookingController() throws Exception {
        createBookingController C = new createBookingController();
        C.start();
    }
}
