/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dht.service;

import com.dht.pojo.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 *
 * @author Admin
 */
public class ProductTester {
    private static Connection conn;
    
    @BeforeAll
    public static void setUpClass() {
        try {
            conn = JdbcUtils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(ProductTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterAll
    public static void tearDownClass() {
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductTester.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    @Test
    public void testWithKeyWord() {
        try {
            ProductService s = new ProductService(conn);
            List<Product> products = s.getProducts("iphone");
            
            products.forEach(p -> {
                Assertions.assertTrue(p.getName().toLowerCase().contains("iphone"));
            });
        } catch (SQLException ex) {
            Logger.getLogger(ProductTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testUnknownWithKeyWord() {
        try {
            ProductService s = new ProductService(conn);
            List<Product> products = s.getProducts("43*&^&^GYGFUYGFUYGFHGD%$");
            
            Assertions.assertEquals(0, products.size());
        } catch (SQLException ex) {
            Logger.getLogger(ProductTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testException() {
        Assertions.assertThrows(SQLDataException.class, () -> {
            new ProductService(conn).getProducts(null);
        });
    }
    
    @Test
    public void testTimeout() {
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            new ProductService(conn).getProducts("");
        });
    }
    
    @Test
    @DisplayName("Kiem thu chuc nang them sp voi name=null")
    @Tag("critical")
    public void testAddProductNameNull() {
        try {
            Product p = new Product();
            p.setName(null);
            p.setPrice(new BigDecimal(100));
            p.setCategoryId(1);
            
            ProductService s = new ProductService(conn);
            Assertions.assertFalse(s.addProduct(p));
        } catch (SQLException ex) {
            Logger.getLogger(ProductTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testAddProductInvalidCate() {
        try {
            Product p = new Product();
            p.setName("TEST PRODUCT");
            p.setPrice(new BigDecimal(100));
            p.setCategoryId(999);
            
            ProductService s = new ProductService(conn);
            Assertions.assertFalse(s.addProduct(p));
        } catch (SQLException ex) {
            Logger.getLogger(ProductTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testAddProduct() {
        try {
            Product p = new Product();
            p.setName("TEST PRODUCT");
            p.setPrice(new BigDecimal(100));
            p.setCategoryId(1);
            
            ProductService s = new ProductService(conn);
            Assertions.assertTrue(s.addProduct(p));
        } catch (SQLException ex) {
            Logger.getLogger(ProductTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @ParameterizedTest
    @CsvSource({"p1,200,1", "p2,200,2"})
    public void testAddBatchProduct(String name, BigDecimal price, int cateId) {
        try {
            Product p = new Product();
            p.setName(name);
            p.setPrice(price);
            p.setCategoryId(cateId);
            
            ProductService s = new ProductService(conn);
            Assertions.assertTrue(s.addProduct(p));
        } catch (SQLException ex) {
            Logger.getLogger(ProductTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
