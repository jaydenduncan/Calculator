package com.example.calculator;

import android.util.Log;

import java.math.BigDecimal;

public class CalculatorModel extends AbstractModel {

    public static final String TAG = "CalculatorModel";

    private StringBuilder screen;
    private CalculatorState state;
    private BigDecimal lhs, rhs;
    private CalculatorFunction function;
    private String operator;
    private String result;
    private boolean hasDecimal;



    public void init() {

        // initialize values

    }

    public void processInput(String tag){

        //INSERT CODE HERE

    }

    public void setScreen(){

        //INSERT CODE HERE

    }


    public void setKey(Character digit) {

        //INSERT CODE HERE

    }

    public void setFunction(String tag){

        //INSERT CODE HERE

    }

    private void appendDigit(char digit) {

        //INSERT CODE HERE

    }

    private void display(String newText){

        //INSERT CODE HERE

    }

    private void clearScreen(){

        //INSERT CODE HERE

    }

    private void clearBuffer(){

        //INSERT CODE HERE

    }

    private void changeState(){

        //INSERT CODE HERE

    }

    private void evaluate(){

        //INSERT CODE HERE

    }

    private enum CalculatorState {
        CLEAR, LHS, OP_SELECTED, RHS, RESULT, ERROR
    }


}
