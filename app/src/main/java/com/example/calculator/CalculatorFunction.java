package com.example.calculator;

public enum CalculatorFunction {

    CLEAR("C"),
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("x"),
    DIVIDE("/"),
    SQRT("sqrt");

    public final String operation;

    private CalculatorFunction(String operation){
        this.operation = operation;
    }

}
