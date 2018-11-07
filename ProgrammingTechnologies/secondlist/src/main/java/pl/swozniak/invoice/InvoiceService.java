package pl.swozniak.invoice;

import pl.swozniak.TempDatabase;
import pl.swozniak.article.Article;
import pl.swozniak.article.ArticleService;
import pl.swozniak.client.Client;
import pl.swozniak.client.ClientService;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceService {

    public static void newInvoice(Long invoiceId, Long clientId) {


        if (findInvoice(invoiceId) != null) {
            System.out.println("Error! This invoice_ID is already occupied");
            return;
        }

        Client client = ClientService.findClient(clientId);
        if (client != null) {
            Invoice invoice = new Invoice();
            invoice.setInvoiceID(invoiceId);
            invoice.setClient(client);
            invoice.setDate(new Date());
            ClientService.addInvoice(client, invoice);
            TempDatabase.getDatabase().getAllInvoices().add(invoice);
        }else {
            System.out.println("Error! Client doesn't exist in database!");
        }

    }

    public static Invoice findInvoice(Long invoiceId) {
        for (Invoice invoice : TempDatabase.getDatabase().getAllInvoices()) {
            if (invoice.getInvoiceID().equals(invoiceId)) {
                return invoice;
            }
        }
        //System.out.println("Error! That invoice doesn't exists!");
        return null;
    }

    private static BigDecimal totalPrice(Invoice invoice) {
        BigDecimal total = BigDecimal.ZERO;

        for (Article article : invoice.getArticles()) {
            total = total.add(ArticleService.getTotalPrice(article));
        }

        return total;
    }

    public static void addArticle(Long invoiceId, String articleName) {

        Invoice invoice = findInvoice(invoiceId);
        Article article = ArticleService.findArticle(articleName);
        if (invoice != null) {

            if (article != null) {
                invoice.getArticles().add(article);
            } else {
                System.out.println("Error! Article doesn't exist in database!");
            }

        } else {
            System.out.println("Error! Invoice doesn't exist in database!");
        }
    }

    public static String toString(Invoice invoice) {
        SimpleDateFormat sdf = new SimpleDateFormat();

        return "Invoice: " + invoice.getInvoiceID()
                //+ "\n Client's Id: " + invoice.getClient().getId()
               // + "\n Client's name: " + invoice.getClient().getName()
                + "\n Invoice was concluded on " + sdf.format(invoice.getDate())
                + "\n Total price: " + totalPrice(invoice)
                + "\n" + articlesToString(invoice) + '\n';
    }

    private static String articlesToString(Invoice invoice) {
        StringBuilder articles = new StringBuilder();

        for (Article article : invoice.getArticles()) {
            articles.append("-> ").append(ArticleService.toString(article));
        }

        return "Articles : \n" + articles;
    }
}
