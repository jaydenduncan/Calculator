package com.example.calculator;

import java.util.ArrayList;
import java.util.HashMap;

public class CalculatorController extends AbstractController
{


    public static final String KEY_PROPERTY = "Key";
    public static final String SCREEN_PROPERTY = "Screen";
    public static final String KEY_FUNCTION = "Function";
    private static HashMap<String, Character> keyMap = new HashMap<>();
    private static HashMap<String, CalculatorFunction> functionMap = new HashMap<>();
    private static ArrayList<String> operators = new ArrayList<>();

    public CalculatorController(){

        super();
        createKeyMap();
        createFunctionMap();
        createOperatorsArray();

    }

    private void createKeyMap(){

        for(int i=0; i < 10; i++){

            keyMap.put("btn" + i, String.valueOf(i).charAt(0));

        }

        keyMap.put("btnDec", '.');

    }

    private void createFunctionMap(){

        functionMap.put("btnC", CalculatorFunction.CLEAR);
        functionMap.put("btnAdd", CalculatorFunction.ADD);
        functionMap.put("btnSub", CalculatorFunction.SUBTRACT);
        functionMap.put("btnMul", CalculatorFunction.MULTIPLY);
        functionMap.put("btnDiv", CalculatorFunction.DIVIDE);
        functionMap.put("btnSqrt", CalculatorFunction.SQRT);

    }

    private void createOperatorsArray(){

        operators.add("btnAdd");
        operators.add("btnSub");
        operators.add("btnMul");
        operators.add("btnDiv");

    }

    public static HashMap<String, Character> getKeyMap(){
        return keyMap;
    }

    public static HashMap<String, CalculatorFunction> getFunctionMap(){
        return functionMap;
    }

    public static ArrayList<String> getOperators(){
        return operators;
    }


    public void changeScreen(String tag) {
        setModelProperty(SCREEN_PROPERTY, tag);
    }


}
