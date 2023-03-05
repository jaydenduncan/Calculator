package com.example.calculator;

import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class CalculatorModel extends AbstractModel {

    public static final String TAG = "CalculatorModel";

    private StringBuilder buffer;
    private CalculatorState state;
    private BigDecimal lhs, rhs;
    private BigDecimal result;
    private CalculatorFunction function;
    private ArrayList<String> operators;
    private String operator;
    private boolean hasDecimal;
    private HashMap<String, Character> keyMap;
    private HashMap<String, CalculatorFunction> functionMap;



    public void init() {

        // initialize values
        buffer = new StringBuilder();
        state = CalculatorState.CLEAR;
        lhs = BigDecimal.ZERO;
        rhs = BigDecimal.ZERO;
        result = BigDecimal.ZERO;
        function = null;
        operators = CalculatorController.getOperators();
        operator = null;
        hasDecimal = false;

        keyMap = CalculatorController.getKeyMap();
        functionMap = CalculatorController.getFunctionMap();

        // append '0' to the screen
        appendDigit('0');

    }

    public void processInput(String tag){

        if(keyMap.containsKey(tag)){
            // append appropriate digit to screen
            setKey(keyMap.get(tag));
        }
        else if(functionMap.containsKey(tag)){
            // set appropriate function
            function = functionMap.get(tag);

            // perform function
            setFunction();
        }
        else if(tag.equals("btnPlusMin")){
            negate();
        }

    }

    public void setScreen(String tag){

        // perform an action based on the button that was pressed
        switch(tag){
            case "btn1": case "btn2": case "btn3": case "btn4": case "btn5": case "btn6":
            case "btn7": case "btn8": case "btn9": case "btn0": case "btnDec":
                changeState(tag);
                processInput(tag);

                break;
            case "btnC":
                changeState(tag);
                processInput(tag);

                break;
            case "btnPlusMin":
                processInput(tag);
                break;

        }

    }


    public void setKey(Character digit) {

        if(digit.equals('.')){
            // append decimal point if there is not already a decimal point
            if(!hasDecimal){
                appendDigit(digit.charValue());
                hasDecimal = true;
            }
        }
        else{
            // append char value of digit
            appendDigit(digit.charValue());
        }

    }

    public void setFunction(){

        // perform action based on function property
        switch(function){

            case CLEAR:
                init();
                Log.i(TAG, "State Change: " + state);
                break;

        }

    }

    private void appendDigit(char digit) {

        // store old text
        String oldText = buffer.toString();

        // append the digit to the buffer
        buffer.append(digit);

        // store new text
        String newText = buffer.toString();

        // Log changes
        Log.i(TAG, "Screen Change: " + digit);

        // apply change to the screen
        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);

    }

    private void display(String oldText, String newText){

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);

    }

    private void clearScreen(){

        String oldText = buffer.toString();

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, "");

    }

    private void clearBuffer(){

        buffer.setLength(0);

    }

    private void changeState(String tag){

        // change state of calculator based on current state
        switch(state){

            case CLEAR:

                if(operators.contains(tag)){
                    state = CalculatorState.OP_SELECTED;
                }
                else if(tag.equals("btnDec")){
                    state = CalculatorState.LHS;
                }
                else{
                    clearBuffer();
                    clearScreen();
                    state = CalculatorState.LHS;
                }

                Log.i(TAG, "State Change: " + state);

                break;

            case LHS:

                if(operators.contains(tag)){
                    state = CalculatorState.OP_SELECTED;
                }
                else if(tag.equals("btnEqual")){
                    state = CalculatorState.RESULT;
                }

                Log.i(TAG, "State Change: " + state);

                break;

        }

    }

    private void negate(){
        String oldText = buffer.toString();
        clearBuffer();

        switch(state){
            case LHS:
                lhs = new BigDecimal(oldText);
                lhs = lhs.negate();
                buffer.append(lhs.toString());
                display(oldText, buffer.toString());
                break;
        }

    }

    private void evaluate(){

        //INSERT CODE HERE

    }

    private enum CalculatorState {
        CLEAR, LHS, OP_SELECTED, RHS, RESULT, ERROR
    }


}
