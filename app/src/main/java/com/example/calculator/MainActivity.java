package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.calculator.databinding.ActivityMainBinding;

import java.beans.PropertyChangeEvent;

//import edu.jsu.mcis.cs408.androidmvctest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements AbstractView {

    public static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private CalculatorController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /* Create Controller and Model */

        controller = new CalculatorController();
        CalculatorModel model = new CalculatorModel();

        /* Register Activity View and Model with Controller */

        controller.addView(this);
        controller.addModel(model);

        /* Initialize Model to Default Values */

        model.init();

        /* Associate Click Handler with Input Buttons */

        DefaultClickHandler click = new DefaultClickHandler();
        ConstraintLayout layout = binding.layout;

        for (int i = 0; i < layout.getChildCount(); ++i) {
            View child = layout.getChildAt(i);
            if(child instanceof Button) {
                child.setOnClickListener(click);
            }
        }

    }

    @Override
    public void modelPropertyChange(final PropertyChangeEvent evt) {

        /*
         * This method is called by the "propertyChange()" method of AbstractController
         * when a change is made to an element of a Model.  It identifies the element that
         * was changed and updates the View accordingly.
         */

        String propertyName = evt.getPropertyName();
        String propertyValue = evt.getNewValue().toString();

        Log.i(TAG, "New " + propertyName + " Value from Model: " + propertyValue);

        if ( propertyName.equals(CalculatorController.SCREEN_PROPERTY) ) {

            String oldPropertyValue = binding.displayTextView.getText().toString();

            if ( !oldPropertyValue.equals(propertyValue) ) {
                binding.displayTextView.setText(propertyValue);
            }

        }

    }

    class DefaultClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            //INSERT CODE HERE

        }

    }

}
