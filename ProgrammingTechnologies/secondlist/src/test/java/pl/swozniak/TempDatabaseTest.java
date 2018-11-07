package pl.swozniak;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.swozniak.article.Article;
import pl.swozniak.client.Client;
import pl.swozniak.invoice.Invoice;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TempDatabaseTest {

    private TempDatabase tempDatabase;

    @Before
    public void setUp() throws Exception {
            tempDatabase = new TempDatabase();
    }

    @After
    public void tearDown() throws Exception {
        tempDatabase = null;
    }

    @Test
    public void setDatabase() {
        TempDatabase.setDatabase(tempDatabase);
        assertEquals(tempDatabase, TempDatabase.getDatabase());
    }

    @Test
    public void getAllArticles() {
        assertEquals(new ArrayList<Article>(), tempDatabase.getAllArticles());
    }

    @Test
    public void getAllInvoices() {
        assertEquals(new ArrayList<Invoice>(), tempDatabase.getAllInvoices());
    }

    @Test
    public void getAllClients() {
        assertEquals(new ArrayList<Client>(), tempDatabase.getAllClients());
    }
}