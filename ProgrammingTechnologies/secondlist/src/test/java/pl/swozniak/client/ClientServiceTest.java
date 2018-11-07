package pl.swozniak.client;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import pl.swozniak.TempDatabase;
import pl.swozniak.invoice.Invoice;

import static org.junit.Assert.*;

public class ClientServiceTest {


    @AfterClass
    public static void resetDatabase()  {
        TempDatabase.setDatabase(new TempDatabase());
    }

    @Test
    public void findClientId(){
        ClientService.newClient((long) 5, "testFind");
        assertEquals(5, ClientService.findClient((long) 5).getId().intValue());
    }

    @Test
    public void addInvoice() {
        Client client = new Client((long) 1, "test");
        Invoice invoice = new Invoice();
        ClientService.addInvoice(client, invoice);
        assertEquals(invoice, client.getInvoices().get(0));
    }

}