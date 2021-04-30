module se.dykstrom.javafx.dadjoke {
    requires com.gluonhq.connect;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.fasterxml.jackson.databind;

    opens se.dykstrom.javafx.dadjoke to javafx.fxml;
    opens se.dykstrom.javafx.dadjoke.model to com.fasterxml.jackson.databind;
    exports se.dykstrom.javafx.dadjoke;
}