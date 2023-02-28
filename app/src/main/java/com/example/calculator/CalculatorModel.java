package com.example.calculator;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorModel extends AbstractModel {

    public static final String TAG = "CalculatorModel";

    private StringBuilder screen;

    private CalculatorState state;

    private BigDecimal lhs, rhs;

    private Operator operator;

    private String result;


    public CalculatorModel(){

        screen = new StringBuilder();
        state = CalculatorState.CLEAR;
        lhs = new BigDecimal(0);
        rhs = new BigDecimal(0);
        operator = null;

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

        String oldText = screen.toString();
        String newText;
        long tempLong = 0;
        double tempDouble = 0.0;

        switch(tag){
            case "btn1": case "btn2": case "btn3": case "btn4": case "btn5": case "btn6":
            case "btn7": case "btn8": case "btn9": case "btn0":
                if(state == CalculatorState.CLEAR){
                    // change state to lhs
                    changeState(CalculatorState.LHS);

                    if(!(screen.toString().contains(".")) && (screen.toString().length() > 1)
                            && (screen.toString().substring(0, 2).equals("-0"))){
                        clearScreen();
                        setKey('-');
                        setKey(tag.charAt(3));
                    }
                    else{
                        // clear the screen
                        clearScreen();

                        // append proper number to the screen
                        setKey(tag.charAt(3));
                    }
                }
                else if((state == CalculatorState.LHS) || (state == CalculatorState.RHS)){

                    if(!(screen.toString().contains(".")) && (screen.toString().charAt(0) == '0')){
                        clearScreen();
                        setKey(tag.charAt(3));
                    }
                    else if(!(screen.toString().contains(".")) && (screen.toString().length() > 1)
                            && (screen.toString().substring(0, 2).equals("-0"))){
                        clearScreen();
                        setKey('-');
                        setKey(tag.charAt(3));
                    }
                    else{
                        setKey(tag.charAt(3));
                    }
                }
                else if(state == CalculatorState.OP_SELECTED){
                    changeState(CalculatorState.RHS);
                    clearScreen();
                    setKey(tag.charAt(3));
                }
                else{
                    changeState(CalculatorState.LHS);
                    clearScreen();
                    setKey(tag.charAt(3));
                }

                break;

            case "btnC":
                // change state to clear
                changeState(CalculatorState.CLEAR);

                // clear the screen
                clearScreen();

                break;

            case "btnDec":
                // place decimal in input if allowed
                if(state == CalculatorState.CLEAR){
                    changeState(CalculatorState.LHS);

                    setKey('.');
                }
                else if((state == CalculatorState.LHS) || (state == CalculatorState.RHS)){
                    if (!(screen.toString().contains("."))) {

                        setKey('.');
                    }
                }
                else if(state == CalculatorState.OP_SELECTED){
                    changeState(CalculatorState.RHS);
                    clearScreen();
                    setKey('0');
                    setKey('.');
                }
                else{
                    changeState(CalculatorState.LHS);
                    setKey('0');
                    setKey('.');
                }


                break;

            case "btnPlusMin":

                if(state == CalculatorState.OP_SELECTED){
                    changeState(CalculatorState.RHS);
                    clearScreen();
                    setKey('-');
                    setKey('0');
                }
                else if(state == CalculatorState.RESULT){

                    changeState(CalculatorState.LHS);

                    if(result.contains("-")){
                        firePropertyChange(CalculatorController.SCREEN_PROPERTY, result, result.substring(1));
                        result = result.substring(1);
                        lhs = new BigDecimal(result);
                    }
                    else{
                        firePropertyChange(CalculatorController.SCREEN_PROPERTY, result, "-" + result);
                        result = "-" + result;
                        lhs = new BigDecimal(result);
                    }
                }
                else{
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
                }

                break;

            case "btnAdd": case "btnSub": case "btnMul": case "btnDiv":

                changeState(CalculatorState.OP_SELECTED);

                if(tag.equals("btnAdd")) operator = Operator.ADD;
                else if(tag.equals("btnSub")) operator = Operator.SUBTRACT;
                else if(tag.equals("btnMul")) operator = Operator.MULTIPLY;
                else if(tag.equals("btnDiv")) operator = Operator.DIVIDE;

                if(screen.toString().contains(".")){
                    tempDouble = Double.valueOf(screen.toString());
                    lhs = new BigDecimal(tempDouble);
                }
                else{
                    tempLong = Long.valueOf(screen.toString());
                    lhs = new BigDecimal(tempLong);
                }

                break;

            case "btnSqrt":

                // store lhs or rhs
                if(state == CalculatorState.LHS){

                    tempDouble = Double.valueOf(screen.toString());
                    lhs = new BigDecimal(tempDouble);

                    if(screen.toString().contains(".")){
                        tempDouble = Double.valueOf(screen.toString());
                        lhs = new BigDecimal(tempDouble);
                    }
                    else{
                        tempLong = Long.valueOf(screen.toString());
                        lhs = new BigDecimal(tempLong);
                    }

                }
                else if(state == CalculatorState.RHS){

                    tempDouble = Double.valueOf(screen.toString());
                    rhs = new BigDecimal(tempDouble);


                    if(screen.toString().contains(".")){
                        tempDouble = Double.valueOf(screen.toString());
                        rhs = new BigDecimal(tempDouble);
                    }
                    else{
                        tempLong = Long.valueOf(screen.toString());
                        rhs = new BigDecimal(tempLong);
                    }

                }

                // calculate square root of lhs or rhs
                result = String.valueOf(calculateSqrt());

                // make result the new lhs or rhs
                if(state == CalculatorState.LHS){
                    lhs = new BigDecimal(result);
                }
                else if(state == CalculatorState.RHS) {
                    rhs = new BigDecimal(result);
                }


                // clear the screen
                clearScreen();

                // display the result on the screen
                displayResult(result);

                // replace the screen buffer with result
                screen.replace(0, screen.length(), result);

                break;

            case "btnEqual":

                if(screen.toString().contains(".")){
                    tempDouble = Double.valueOf(screen.toString());
                    rhs = new BigDecimal(tempDouble);
                }
                else{
                    tempLong = Long.valueOf(screen.toString());
                    rhs = new BigDecimal(tempLong);
                }

                changeState(CalculatorState.RESULT);

                result = String.valueOf(calculateResult());

                if( result.contains(".") && !(lhs.toString().contains(".")) &&
                        !(rhs.toString().contains("."))){
                    result = result.split("[.]", 0)[0];
                }

                clearScreen();

                displayResult(result);

                lhs = new BigDecimal(result);

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

        screen.append(digit);
        String newText = screen.toString();

        Log.i(TAG, "Screen Change: " + digit);

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);

    }

    private void clearScreen() {
        String oldText = screen.toString();

        if(state == CalculatorState.CLEAR){
            // reset lhs, rhs, and operator
            lhs = new BigDecimal(0);
            rhs = new BigDecimal(0);
            operator = null;

            // clear the buffer
            screen.delete(0, screen.length());

            screen.append('0');


            // place 0 as placeholder in screen
            firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, "0");
        }
        else{
            screen.delete(0, screen.length());

            firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, "");
        }

    }

    private double calculateSqrt(){

        BigDecimal result = null;

        if(state == CalculatorState.LHS){

            double tempResult = Math.pow(lhs.doubleValue(), 0.5);
            result = new BigDecimal(tempResult);

            Log.i(TAG, "Result: " + result.toString()); // ERROR CHECK

            return result.doubleValue();
        }
        else if(state == CalculatorState.RHS){
            double tempResult = Math.pow(rhs.doubleValue(), 0.5);
            result = new BigDecimal(tempResult);

            Log.i(TAG, "Result: " + result.toString()); // ERROR CHECK

            return result.doubleValue();
        }

        return result.doubleValue();

    }


    private double calculateResult(){
        BigDecimal result = null;

        switch(operator){

            case ADD:

                result = lhs.add(rhs);
                result.setScale(2, RoundingMode.HALF_UP);

                return result.doubleValue();

            case SUBTRACT:

                result = lhs.subtract(rhs);
                result.setScale(2, RoundingMode.HALF_UP);

                return result.doubleValue();

            case MULTIPLY:

                result = lhs.multiply(rhs);
                result.setScale(2, RoundingMode.HALF_UP);

                return result.doubleValue();

            case DIVIDE:

                result = lhs.divide(rhs, 2, RoundingMode.HALF_UP);
                result.setScale(2, RoundingMode.HALF_UP);

                return result.doubleValue();
        }

        return result.doubleValue();

    }

    private void displayResult(String result){

        String oldText = screen.toString();

        String newText = result;

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);

    }

    private enum CalculatorState {
        CLEAR, LHS, OP_SELECTED, RHS, RESULT, ERROR
    }

    private enum Operator {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

}
