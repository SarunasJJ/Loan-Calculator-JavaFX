package com.loancalculator.calculations;

import javafx.beans.property.SimpleStringProperty;

public class TableClass {
    SimpleStringProperty month, monthlyPayment, monthlyInterest, unpaidSum;
    public TableClass(String monthColumn, String monthlyColumn, String sumColumn, String unpaidColumn) {
        this.month = new SimpleStringProperty(monthColumn);
        this.monthlyPayment = new SimpleStringProperty(monthlyColumn);
        this.monthlyInterest = new SimpleStringProperty(sumColumn);
        this.unpaidSum = new SimpleStringProperty(unpaidColumn);
    }

    public String getMonthColumn() {
        return month.get();
    }

    public void setMonthColumn(String month) {
        this.month.set(month);
    }

    public String getMonthlyColumn() {
        return monthlyPayment.get();
    }

    public void setMonthlyColumn(String monthlyPayment) {
        this.monthlyPayment.set(monthlyPayment);
    }

    public String getSumColumn() {
        return monthlyInterest.get();
    }

    public void setSumColumn(String monthlyInterest) {
        this.monthlyInterest.set(monthlyInterest);
    }

    public String getUnpaidColumn() {
        return unpaidSum.get();
    }

    public void setUnpaidColumn(String unpaidSum) {
        this.unpaidSum.set(unpaidSum);
    }
}
