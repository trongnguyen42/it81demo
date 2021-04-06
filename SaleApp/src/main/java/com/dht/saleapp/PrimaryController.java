package com.dht.saleapp;

import com.dht.pojo.Category;
import com.dht.pojo.Product;
import com.dht.service.CategoryService;
import com.dht.service.JdbcUtils;
import com.dht.service.ProductService;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController implements Initializable {
    @FXML private ComboBox<Category> cbCates;
    @FXML private TableView<Product> tbProducts;
    @FXML private TextField txtKeywords;
    @FXML private TextField txtName;
    @FXML private TextField txtPrice;

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
        
        this.tbProducts.setRowFactory(obj -> {
            TableRow r = new TableRow();
            
            r.setOnMouseClicked(evt -> {
                try {
                    Connection conn = JdbcUtils.getConn();
                    CategoryService s = new CategoryService(conn);
                    
                    Product p = this.tbProducts.getSelectionModel().getSelectedItem();
                    txtName.setText(p.getName());
                    txtPrice.setText(p.getPrice().toString());
                    cbCates.getSelectionModel().select(s.getCateById(p.getCategoryId()));
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            return r;
        });
    }
    
    public void addHandler(ActionEvent evt) {
        try {
            Connection conn = JdbcUtils.getConn();
            ProductService s = new ProductService(conn);
            
            Product p = new Product();
            p.setName(txtName.getText());
            p.setPrice(new BigDecimal(txtPrice.getText()));
            p.setCategoryId(this.cbCates.getSelectionModel().getSelectedItem().getId());
            if (s.addProduct(p) == true) {
                Utils.getBox("SUCCESSFUL", Alert.AlertType.INFORMATION).show();
                this.loadProductData("");
            } else
                Utils.getBox("FAILED", Alert.AlertType.INFORMATION).show();
            
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        
        TableColumn colAction = new TableColumn();
        colAction.setCellFactory((obj) -> {
            Button btn = new Button("Xóa");
            
            btn.setOnAction(evt -> {
                Utils.getBox("Ban chac chan xoa khong?", Alert.AlertType.CONFIRMATION)
                     .showAndWait().ifPresent(bt -> {
                         if (bt == ButtonType.OK) {
                             try {
                                 TableCell cell = (TableCell) ((Button) evt.getSource()).getParent();
                                 Product p = (Product) cell.getTableRow().getItem();
                                 
                                 Connection conn = JdbcUtils.getConn();
                                 ProductService s = new ProductService(conn);
                                 
                                 if (s.deleleProduct(p.getId())) {
                                     Utils.getBox("SUCCESSFUL", Alert.AlertType.INFORMATION).show();
                                     loadProductData("");
                                 } else
                                     Utils.getBox("FAILED", Alert.AlertType.ERROR).show();
                                 
                                 conn.close();
                             } catch (SQLException ex) {
                                 
                                 ex.printStackTrace();
                                 Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                             }
                         }
                     });
                
                
               
            });
            
            TableCell cell = new TableCell();
            cell.setGraphic(btn);
            return cell;
        });
        
        this.tbProducts.getColumns().addAll(colId, colName, colPrice, colAction);
    }
}
