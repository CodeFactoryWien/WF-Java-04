package hotel;

import java.sql.Date;

public class Invoice {
    private int invoiceID, fk_bookingID, fk_guestID, fk_customerID, nights, priceNight;
    private Date arrival, depature;

    public void InvoiceTable(int invoiceID, int fk_bookingID, int fk_guestID, int fk_customerID, int nights, int priceNight, Date arrival, Date depature) {
        this.invoiceID = invoiceID;
        this.fk_bookingID = fk_bookingID;
        this.fk_guestID = fk_guestID;
        this.fk_customerID = fk_customerID;
        this.nights = nights;
        this.priceNight = priceNight;
        this.arrival = arrival;
        this.depature = depature;
    }

    public int getInvoiceID() {
        return invoiceID;
    }
}