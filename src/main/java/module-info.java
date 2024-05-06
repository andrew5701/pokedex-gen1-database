module com.mycompany.databaseexample {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.json;
    requires javafx.media;
    requires java.net.http;


    opens com.mycompany.databaseexample to javafx.fxml;
    exports com.mycompany.databaseexample;
}