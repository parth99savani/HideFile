package com.popseven.hidefile;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView btnAC;
    private CardView btnDel;
    private CardView btnPerc;
    private CardView btnDivide;
    private CardView btn7;
    private CardView btn8;
    private CardView btn9;
    private CardView btnMultiply;
    private CardView btn4;
    private CardView btn5;
    private CardView btn6;
    private CardView btnMinus;
    private CardView btn1;
    private CardView btn2;
    private CardView btn3;
    private CardView btnPlus;
    private CardView btn0;
    private CardView btnDot;
    private CardView btnEqual;
    private TextView numberDisplay, operationsDisplay;
    private HorizontalScrollView scrollerDisplayNumber, scrollerDisplayOperations;
    private String stringNumber, stringSpecial;
    private Expression expression;
    private double value=0;
    private boolean numberClicked=false;
    private ClipboardManager clipboard;
    private ClipData clip;
    private int dotCount=0;
    private SharedPreferences sharedPref;
    private String confirmPin="confirmPin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences("com.popseven.hidefile.loginpin",MODE_PRIVATE);

        numberDisplay = (TextView) findViewById(R.id.displayNumber);
        operationsDisplay = (TextView) findViewById(R.id.displayOperationNumber);
        scrollerDisplayNumber = (HorizontalScrollView) findViewById(R.id.displayNumberScroller);
        scrollerDisplayOperations = (HorizontalScrollView) findViewById(R.id.displayOperationNumberScroller);
        btnAC = findViewById(R.id.btnAC);
        btnDel = findViewById(R.id.btnDel);
        btnPerc = findViewById(R.id.btnPerc);
        btnDivide = findViewById(R.id.btnDivide);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btnMultiply = findViewById(R.id.btnMultiply);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btnMinus = findViewById(R.id.btnMinus);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btnPlus = findViewById(R.id.btnPlus);
        btn0 = findViewById(R.id.btn0);
        btnDot = findViewById(R.id.btnDot);
        btnEqual = findViewById(R.id.btnEqual);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnAC.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnMultiply.setOnClickListener(this);
        btnDivide.setOnClickListener(this);
        btnPerc.setOnClickListener(this);
        btnDot.setOnClickListener(this);
        btnEqual.setOnClickListener(this);
        numberDisplay.setOnClickListener(this);
        operationsDisplay.setOnClickListener(this);


        if(sharedPref.getInt("loginpin", 111111111)==111111111){
            ShowcaseConfig config = new ShowcaseConfig();
            config.setDelay(200); // half second between each showcase view

            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "2001");

            sequence.setConfig(config);

            sequence.addSequenceItem(numberDisplay,
                    "Enter 4 to 8 number pin", "GOT IT");

            sequence.addSequenceItem(btnEqual,
                    "Press \"=\" button to login", "GOT IT");

            sequence.start();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        stringSpecial = numberDisplay.getText().toString();
        outState.putString("numberDisplay", stringSpecial);

        stringSpecial = operationsDisplay.getText().toString();
        outState.putString("operationsDisplay", stringSpecial);

        outState.putDouble("resultValue", value);

        outState.putBoolean("numberClicked", numberClicked);

        outState.putInt("dotCount", dotCount);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        numberDisplay.setText(savedInstanceState.getString("numberDisplay"));
        operationsDisplay.setText(savedInstanceState.getString("operationsDisplay"));

        value = savedInstanceState.getDouble("resultValue");

        numberClicked = savedInstanceState.getBoolean("numberClicked");

        dotCount = savedInstanceState.getInt("dotCount");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn0:
                numberDisplay.setText(numberDisplay.getText()+"0");
                operationsDisplay.setText(operationsDisplay.getText()+"0");
                numberClicked=true;
                break;
            case R.id.btn1:
                numberDisplay.setText(numberDisplay.getText()+"1");
                operationsDisplay.setText(operationsDisplay.getText()+"1");
                numberClicked=true;
                break;
            case R.id.btn2:
                numberDisplay.setText(numberDisplay.getText()+"2");
                operationsDisplay.setText(operationsDisplay.getText()+"2");
                numberClicked=true;
                break;
            case R.id.btn3:
                numberDisplay.setText(numberDisplay.getText()+"3");
                operationsDisplay.setText(operationsDisplay.getText()+"3");
                numberClicked=true;
                break;
            case R.id.btn4:
                numberDisplay.setText(numberDisplay.getText()+"4");
                operationsDisplay.setText(operationsDisplay.getText()+"4");
                numberClicked=true;
                break;
            case R.id.btn5:
                numberDisplay.setText(numberDisplay.getText()+"5");
                operationsDisplay.setText(operationsDisplay.getText()+"5");
                numberClicked=true;
                break;
            case R.id.btn6:
                numberDisplay.setText(numberDisplay.getText()+"6");
                operationsDisplay.setText(operationsDisplay.getText()+"6");
                numberClicked=true;
                break;
            case R.id.btn7:
                numberDisplay.setText(numberDisplay.getText()+"7");
                operationsDisplay.setText(operationsDisplay.getText()+"7");
                numberClicked=true;
                break;
            case R.id.btn8:
                numberDisplay.setText(numberDisplay.getText()+"8");
                operationsDisplay.setText(operationsDisplay.getText()+"8");
                numberClicked=true;
                break;
            case R.id.btn9:
                numberDisplay.setText(numberDisplay.getText()+"9");
                operationsDisplay.setText(operationsDisplay.getText()+"9");
                numberClicked=true;
                break;
            case R.id.btnAC:
                numberDisplay.setText(null);
                operationsDisplay.setText(null);
                stringNumber="";
                stringSpecial="";
                value=0;
                numberClicked=false;
                dotCount=0;
                break;
            case R.id.btnDel:
                stringSpecial = operationsDisplay.getText().toString();

                if(!stringSpecial.isEmpty()) {

                    if (Double.toString(value).equals(numberDisplay.getText().toString())) {

                    } else if(stringSpecial.endsWith(".")) {
                        dotCount=0;
                        stringSpecial = stringSpecial.substring(0, stringSpecial.length()-1);
                        operationsDisplay.setText(stringSpecial);

                    } else {
                        stringSpecial = stringSpecial.substring(0, stringSpecial.length() - 1);
                        operationsDisplay.setText(stringSpecial);
                        numberClicked=false;
                        if(stringSpecial.endsWith("1") || stringSpecial.endsWith("2") || stringSpecial.endsWith("3") || stringSpecial.endsWith("4") || stringSpecial.endsWith("5") || stringSpecial.endsWith("6") || stringSpecial.endsWith("7") || stringSpecial.endsWith("8") || stringSpecial.endsWith("9") || stringSpecial.endsWith("0") || stringSpecial.endsWith(")")) {
                            numberClicked=true;
                        }
                    }
                }

                stringSpecial = numberDisplay.getText().toString();

                if(!stringSpecial.isEmpty()) {

                    if (Double.toString(value).equals(numberDisplay.getText().toString())) {

                    } else if(stringSpecial.equals("Invalid expression") || stringSpecial.equals("Can't divide by 0")) {

                    } else {

                        stringSpecial = stringSpecial.substring(0, stringSpecial.length() - 1);
                        numberDisplay.setText(stringSpecial);
                    }
                }
                break;
            case R.id.btnPlus:
                checkNumberDisplay();

                stringSpecial = operationsDisplay.getText().toString();

                if(stringSpecial.isEmpty()) {

                } else if(stringSpecial.charAt(stringSpecial.length()-1)=='+' || stringSpecial.charAt(stringSpecial.length()-1)=='-' || stringSpecial.charAt(stringSpecial.length()-1)=='*' || stringSpecial.charAt(stringSpecial.length()-1)=='/') {

                } else {
                    operationsDisplay.setText(operationsDisplay.getText() + "+");
                    numberDisplay.setText(null);
                    numberClicked=false;
                    dotCount=0;

                }
                break;
            case R.id.btnMinus:
                checkNumberDisplay();

                stringSpecial = operationsDisplay.getText().toString();

                if(stringSpecial.endsWith("sqrt(")) {

                } else {
                    operationsDisplay.setText(operationsDisplay.getText() + "-");
                    numberDisplay.setText("-");
                    numberClicked = false;
                    dotCount = 0;
                }
                break;
            case R.id.btnMultiply:
                checkNumberDisplay();


                stringSpecial = operationsDisplay.getText().toString();

                if(stringSpecial.isEmpty()) {

                } else if(stringSpecial.charAt(stringSpecial.length()-1)=='(') {

                } else if(stringSpecial.charAt(stringSpecial.length()-1)=='+' || stringSpecial.charAt(stringSpecial.length()-1)=='-' || stringSpecial.charAt(stringSpecial.length()-1)=='*' || stringSpecial.charAt(stringSpecial.length()-1)=='/') {

                } else {
                    operationsDisplay.setText(operationsDisplay.getText() + "*");
                    numberDisplay.setText(null);
                    numberClicked=false;
                    dotCount=0;
                }
                break;
            case R.id.btnDivide:
                checkNumberDisplay();

                stringSpecial = operationsDisplay.getText().toString();

                if(stringSpecial.isEmpty()) {

                } else if(stringSpecial.charAt(stringSpecial.length()-1)=='(') {

                } else if(stringSpecial.charAt(stringSpecial.length()-1)=='+' || stringSpecial.charAt(stringSpecial.length()-1)=='-' || stringSpecial.charAt(stringSpecial.length()-1)=='*' || stringSpecial.charAt(stringSpecial.length()-1)=='/') {

                } else {
                    numberClicked=false;
                    operationsDisplay.setText(operationsDisplay.getText() + "/");
                    numberDisplay.setText(null);
                    numberClicked=false;
                    dotCount=0;
                }
                break;
            case R.id.btnPerc:
                checkNumberDisplay();

                stringSpecial = operationsDisplay.getText().toString();

                if(stringSpecial.isEmpty()) {

                } else if(stringSpecial.charAt(stringSpecial.length()-1)=='+' || stringSpecial.charAt(stringSpecial.length()-1)=='-' || stringSpecial.charAt(stringSpecial.length()-1)=='*' || stringSpecial.charAt(stringSpecial.length()-1)=='/') {

                } else {
                    float abc = Float.parseFloat(stringSpecial)/100;
                    numberDisplay.setText(String.valueOf(abc));
                    operationsDisplay.setText(String.valueOf(abc));
                    numberClicked=false;
                    dotCount=0;

                }
                break;
            case R.id.btnDot:
                stringSpecial = operationsDisplay.getText().toString();
                if (numberClicked == false) {

                } else if(stringSpecial.endsWith("(") || stringSpecial.endsWith("+") || stringSpecial.endsWith("-") || stringSpecial.endsWith("*") || stringSpecial.endsWith("/")) {

                } else {
                    if (dotCount == 1) {

                    } else {
                        dotCount++;
                        operationsDisplay.setText(operationsDisplay.getText() + ".");
                        stringSpecial = numberDisplay.getText().toString();
                        if(!stringSpecial.contains(".")) {
                            numberDisplay.setText(numberDisplay.getText() + "."); }
                    }
                }
                break;
            case R.id.btnEqual:
                if(numberClicked==false) {

                } else {

                    stringNumber = operationsDisplay.getText().toString();
                    if(stringNumber.matches("[0-9]+") && stringNumber.length() > 3 && stringNumber.length() < 9){
                        checkLogin(stringNumber);
                    } else if(stringNumber.contains("Infinity")) {
                        numberDisplay.setText("Infinity");
                    } else {

                        expression = new ExpressionBuilder(stringNumber).build();

                        try {
                            value = expression.evaluate();
                            numberDisplay.setText(Double.toString(value));
                        } catch (ArithmeticException e) {
                            numberDisplay.setText("Can't divide by 0");
                        }
                    }
                }
                break;
            case R.id.displayNumber:
                clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clip = ClipData.newPlainText("number", numberDisplay.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied result to clipboard", Toast.LENGTH_SHORT).show();
                break;
            case R.id.displayOperationNumber:
                clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clip = ClipData.newPlainText("operations", operationsDisplay.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied operations to clipboard", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void checkLogin(final String stringNum) {

        if(sharedPref.getInt("loginpin", 111111111)==111111111){

            if(confirmPin.equals(stringNum)){
                sharedPref.edit().putInt("loginpin", Integer.valueOf(stringNum)).commit();
                startActivity(new Intent(MainActivity.this,SplashActivity.class));
                finish();
            }else if(confirmPin.equals("confirmPin")){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Re-Enter your password and press \"=\" button to login.");
                builder1.setCancelable(false);
                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                confirmPin = stringNum;
                                numberDisplay.setText(null);
                                operationsDisplay.setText(null);
                                stringNumber="";
                                stringSpecial="";
                                value=0;
                                numberClicked=false;
                                dotCount=0;
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else {
                Toast.makeText(this, "Pin doesn't match. Please enter valid pin.", Toast.LENGTH_LONG).show();
            }

        }else if(sharedPref.getInt("loginpin", 111111111)==Integer.valueOf(stringNum)){
            startActivity(new Intent(MainActivity.this,SplashActivity.class));
        }
    }


    public void checkNumberDisplay() {
        stringSpecial = numberDisplay.getText().toString();
        if(Double.toString(value).equals(stringSpecial)) {
            operationsDisplay.setText(stringSpecial);
        }
    }

    @Override
    protected void onResume() {
        numberDisplay.setText(null);
        operationsDisplay.setText(null);
        stringNumber="";
        stringSpecial="";
        value=0;
        numberClicked=false;
        dotCount=0;
        super.onResume();
    }
}
