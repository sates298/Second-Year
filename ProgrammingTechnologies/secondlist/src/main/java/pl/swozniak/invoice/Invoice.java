package pl.swozniak.invoice;

import pl.swozniak.article.Article;
import pl.swozniak.client.Client;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Invoice {
    private Long invoiceID;
    private Client client;
    private Date date;
    private List<Article> articles;

    public Invoice() {
        this.articles = new ArrayList<Article>();
    }

    public Long getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Long invoiceID) {
        this.invoiceID = invoiceID;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Article> getArticles() {
        return articles;
    }

}
