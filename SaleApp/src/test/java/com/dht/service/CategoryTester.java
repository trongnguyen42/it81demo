/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dht.service;

import com.dht.pojo.Category;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Admin
 */
public class CategoryTester {
    private static Connection CONN;
    
    @BeforeAll
    public static void setUp() {
        try {
            CONN = JdbcUtils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterAll
    public static void tearDown() {
        if (CONN != null)
            try {
                CONN.close();
            } catch (SQLException ex) {
                Logger.getLogger(CategoryTester.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    @Test
    public void testQuantity() {
        try {
            int expected = 3;
            CategoryService s = new CategoryService(CONN);
            List<Category> cates = s.getCates();
            
            System.out.println(cates);
            Assertions.assertTrue(cates.size() >= expected);
        } catch (SQLException ex) {
            Logger.getLogger(CategoryTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testUniqueName() {
         try {
            CategoryService s = new CategoryService(CONN);
            List<Category> cates = s.getCates();
            
            List<String> names = new ArrayList<>();
            cates.forEach(c -> {
                names.add(c.getName());
             });
            
            Set<String> names2 = new HashSet<>(names);
            
            Assertions.assertTrue(names.size() == names2.size());
         } catch (SQLException ex) {
            Logger.getLogger(CategoryTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
