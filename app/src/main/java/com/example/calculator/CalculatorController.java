package com.example.calculator;

public class CalculatorController extends AbstractController
{

    /*
     * These static property names are used as the identifiers for the elements
     * of the Models and Views which may need to be updated.  These updates can
     * be a result of changes to the Model which must be reflected in the View,
     * or a result of changes to the View (in response to user input) which must
     * be reflected in the Model.
     */

    public static final String KEY_PROPERTY = "Key";
    public static final String SCREEN_PROPERTY = "Screen";

    /*
     * This is the change method which corresponds to ELEMENT_TEXT1_PROPERTY.
     * It receives the new data for the Model, and invokes "setModelProperty()"
     * (inherited from AbstractController) so that the proper Model can be found
     * and updated properly.
     */

    public void changeKey(Character newText) {
        setModelProperty(KEY_PROPERTY, newText);
    }

    /*
     * This is the change method which corresponds to ELEMENT_TEXT2_PROPERTY.
     */

    public void changeScreen(String newText) {
        setModelProperty(SCREEN_PROPERTY, newText);
    }

}
