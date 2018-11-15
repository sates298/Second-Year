package pl.swozniak;

import pl.swozniak.client.ClientService;

import java.math.BigDecimal;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Controller c = new Controller();

        c.newArticle("rock", 20, new BigDecimal("12.0"));
        c.newArticle("sand", 3000, new BigDecimal("1.1"));
        c.newArticle("cliff", 4, new BigDecimal("20000.50"));


        boolean exit = true;
        long clientId, invoiceId;
        String clientName, option, e;
        while(exit) {


            System.out.println("wprowadź id klienta: ");
            clientId = sc.nextLong();
            if (ClientService.findClient(clientId) == null) {
                System.out.println("wprowadz nazwe clienta: ");
                clientName = sc.next();
                c.newClient(clientId, clientName);
            }
            System.out.println("jesli chcesz dodac fakture wybierz 'd' jesli chcesz wyswietlic dla tego klienta jego faktury wybierz 'p', jesli chcesz wysztko wyswietlic wybierz 'a'");
            option = sc.next();
            switch (option.charAt(0)) {
                case 'd':
                    System.out.println("wprowadz id nowej faktury: ");
                    invoiceId = sc.nextLong();
                    c.newInvoice(invoiceId, clientId);
                    int quantity;
                    String articleName;
                    System.out.println("wprowadz ilosc artykułów ktore chcesz dodac do faktury");
                    quantity = sc.nextInt();

                    for (int i = 0; i < quantity; i++) {
                        System.out.println("wprowadz naze artykułu który chcesz dodac:");
                        articleName = sc.next();
                        c.addArticleToInvoice(invoiceId, articleName);
                    }
                    break;
                case 'p':
                    System.out.println(c.showOneClient(clientId));
                    break;
                case 'a':
                    System.out.println(c.showAllClients());
                    break;
                default:
                    System.out.println("blad");

            }
            System.out.println("jesli chcesz wyjsc wybierz 'y' inaczej wybierz co innego");
            e=sc.next();
            if(e.equals("y")){
                exit=false;
            }
        }


   /*     c.newClient((long) 1, "first");
        c.newClient((long) 2, "second");
        c.newInvoice((long) 1, (long) 1);
        c.newInvoice((long) 2, (long) 1);
        c.newInvoice((long) 3, (long) 2);1

        c.addArticleToInvoice((long) 1, "sand");
        c.addArticleToInvoice((long) 1, "cliff");
        c.addArticleToInvoice((long) 2, "sand");
        c.addArticleToInvoice((long) 3, "rock");
        c.addArticleToInvoice((long) 3, "cliff");

        System.out.println(c.showOneClient((long) 1));
        System.out.println("#######################################################\n");
        System.out.println(c.showAllClients());
        System.out.println("#######################################################\n");
        System.out.println(c.showOneClient((long) 2));
*/
    }
}
