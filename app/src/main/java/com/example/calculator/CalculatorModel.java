package com.example.calculator;

import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;

public class CalculatorModel extends AbstractModel {

    public static final String TAG = "CalculatorModel";
    public static final String ERROR = "ERROR";

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
            case "btnC": case "btnAdd": case "btnSub": case "btnMul": case "btnDiv":
                changeState(tag);
                processInput(tag);
                break;
            case "btnSqrt":
                processInput(tag);
                calculateSqrt();
                break;
            case "btnPerc":
                calculatePercent();
                break;
            case "btnPlusMin":
                processInput(tag);
                break;
            case "btnEqual":
                changeState(tag);
                evaluate();
                lhs = result;
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
            case ADD: case SUBTRACT: case MULTIPLY: case DIVIDE:
                operator = function.operation;
                Log.i(TAG, "Function Change: " + function);
                Log.i(TAG, "Operator: " + operator);
                break;
            case SQRT:
                if(state == CalculatorState.LHS){
                    operator = function.operation;
                    Log.i(TAG, "Function Change: " + function);
                    Log.i(TAG, "Operator: " + operator);
                }
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

        hasDecimal = false;
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

                break;
            case LHS:
                if(operators.contains(tag)){
                    lhs = new BigDecimal(buffer.toString());
                    state = CalculatorState.OP_SELECTED;
                }
                else if(tag.equals("btnSqrt")){
                    lhs = new BigDecimal(buffer.toString());
                    state = CalculatorState.RESULT;
                }
                else if(tag.equals("btnEqual")) {
                    lhs = new BigDecimal(buffer.toString());
                    evaluate();
                    lhs = result;
                    state = CalculatorState.RESULT;
                }

                break;
            case OP_SELECTED:
                if(keyMap.containsKey(tag)){
                    clearBuffer();
                    clearScreen();

                    if(tag.equals("btnDec")){
                        buffer.append("0");
                        display(buffer.toString(), "0");
                    }

                    state = CalculatorState.RHS;
                }
                else if(tag.equals("btnEqual")){
                    rhs = lhs;
                    clearBuffer();
                    clearScreen();
                    state = CalculatorState.RESULT;
                }

                break;
            case RHS:
                if(tag.equals("btnEqual")){
                    rhs = new BigDecimal(buffer.toString());
                    state = CalculatorState.RESULT;
                }

                break;
            case RESULT:

                if(keyMap.containsKey(tag)){
                    clearBuffer();
                    clearScreen();

                    if(tag.equals("btnDec")){
                        buffer.append("0");
                        display(buffer.toString(), "0");
                    }

                    state = CalculatorState.LHS;
                }
                else if(operators.contains(tag)){
                    state = CalculatorState.OP_SELECTED;
                }

                break;
            case ERROR:

                if(operators.contains(tag)){
                    state = CalculatorState.OP_SELECTED;
                }
                else if(keyMap.containsKey(tag)){
                    clearBuffer();
                    clearScreen();
                    state = CalculatorState.LHS;
                }

