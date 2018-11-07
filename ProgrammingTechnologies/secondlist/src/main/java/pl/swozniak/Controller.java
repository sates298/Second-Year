package pl.swozniak;

import pl.swozniak.article.ArticleService;
import pl.swozniak.client.Client;
import pl.swozniak.client.ClientService;
import pl.swozniak.invoice.InvoiceService;

import java.math.BigDecimal;

public class Controller {

    public String showOneClient(Long clientId){
        Client client = ClientService.findClient(clientId);
        if(client != null){
            return ClientService.toString(client);
        }else {
            return "Error! That client doesn't exist! \n";
        }
    }

    public String showAllClients(){
        StringBuilder all = new StringBuilder();

        for(Client client : TempDatabase.getDatabase().getAllClients()){
            all.append(ClientService.toString(client)).append("---------------------------------\n");
        }
        return "All Clients: \n" + all;
    }

    public void newClient(Long id, String name){
        ClientService.newClient(id, name);
    }
    public void newArticle(String name, int quantity, BigDecimal price){
        ArticleService.newArticle(name, quantity, price);
    }
    public void newInvoice(Long invoiceId,Long clientId){
        InvoiceService.newInvoice(invoiceId, clientId);
    }

    public void addArticleToInvoice(Long invoiceId, String articleName){
        InvoiceService.addArticle(invoiceId, articleName);
    }


}
