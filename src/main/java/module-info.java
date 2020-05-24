module org.examples {
        requires javafx.controls;
        requires javafx.fxml;
        requires org.tinylog.api;
        requires org.json;
        requires junit;



        opens org.example to javafx.fxml;
        exports org.example;
        exports Test;
        }