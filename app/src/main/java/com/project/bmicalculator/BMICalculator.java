package com.project.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class BMICalculator extends AppCompatActivity implements View.OnClickListener {

    public ImageButton btnGetValues, btnCalculate;
    public Button btnProceed;
    public TextView txtHeight, txtWeight, txtBMI, txtRange, btnConfirm, btnCancel;
    public EditText etHeight, etWeight;
    public RadioButton radioKilo, radioCenti, radioFeet, radioPound;

    int weight, height;
    String unitHeight, unitWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmicalculator);

        // Setup UI objects to be used
        btnCalculate = findViewById(R.id.btnCalculate);
        btnGetValues = findViewById(R.id.btnGetValues);

        // Sets context of onClick event to this activity
        btnCalculate.setOnClickListener(this);
        btnGetValues.setOnClickListener(this);
    }


    // Function that computes and returns the BMI
    public float getBMI(){
        int convertedHeight;
        float convertedWeight;

        convertedHeight = (int) toMeters();
        convertedWeight = (float) toKilo();

        return convertedWeight/(convertedHeight^2);
    }

    // Function that converts centimeter and feet to meter
    public double toMeters(){
        if(unitHeight.equals("cm")){
            return (float) height/100;
        }

        return height/3.281;
    }

    // Function that converts pounds to kilograms
    public double toKilo(){
        if(unitWeight.equals("lb")){
            return weight * 0.453592;
        }

        return weight;
    }

    // Sets up the Custom Input Dialog box to screen, and ask/obtain input
    public void getInputs(){

        // Declare and Define a View and Alert Dialog Builder Object
        AlertDialog.Builder inputDialogBuilder = new AlertDialog.Builder(BMICalculator.this);
        View viewDialog = getLayoutInflater().inflate(R.layout.input_dialog_box, null);

        // Setup all needed elements to display input on screen
        txtWeight = findViewById(R.id.txtWeight);

        // Setup all needed elements to obtain the values through the input dialog Box
        etHeight = viewDialog.findViewById(R.id.etHeight);
        etWeight = viewDialog.findViewById(R.id.etWeight);
        btnConfirm = viewDialog.findViewById(R.id.txtConfirm);
        btnCancel = viewDialog.findViewById(R.id.txtCancel);
        radioCenti = viewDialog.findViewById(R.id.radioCenti);
        radioFeet = viewDialog.findViewById(R.id.radioFeet);
        radioKilo = viewDialog.findViewById(R.id.radioKilo);
        radioPound = viewDialog.findViewById(R.id.radioPound);

        // Setup the finals steps and layout of input dialog box
        inputDialogBuilder.setView(viewDialog);
        AlertDialog dialog = inputDialogBuilder.create();

        // If confirm button is clicked, get all values from the dialog box and store in variables
        btnConfirm.setOnClickListener(view -> {
            if(!etHeight.getText().toString().isEmpty() && !etWeight.getText().toString().isEmpty()){
                height = Integer.parseInt(etHeight.getText().toString());
                weight = Integer.parseInt(etWeight.getText().toString());


                // Check what unit of measurement for height did the user select,
                // and assign the value to the unitHeight variable
                if(radioCenti.isChecked()){
                    unitHeight = radioCenti.getText().toString();
                }else{
                    unitHeight = radioFeet.getText().toString();
                }

                // Check what unit of measurement for weight did the user select
                // and assign the value to unitHeight variable
                if(radioKilo.isChecked()){
                    unitWeight = radioKilo.getText().toString();
                }else{
                    unitWeight = radioPound.getText().toString();
                }

                // Display input on screen
                String temp  = height + unitHeight;
                txtHeight.setText(temp);
                temp = weight + unitWeight;
                txtWeight.setText(temp);

                dialog.dismiss();
            }else{
                // If any of the input boxes is empty,
                // Display a toast on screen that tells users to fill up all inputs before pressing confirm
                Toast.makeText(BMICalculator.this,
                        "Please fill up all inputs before confirming",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // if cancel button is clicked, dismiss dialog box.
        btnCancel.setOnClickListener(view -> dialog.dismiss());

        // Show input dialog box on screen
        dialog.show();
    }

    // Sets up the Custom ShowInfo Dialog box to screen,
    public void displayResult(float bmi){
        AlertDialog.Builder displayDialogBuilder = new AlertDialog.Builder(BMICalculator.this);
        View viewDialog = getLayoutInflater().inflate(R.layout.display_dialog_box, null);

        btnProceed = viewDialog.findViewById(R.id.btnProceed);
        txtBMI = viewDialog.findViewById(R.id.txtBMI);
        txtRange = viewDialog.findViewById(R.id.txtRange);

        // Assigns results of calculation to all text views in the custom display dialog box
        String temp = "" + bmi;
        txtBMI.setText(temp);
        temp = determineRange(bmi);
        txtRange.setText(temp);

        displayDialogBuilder.setCancelable(false);
        displayDialogBuilder.setView(viewDialog);
        AlertDialog dialog = displayDialogBuilder.create();


        // Dismiss display dialog box once user click the button
        btnProceed.setOnClickListener(view -> dialog.dismiss());

        // Show display dialog box on screen
        dialog.show();
    }

    // Checks what category does a calculated BMI belong to
    public String determineRange(float bmi){

        if(bmi < 18.5){
            return "Underweight";
        }else if(bmi < 25.0){
            return "Normal";
        }else if(bmi < 30.0){
            return "Overweight";
        }
        return "Obese";
    }

    @Override
    public void onClick(View view) {
        txtHeight = findViewById(R.id.txtHeight);

        switch(view.getId()){
            case R.id.btnGetValues:
                getInputs();
                break;
            case R.id.btnCalculate:
                if(!txtHeight.getText().toString().isEmpty()){
                    float bmi = getBMI();
                    displayResult(bmi);
                }else{
                    Toast.makeText(BMICalculator.this,
                            "No inputs available, cannot perform the calculations",
                            Toast.LENGTH_SHORT).show();
                }
        }
    }
}