package com.calculator.models;

import java.sql.Timestamp;

public class Calculations {
    private int id;
    private String input;
    private double result;
    private Timestamp timestamp;

    // Constructors
    public Calculations() {}

    public Calculations(String input, double result) {
        this.input = input;
        this.result = result;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
