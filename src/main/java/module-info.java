module com.loancalculator.loancalculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.loancalculator.loancalculator to javafx.fxml;
    exports com.loancalculator.loancalculator;
    exports com.loancalculator.calculations;
    opens com.loancalculator.calculations to javafx.fxml;
}