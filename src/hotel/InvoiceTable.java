package hotel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;



public class InvoiceTable {

    private final SimpleIntegerProperty servicesID, quant;
    private final SimpleStringProperty serviceType, serviceName, serviceDate, pps, price ;


    public InvoiceTable(int servicesID,  String serviceDate,  int quant, String serviceType, String serviceName, int pps, int price ) {
        this.servicesID = new SimpleIntegerProperty(servicesID);
        this.serviceDate = new SimpleStringProperty(serviceDate);
        this.quant = new SimpleIntegerProperty(quant);
        this.serviceType = new SimpleStringProperty(serviceType);
        this.serviceName = new SimpleStringProperty(serviceName);
        this.pps = new SimpleStringProperty(String.format("%.2f", (double)pps/100) + " €");
        this.price = new SimpleStringProperty(String.format("%.2f", (double)price/100) + " €");
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

    public String getPps() {
        return pps.get();
    }

    public SimpleStringProperty ppsProperty() {
        return pps;
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
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
