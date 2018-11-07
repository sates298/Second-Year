package pl.swozniak.article;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ArticleTest {

    private Article article;

    @Before
    public void setUp() throws Exception {
        article = new Article();
    }

    @After
    public void tearDown() throws Exception {
        article = null;
    }

    @Test
    public void setName() {
        article.setName("test");
        assertEquals("test", article.getName());
    }

    @Test
    public void setQuantity() {
        article.setQuantity(1);
        assertEquals(1, article.getQuantity());
    }

    @Test
    public void setPrice() {
        article.setPrice(new BigDecimal("10"));
        assertEquals(new BigDecimal("10"), article.getPrice());
    }
}