module se.dykstrom.javafx.dadjoke {
    requires com.gluonhq.connect;
    requires com.google.gson;
    requires javafx.controls;
    requires javafx.fxml;

    opens se.dykstrom.javafx.dadjoke to javafx.fxml;
    opens se.dykstrom.javafx.dadjoke.model to com.google.gson;
    exports se.dykstrom.javafx.dadjoke;
}