/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dht.service;

import com.dht.pojo.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class CategoryService {
    private Connection conn;
    
    public CategoryService(Connection conn) {
        this.conn = conn;
    }
    public List<Category> getCates() throws SQLException {
        Statement stm = this.conn.createStatement();
        ResultSet r = stm.executeQuery("SELECT * FROM category");
        
        List<Category> re = new ArrayList<>();
        while (r.next()) {
            Category c = new Category();
            c.setId(r.getInt("id"));
            c.setName(r.getString("name"));
            c.setDescription(r.getString("description"));
            
            re.add(c);
        }
        return re;
    }
    
    public Category getCateById(int id) throws SQLException {
        String sql = "SELECT * FROM category WHERE id=?";
        PreparedStatement stm = this.conn.prepareStatement(sql);
        stm.setInt(1, id);
        
        ResultSet rs = stm.executeQuery();
        Category c = null;
        while (rs.next()) {
            c = new Category();
            c.setId(rs.getInt("id"));
            c.setName(rs.getString("name"));
            
        }
        
        return c;
    }
}
