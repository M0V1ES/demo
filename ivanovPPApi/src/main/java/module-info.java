module com.example.ivanovppapi {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires odfdom.java;


    opens com.example.ivanovppapi to javafx.fxml;
    exports com.example.ivanovppapi;
}