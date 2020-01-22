package hotel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;



public class InvoiceTable {

    private final SimpleIntegerProperty servicesID, quant, pps, price;
    private final SimpleStringProperty serviceType, serviceName, serviceDate ;


    public InvoiceTable(int servicesID,  String serviceDate,  int quant, String serviceType, String serviceName, int pps, int price ) {
        this.servicesID = new SimpleIntegerProperty(servicesID);
        this.serviceDate = new SimpleStringProperty(serviceDate);
        this.quant = new SimpleIntegerProperty(quant);
        this.serviceType = new SimpleStringProperty(serviceType);
        this.serviceName = new SimpleStringProperty(serviceName);
        this.pps = new SimpleIntegerProperty(pps);
        this.price = new SimpleIntegerProperty(price);
    }

    public int getServicesID() {
        return servicesID.get();
    }

    public SimpleIntegerProperty servicesIDProperty() {
        return servicesID;
    }

    public int getQuant() {
        return quant.get();
    }

    public SimpleIntegerProperty quantProperty() {
        return quant;
    }

    public int getPps() {
        return pps.get();
    }

    public SimpleIntegerProperty ppsProperty() {
        return pps;
    }

    public int getPrice() {
        return price.get();
    }

    public SimpleIntegerProperty priceProperty() {
        return price;
    }

    public String getServiceType() {
        return serviceType.get();
    }

    public SimpleStringProperty serviceTypeProperty() {
        return serviceType;
    }

    public String getServiceName() {
        return serviceName.get();
    }

    public SimpleStringProperty serviceNameProperty() {
        return serviceName;
    }

    public String getServiceDate() {
        return serviceDate.get();
    }

    public SimpleStringProperty serviceDateProperty() {
        return serviceDate;
    }
}
