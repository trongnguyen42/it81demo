package com.dht.saleapp;

import com.dht.pojo.Category;
import com.dht.pojo.Product;
import com.dht.service.CategoryService;
import com.dht.service.JdbcUtils;
import com.dht.service.ProductService;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class PrimaryController implements Initializable {
    @FXML private ComboBox<Category> cbCates;
    @FXML private TableView<Product> tbProducts;
    @FXML private TextField txtKeywords;
//
//    @FXML
//    private void switchToSecondary() throws IOException {
//        App.setRoot("secondary");
//    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection conn = JdbcUtils.getConn();
            CategoryService s = new CategoryService(conn);
            
            this.cbCates.setItems(FXCollections.observableList(s.getCates()));
            
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        loadTables();
        loadProductData("");
        
        this.txtKeywords.textProperty().addListener((obj) -> {
            loadProductData(this.txtKeywords.getText());
        });
    }
    
    public void loadProductData(String kw) {
        try {
            this.tbProducts.getItems().clear();
            
            Connection conn = JdbcUtils.getConn();
            ProductService s = new ProductService(conn);
            
            this.tbProducts.setItems(FXCollections.observableList(s.getProducts(kw)));
            
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadTables() {
        TableColumn colId = new TableColumn("Mã SP");
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        
        TableColumn colName = new TableColumn("Tên SP");
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        
        TableColumn colPrice = new TableColumn("Gía SP");
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));
        
        this.tbProducts.getColumns().addAll(colId, colName, colPrice);
    }
}
