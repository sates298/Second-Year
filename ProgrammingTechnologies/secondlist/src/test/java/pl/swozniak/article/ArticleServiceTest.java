package pl.swozniak.article;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import pl.swozniak.TempDatabase;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ArticleServiceTest {


    @Before
    public void setUp() throws Exception{
        ArticleService.newArticle("test", 1, new BigDecimal("1"));
    }

    @After
    public void resetDatabase() throws Exception {
        TempDatabase.setDatabase(new TempDatabase());
    }

    @Test
    public void findArticleName(){
        assertEquals("test", ArticleService.findArticle("test").getName());
    }

    @Test
    public void findArticleQuantity(){
        assertEquals(1, ArticleService.findArticle("test").getQuantity());    }

    @Test
    public void findArticlePrice() {
        assertEquals(1, ArticleService.findArticle("test").getPrice().intValue());
    }


    @Test
    public void getTotalPrice() {
        Article article = new Article();
        article.setPrice(new BigDecimal("10"));
        article.setQuantity(3);
        assertEquals(30, ArticleService.getTotalPrice(article).intValue());
    }


}