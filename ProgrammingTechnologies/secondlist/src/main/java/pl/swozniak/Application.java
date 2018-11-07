package pl.swozniak;

import java.math.BigDecimal;

public class Application {

    public static void main(String[] args) {

        Controller c = new Controller();


        c.newClient((long)1, "first");
        c.newClient((long) 2, "second");
        c.newInvoice((long) 1, (long) 1);
        c.newInvoice((long) 2, (long) 1);
        c.newInvoice((long) 3, (long) 2);
        c.newArticle("rock", 20, new BigDecimal("12.0"));
        c.newArticle("sand", 3000, new BigDecimal("1.1"));
        c.newArticle("cliff", 4, new BigDecimal("20000.50"));
        c.addArticleToInvoice((long) 1, "sand");
        c.addArticleToInvoice((long) 1, "cliff");
        c.addArticleToInvoice((long) 2, "sand");
        c.addArticleToInvoice((long) 3, "rock");
        c.addArticleToInvoice((long) 3, "cliff");

        System.out.println(c.showOneClient((long)1));
        System.out.println("#######################################################\n");
        System.out.println(c.showAllClients());
        System.out.println("#######################################################\n");
        System.out.println(c.showOneClient((long) 2));

    }
}
