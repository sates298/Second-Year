package pl.swozniak.invoice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.swozniak.TempDatabase;
import pl.swozniak.article.ArticleService;
import pl.swozniak.client.ClientService;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class InvoiceServiceTest {

    @Before
    public void setUp() throws Exception {
        ClientService.newClient((long) 4, "Test");
        InvoiceService.newInvoice((long) 2, (long) 4);
    }

    @After
    public void tearDown() throws Exception {
        TempDatabase.setDatabase(new TempDatabase());
    }

    @Test
    public void findInvoiceId() {
        assertEquals( 2, InvoiceService.findInvoice((long) 2).getInvoiceID().intValue());
    }

    @Test
    public void findInvoiceClient() {
        assertEquals(4, InvoiceService.findInvoice((long) 2).getClient().getId().intValue());
    }

    @Test
    public void addArticle() {
        ArticleService.newArticle("test", 2, new BigDecimal("3"));
        InvoiceService.addArticle((long) 2, "test");
        assertEquals("test", InvoiceService.findInvoice((long)2).getArticles().get(0).getName());
    }


}