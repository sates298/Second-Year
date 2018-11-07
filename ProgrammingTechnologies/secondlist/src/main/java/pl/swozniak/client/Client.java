package pl.swozniak.client;

import pl.swozniak.invoice.Invoice;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private Long id;
    private String name;
    private List<Invoice> invoices;

    public Client(Long id,String name) {
        this.id = id;
        this.name = name;
        this.invoices = new ArrayList<Invoice>();
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }
}
