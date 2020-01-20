package hotel.services;

import java.sql.Date;

public class ServicesList {
    private int servicesID;
    private int bookingID;
    private String serviceType;
    private Date serviceDate;
    private String serviceName;

    public ServicesList(int servicesID, int bookingID, String serviceType, Date serviceDate, String serviceName) {
        this.servicesID = servicesID;
        this.bookingID = bookingID;
        this.serviceType = serviceType;
        this.serviceDate = serviceDate;
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return  serviceDate + " " +
                serviceType + " "
                + serviceName + " ";
    }

    public int getServicesID() {return servicesID;}

    public String getServiceName() {return serviceName;}

    public Date getServiceDate() { return serviceDate;}

    public String getServiceType() {return  serviceType;}
}
