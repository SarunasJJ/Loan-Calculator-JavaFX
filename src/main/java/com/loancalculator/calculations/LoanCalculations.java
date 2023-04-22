package com.loancalculator.calculations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class LoanCalculations {
    TableView<TableClass> table;
    ObservableList<TableClass> observableList = FXCollections.observableArrayList();
    InputClass input;
    DecimalFormat decFormat = new DecimalFormat("#.00");
    @FXML
    NumberAxis xAxis = new NumberAxis();
    @FXML
    NumberAxis yAxis = new NumberAxis();
    @FXML
    LineChart<Number,Number> lineChart = new LineChart<>(xAxis, yAxis);

    public LoanCalculations(TableView<TableClass> table, InputClass input, LineChart<Number, Number> lineChart){
        this.table = table;
        this.input = input;
        this.lineChart = lineChart;
    }

    public void calculate(){
        observableList.clear();
        lineChart.getData().clear();
        table.getItems().clear();
        input.loanSum -= input.loanDownPayment;
        double delayInterest = input.loanSum * (input.delayInterest / 100.00 / 12.00);
        XYChart.Series series = new XYChart.Series();
        if(input.paymentType == 1){
            double monthlyPayment = (input.loanSum * input.loanInterest / 100 / 12 / (1 - (1 / Math.pow(1 + input.loanInterest / 100 / 12, input.loanTerm))));
            double unpaidSum = delayInterest * input.fullDelay;
            double tempSum = input.loanSum;
            for(int i=1; i<= input.loanTerm; i++){
                double interest = tempSum * (input.loanInterest / 100.00 / 12.00);
                if(interest <= 0){
                    interest = 0;
                }
                unpaidSum = unpaidSum + monthlyPayment + interest;
                tempSum = tempSum - monthlyPayment;
            }
            for(int i=1; i<= (input.loanTerm + input.fullDelay); i++){
                double interest = input.loanSum * (input.loanInterest / 100.00 / 12.00);
                if(interest <= 0){
                    interest = 0;
                }
                series.getData().add(new XYChart.Data(Integer.toString(i), (int)(monthlyPayment+interest)));
                if(input.delay && i>=input.delayMonth1 && i<=input.delayMonth2){
                    unpaidSum  = unpaidSum - delayInterest;
                    observableList.add(new TableClass(Integer.toString(i), " ", decFormat.format(delayInterest), decFormat.format(unpaidSum)));
                }
                else if(!input.filter || (input.filter && (i >= input.filterMonth1 && i <= input.filterMonth2))){
                    unpaidSum = unpaidSum - monthlyPayment - interest;
                    observableList.add(new TableClass(Integer.toString(i), decFormat.format(monthlyPayment), decFormat.format(interest), decFormat.format(unpaidSum)));
                    input.loanSum -= monthlyPayment;
                }
            }
        }
        if(input.paymentType == 2) {
            double monthlyPayment = input.loanSum / input.loanTerm;
            double interest = input.loanSum * (input.loanInterest / 100.00 / 12.00);
            double unpaidSum = input.loanSum + (input.loanSum * (input.loanInterest / 100.00 / 12.00) * input.loanTerm) + delayInterest * input.fullDelay;
            for (int i = 1; i <= (input.loanTerm + input.fullDelay); i++) {
                series.getData().add(new XYChart.Data(Integer.toString(i), (int)(monthlyPayment+interest)));
                if(input.delay && i>=input.delayMonth1 && i<=input.delayMonth2){
                    unpaidSum  = unpaidSum - delayInterest;
                    observableList.add(new TableClass(Integer.toString(i), " ", decFormat.format(delayInterest), decFormat.format(unpaidSum)));
                }
                else if(!input.filter || (input.filter && (i >= input.filterMonth1 && i <= input.filterMonth2))){
                    unpaidSum  = unpaidSum - monthlyPayment - interest;
                    observableList.add(new TableClass(Integer.toString(i), decFormat.format(monthlyPayment), decFormat.format(interest), decFormat.format(unpaidSum)));
                }
            }
        }
        lineChart.getData().add(series);
    }
    public void initializeTable(){
        table.getColumns().clear();
        TableColumn<TableClass, String> monthColumn = new TableColumn<>("Month");
        TableColumn<TableClass, String> monthlyColumn = new TableColumn<>("Monthly payment");
        TableColumn<TableClass, String> sumColumn = new TableColumn<>("Interest");
        TableColumn<TableClass, String> unpaidColumn = new TableColumn<>("Unpaid");
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("monthColumn"));
        monthlyColumn.setCellValueFactory(new PropertyValueFactory<>("monthlyColumn"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sumColumn"));
        unpaidColumn.setCellValueFactory(new PropertyValueFactory<>("unpaidColumn"));
        table.getColumns().addAll(monthColumn,monthlyColumn,sumColumn,unpaidColumn);
        table.setItems(observableList);
    }
    public void exportToFile() throws IOException {
        File file = new File("table.csv");
        if(!file.createNewFile()){
            System.out.println("Could not create file!");
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        for(int i = 0; i<input.loanTerm + input.fullDelay; i++){
            String line = observableList.get(i).getMonthColumn() + ";\t" + observableList.get(i).getMonthlyColumn()
                    + ";\t" + observableList.get(i).getSumColumn() + ";\t" + observableList.get(i).getUnpaidColumn() + "\n";
            bw.write(line);
        }
        bw.close();
        fw.close();
    }
}