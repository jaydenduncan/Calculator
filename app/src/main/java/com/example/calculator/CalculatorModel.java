package com.example.calculator;

import android.util.Log;

import java.math.BigDecimal;

public class CalculatorModel extends AbstractModel {

    public static final String TAG = "CalculatorModel";

    private StringBuilder screen;

    private CalculatorState state;

    private BigDecimal lhs, rhs;

    public CalculatorModel(){

        screen = new StringBuilder();

    }

    /*
     * This is a simple implementation of an AbstractModel which encapsulates
     * two text fields, text1 and text2, which (in this example) are each
     * reflected in the View as an EditText field and a TextView label.
     */

    private String text1;
    private String text2;

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
     * Simple getter methods for text1 and text2
     */


    /*
     * Setters for text1 and text2.  Notice that, in addition to changing the
     * values, these methods announce the change to the controller by firing a
     * PropertyChange event.  Any registered AbstractController subclasses will
     * receive this event, and will propagate it to all registered Views so that
     * they can update themselves accordingly.
     */

    public void setKey(Character digit){

        appendDigit(digit.charValue());

    }

    private void changeState(CalculatorState newState){

        // INSERT CODE HERE

    }

    public void appendDigit(char digit) {

        String oldText = screen.toString();
        screen.append(digit);
        String newText = screen.toString();

        Log.i(TAG, "Screen Change: " + digit);

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);

    }

    private enum CalculatorState {
        CLEAR, LHS, OP_SELECTED, RHS, RESULT, ERROR
    }

}
