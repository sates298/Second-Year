package pl.swozniak;

import pl.swozniak.article.Article;
import pl.swozniak.client.Client;
import pl.swozniak.invoice.Invoice;

import java.util.ArrayList;
import java.util.List;

public class TempDatabase {

    private static TempDatabase database = new TempDatabase();

    private List<Article> allArticles;
    private List<Invoice> allInvoices;
    private List<Client> allClients;

    public static TempDatabase getDatabase() {
        return database;
    }

    public static void setDatabase(TempDatabase tempDatabase){
        database = tempDatabase;
    }

    public TempDatabase() {

        this.allArticles = new ArrayList<Article>();
        this.allClients = new ArrayList<Client>();
        this.allInvoices = new ArrayList<Invoice>();
    }

    public List<Article> getAllArticles() {
        return allArticles;
    }

    public List<Invoice> getAllInvoices() {
        return allInvoices;
    }

    public List<Client> getAllClients() {
        return allClients;
    }

}
