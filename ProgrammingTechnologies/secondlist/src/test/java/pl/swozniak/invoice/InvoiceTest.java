package pl.swozniak.invoice;

import org.junit.*;
import pl.swozniak.article.Article;
import pl.swozniak.client.Client;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class InvoiceTest {

    private Invoice invoice;
    private Client client;
    private Date date;

    @Before
    public  void setUp() throws Exception {
        invoice = new Invoice();
        client = new Client((long) 1, "test");

    }

    @After
    public void tearDown() throws Exception {
        invoice = null;
        client = null;
        date = null;
    }

    @Test
    public void setInvoiceID() {
        invoice.setInvoiceID((long) 1);
        assertEquals(1, invoice.getInvoiceID().intValue());
    }

    @Test
    public void setClient() {
        invoice.setClient(client);
        assertEquals(client, invoice.getClient());
    }

    @Test
    public void setDate() {
        invoice.setDate(date);
        assertEquals(date, invoice.getDate());
    }
    @Test
    public void getArticles(){
        assertEquals(new ArrayList<Article>(), invoice.getArticles());
    }
}