package com.loancalculator.loancalculator;

import com.loancalculator.calculations.InputClass;
import com.loancalculator.calculations.LoanCalculations;
import com.loancalculator.calculations.TableClass;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoanTableInterfaceController implements Initializable {
    @FXML
    private Label warningLabel;
    @FXML
    private TextField loanAmountField, interestField, downPaymentField;
    @FXML
    private Spinner<Integer> yearSpinner, monthSpinner;
    @FXML
    private RadioButton annuityButton, linearButton;
    @FXML
    private CheckBox filterCheck, delayCheck;
    @FXML
    private TextField filterField1, filterField2, delayField1, delayField2, delayInterestField;
    @FXML
    private TableView<TableClass> paymentTable;
    @FXML
    private LineChart<Number, Number> lineChart;
    private int years = 0, months = 0;
    LoanCalculations calculations;
    InputClass input = new InputClass();
    public void getPaymentType(ActionEvent event) {
        if (annuityButton.isSelected()) {
            input.paymentType = 1;
        }
        if (linearButton.isSelected()) {
            input.paymentType = 2;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> yearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50);
        SpinnerValueFactory<Integer> monthValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 12);
        yearSpinner.setValueFactory(yearValueFactory);
        monthSpinner.setValueFactory(monthValueFactory);
        yearSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                years = yearSpinner.getValue();
                input.loanTerm = years * 12 + months;
            }
        });
        monthSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                months = monthSpinner.getValue();
                input.loanTerm = years * 12 + months;
            }
        });
    }

    public void submit(ActionEvent event) {
        try {
            input.loanSum = Double.parseDouble(loanAmountField.getText());
        } catch (NumberFormatException e) {
            warningLabel.setText("Enter only numbers in the fields!");
        }
        try {
            input.loanInterest = Float.parseFloat(interestField.getText());
        } catch (NumberFormatException e) {
            warningLabel.setText("Enter only numbers in the fields!");
        }
        try {
            input.loanDownPayment = Double.parseDouble(downPaymentField.getText());
        } catch (NumberFormatException e) {
            warningLabel.setText("Enter only numbers in the fields!");
        }
        catch (Exception e) {
            warningLabel.setText("Error");
        }
        if(filterCheck.isSelected()){
            input.filter = true;
            try {
                input.filterMonth1 = Integer.parseInt(filterField1.getText());
            } catch (NumberFormatException e) {
                warningLabel.setText("Enter only numbers in the fields!");
            }
            try {
                input.filterMonth2 = Integer.parseInt(filterField2.getText());
            } catch (NumberFormatException e) {
                warningLabel.setText("Enter only numbers in the fields!");
            }
            catch (Exception e) {
                warningLabel.setText("Error");
            }
        }
        else {
            input.filter = false;
        }
        if(delayCheck.isSelected()){
            input.delay = true;
            try {
                input.delayMonth1 = Integer.parseInt(delayField1.getText());
            } catch (NumberFormatException e) {
                warningLabel.setText("Enter only numbers in the fields!");
            }
            try {
                input.delayMonth2 = Integer.parseInt(delayField2.getText());
            } catch (NumberFormatException e) {
                warningLabel.setText("Enter only numbers in the fields!");
            }
            try {
                input.delayInterest = Double.parseDouble(delayInterestField.getText());
            } catch (NumberFormatException e) {
                warningLabel.setText("Enter only numbers in the fields!");
            }
            catch (Exception e) {
                warningLabel.setText("Error");
            }
            input.fullDelay=input.delayMonth2-input.delayMonth1+1;
        } else{
            input.delay = false;
        }
        calculations = new LoanCalculations(paymentTable, input, lineChart);
        calculations.calculate();
        calculations.initializeTable();
    }
    public void export(ActionEvent event) throws IOException {
        calculations.exportToFile();
    }
}