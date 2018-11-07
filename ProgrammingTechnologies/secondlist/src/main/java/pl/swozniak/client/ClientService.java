package pl.swozniak.client;

import pl.swozniak.TempDatabase;
import pl.swozniak.invoice.Invoice;
import pl.swozniak.invoice.InvoiceService;

public class ClientService {


    public static void newClient(Long id, String name){

        Client client = findClient(id);
        if(client != null){
            System.out.println("Error! This ID is already occupied!");
        }else {
            client = new Client(id, name);
            TempDatabase.getDatabase().getAllClients().add(client);
        }
    }

    public static void addInvoice(Client client, Invoice invoice){
        client.getInvoices().add(invoice);
    }

    public static Client findClient(Long clientId){
        for(Client client: TempDatabase.getDatabase().getAllClients()){
            if(client.getId().equals(clientId)){
                return client;
            }
        }
        //System.out.println("Error! That client doesn't exists!");
        return null;
    }

    public static String toString(Client client){
        StringBuilder invoices = new StringBuilder();

        for(Invoice invoice: client.getInvoices()){
            invoices.append(InvoiceService.toString(invoice));
        }

        return "Client's id: " + client.getId()
                + "\n Client's name: " + client.getName()
                + '\n' + invoices;
    }


}
