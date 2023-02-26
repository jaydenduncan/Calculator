package com.example.calculator;

import android.util.Log;

import java.math.BigDecimal;

public class CalculatorModel extends AbstractModel {

    public static final String TAG = "CalculatorModel";

    private StringBuilder screen;

    private CalculatorState state;

    private BigDecimal lhs, rhs;

    private String operator;

    private boolean decimal;

    public CalculatorModel(){

        screen = new StringBuilder();
        state = CalculatorState.CLEAR;
        lhs = new BigDecimal(0);
        rhs = new BigDecimal(0);
        operator = null;
        decimal = false;

    }

    /*
     * Initialize the model elements to known default values.  We use the setter
     * methods instead of changing the values directly so that these changes are
     * properly announced to the Controller, and so that the Views can be updated
     * accordingly.
     */

    public void init() {

        appendDigit('0');

    }


    /*
     * Setters for text1 and text2.  Notice that, in addition to changing the
     * values, these methods announce the change to the controller by firing a
     * PropertyChange event.  Any registered AbstractController subclasses will
     * receive this event, and will propagate it to all registered Views so that
     * they can update themselves accordingly.
     */

    public void setScreen(String tag){

        switch(tag){
            case "btn1": case "btn2": case "btn3": case "btn4": case "btn5": case "btn6":
            case "btn7": case "btn8": case "btn9": case "btn0":
                if(state == CalculatorState.CLEAR){
                    // change state to lhs
                    changeState(CalculatorState.LHS);

                    // append proper number to the screen
                    setKey(tag.charAt(3));
                }
                else if(state == CalculatorState.LHS){
                    setKey(tag.charAt(3));
                }
                else if(state == CalculatorState.OP_SELECTED){
                    changeState(CalculatorState.RHS);
                }
                else{
                    changeState(CalculatorState.LHS);
                }
                break;
            case "btnC":
                // change state to clear
                changeState(CalculatorState.CLEAR);

                // clear the screen
                screen.delete(0, screen.length());

                // place 0 as placeholder
                String oldText = screen.toString();
                String newText = "0";
                firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);
                break;
            case "btnDec":
                // place decimal in input if allowed
                if(!(screen.toString().contains("."))) {
                    setKey('.');
                }
                break;
            case "btnPlusMin":
                String oldNumText = screen.toString();
                String newNumText = "";

                // toggle positive and negative input
                if(oldNumText.contains("-")){
                    newNumText = oldNumText.substring(1);
                }
                else{
                    newNumText = screen.insert(0, "-").toString();
                }

                // replace StringBuilder buffer
                screen.delete(0, screen.length());
                screen.append(newNumText);

                firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldNumText, newNumText);
                break;
        }

    }

    public void setKey(Character digit){

        appendDigit(digit.charValue());

    }

    private void changeState(CalculatorState newState){

        state = newState;

    }

    public void appendDigit(char digit) {

        String oldText = screen.toString();

        if(oldText.equals("0")){
            screen.deleteCharAt(0);
        }

        screen.append(digit);
        String newText = screen.toString();

        Log.i(TAG, "Screen Change: " + digit);

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);

    }

    private enum CalculatorState {
        CLEAR, LHS, OP_SELECTED, RHS, RESULT, ERROR
    }

}
