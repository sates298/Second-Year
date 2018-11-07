package pl.swozniak.article;

import pl.swozniak.TempDatabase;

import java.math.BigDecimal;

public class ArticleService {
    public static void newArticle(String articleName, int articleQuantity, BigDecimal articlePrice) {

        if (ArticleService.findArticle(articleName) != null) {
            System.out.println("Error! This article_name is already occupied!");
            return;
        }

        Article article = new Article();
        article.setName(articleName);
        article.setQuantity(articleQuantity);
        article.setPrice(articlePrice);
        TempDatabase.getDatabase().getAllArticles().add(article);
    }

    public static Article findArticle(String name) {
        for (Article article : TempDatabase.getDatabase().getAllArticles()) {
            if (article.getName().equals(name)) {
                return article;
            }
        }
        //System.out.println("Error! That article doesn't exists!");
        return null;
    }

    public static BigDecimal getTotalPrice(Article article) {
        return article.getPrice().multiply(new BigDecimal(article.getQuantity()));
    }

    public static String toString(Article article) {
        return "Name: " + article.getName()
                + ", Price: " + article.getPrice()
                + ", Quantity: " + article.getQuantity() + '\n';
    }
}
