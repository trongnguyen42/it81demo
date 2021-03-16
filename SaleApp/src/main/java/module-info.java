module com.dht.saleapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; 

    opens com.dht.saleapp to javafx.fxml;
    exports com.dht.saleapp;
}
