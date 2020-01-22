package hotel;

public class InvoiceTable {
    private int quant, pricePerService, price;
    private String service;

    public InvoiceTable(int quant, int pricePerService, int price, String service) {
        this.quant = quant;
        this.pricePerService = pricePerService;
        this.price = price;
        this.service = service;
    }
}
