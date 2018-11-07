package pl.swozniak.client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.swozniak.invoice.Invoice;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ClientTest {

    private Client client;

    @Before
    public void setUp() throws Exception {
        client = new Client((long) 1, "test");
    }

    @After
    public void tearDown() throws Exception {
        client = null;
    }

    @Test
    public void getId() {
        assertEquals(1, client.getId().intValue());
    }

    @Test
    public void getName() {
        assertEquals("test", client.getName());
    }

    @Test
    public void getInvoices(){
        assertEquals(new ArrayList<Invoice>(), client.getInvoices());
    }

}