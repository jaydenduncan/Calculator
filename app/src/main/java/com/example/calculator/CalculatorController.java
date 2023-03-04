package com.example.calculator;

import java.util.HashMap;

public class CalculatorController extends AbstractController
{


    public static final String KEY_PROPERTY = "Key";
    public static final String SCREEN_PROPERTY = "Screen";
    public static final String KEY_FUNCTION = "Function";
    private HashMap<String, Character> keyMap;
    private HashMap<String, CalculatorFunction> functionMap;

    public CalculatorController(){

        super();
        createKeyMap();
        createFunctionMap();

    }

    private void createKeyMap(){

        //INSERT CODE HERE

    }

    private void createFunctionMap(){

        //INSERT CODE HERE

    }

    public void changeKey(Character newText) {
        setModelProperty(KEY_PROPERTY, newText);
    }

    public void changeScreen(String tag) {
        setModelProperty(SCREEN_PROPERTY, tag);
    }

    public void changeFunction(String tag){
        setModelProperty(KEY_FUNCTION, tag);
    }

}