                break;

        }

        Log.i(TAG, "State Change: " + state);

    }

    private void negate(){
        String oldText = buffer.toString();

        switch(state){
            case LHS:
                lhs = new BigDecimal(oldText);
                lhs = lhs.negate();
                clearBuffer();
                buffer.append(lhs.toString());
                display(oldText, buffer.toString());
                break;
            case RHS:
                rhs = new BigDecimal(oldText);
                rhs = rhs.negate();
                clearBuffer();
                buffer.append(rhs.toString());
                display(oldText, buffer.toString());
                break;
            case RESULT:
                lhs = lhs.negate();
                clearBuffer();
                buffer.append(lhs.toString());
                display(oldText, buffer.toString());
                break;
        }

    }

    private void calculateSqrt(){

        String oldText = buffer.toString();
        double temp;

        try{

            switch(state) {

                case LHS:
                    state = CalculatorState.RESULT;

                    lhs = new BigDecimal(buffer.toString());
                    temp = lhs.doubleValue();
                    temp = Math.pow(temp, 0.5);
                    result = new BigDecimal(temp, MathContext.DECIMAL64);

                    lhs = result;
                    display(oldText, result.toString());

                    break;
                case RHS:
                    rhs = new BigDecimal(buffer.toString());
                    temp = rhs.doubleValue();
                    temp = Math.pow(temp, 0.5);
                    rhs = new BigDecimal(temp, MathContext.DECIMAL64);
                    clearBuffer();
                    buffer.append(rhs.toString());

                    display(oldText, rhs.toString());

                    break;
                case RESULT:
                    temp = lhs.doubleValue();
                    temp = Math.pow(temp, 0.5);
                    result = new BigDecimal(temp, MathContext.DECIMAL64);

                    lhs = result;
                    display(oldText, result.toString());

                    break;
                case OP_SELECTED:
                    state = CalculatorState.RHS;
                    temp = lhs.doubleValue();
                    temp = Math.pow(temp, 0.5);
                    result = new BigDecimal(temp, MathContext.DECIMAL64);
                    rhs = result;

                    display(oldText, result.toString());

                    break;
            }

        }
        catch(Exception e){
            e.printStackTrace();
            lhs = null;
            rhs = null;
            result = null;
            state = CalculatorState.ERROR;
            display("", ERROR);
            Log.i(TAG, "State Change: " + state);
        }


    }

    private void calculatePercent(){

        String oldText = buffer.toString();
        BigDecimal percent = BigDecimal.ZERO;
        BigDecimal decimalPerc = BigDecimal.ZERO;

        try{
            switch(state){

                case LHS: case RESULT:
                    state = CalculatorState.RESULT;
                    lhs = new BigDecimal(buffer.toString());
                    result = lhs.divide(new BigDecimal(100), MathContext.DECIMAL64);
                    lhs = result;
                    clearBuffer();
                    buffer.append(lhs.toString());

                    display(oldText, result.toString());
                    break;
                case OP_SELECTED:
                    break;
                case RHS:
                    percent = new BigDecimal(buffer.toString());
                    decimalPerc = percent.divide(new BigDecimal(100), MathContext.DECIMAL64);
                    rhs = lhs.multiply(decimalPerc, MathContext.DECIMAL64);
                    clearBuffer();
                    buffer.append(rhs.toString());

                    display(oldText, rhs.toString());
                    break;

            }

            Log.i(TAG, "State Change: " + state);
        }
        catch(Exception e){
            e.printStackTrace();
            lhs = null;
            rhs = null;
            result = null;
            state = CalculatorState.ERROR;
            display("", ERROR);
            Log.i(TAG, "State Change: " + state);
        }

    }

    private void evaluate(){
        String oldText = buffer.toString();

        if(!(lhs == null) && !(rhs == null))
            Log.i(TAG, "LHS: " + lhs.toString() + "| RHS: " + rhs.toString());

        try{

            if(!(operator == null)){

                switch(operator){

                    case "+":
                        result = lhs.add(rhs);
                        display(oldText, result.toString());
                        break;
                    case "-":
                        result = lhs.subtract(rhs);
                        display(oldText, result.toString());
                        break;
                    case "x":
                        result = lhs.multiply(rhs);
                        display(oldText, result.toString());
                        break;
                    case "/":
                        result = lhs.divide(rhs, MathContext.DECIMAL64);
                        display(oldText, result.toString());
                        break;
                    case "sqrt":
                        double number = result.doubleValue();
                        double doubleResult = Math.pow(number, 0.5);
                        result = new BigDecimal(doubleResult, MathContext.DECIMAL64);
                        display(oldText, result.toString());
                        break;

                }

            }
            else{
                result = lhs;
                display(oldText, result.toString());
            }

        }
        catch(Exception e){
            e.printStackTrace();
            lhs = null;
            rhs = null;
            result = null;
            state = CalculatorState.ERROR;
            display("", ERROR);
            Log.i(TAG, "State Change: " + state);
        }

    }

    private enum CalculatorState {
        CLEAR, LHS, OP_SELECTED, RHS, RESULT, ERROR
    }


}
