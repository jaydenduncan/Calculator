package com.example.calculator;

public enum CalculatorFunction {

    CLEAR("C"),
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("x"),
    DIVIDE("/");

    public final String operation;

    private CalculatorFunction(String operation){
        this.operation = operation;
    }

}
