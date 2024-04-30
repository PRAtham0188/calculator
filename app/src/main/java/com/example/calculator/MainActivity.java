package com.example.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView resultTv, solutionTv;
    MaterialButton buttonC, buttonBrackOpen, buttonBrackClose;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAc, buttonDot;

    ToggleButton toggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        assignId(buttonC, R.id.button_c);
        assignId(buttonBrackOpen, R.id.button_open_bracket);
        assignId(buttonBrackClose, R.id.button_closed_bracket);
        assignId(buttonDivide, R.id.button_divide);
        assignId(buttonMultiply, R.id.button_multiply);
        assignId(buttonPlus, R.id.button_plus);
        assignId(buttonMinus, R.id.button_minus);
        assignId(buttonEquals, R.id.button_equals);
        assignId(button0, R.id.button_0);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        assignId(buttonAc, R.id.button_ac);
        assignId(buttonDot, R.id.button_dot);
        toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(MainActivity.this, UnitConverter.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        switch (buttonText) {
            case "C":
                solutionTv.setText("");
                break;
            case "=":
                String expression = solutionTv.getText().toString();
                try {
                    double result = evaluateExpression(expression);
                    if (result % 1 == 0) {
                        resultTv.setText(String.valueOf((int) result));
                    } else {
                        resultTv.setText(String.valueOf(result));
                    }
                } catch (ArithmeticException | NumberFormatException e) {
                    resultTv.setText("Error");
                }
                break;
            case "AC":
                solutionTv.setText("");
                resultTv.setText("0");
                break;
            default:
                solutionTv.append(buttonText);
                break;
        }
    }


    private double evaluateExpression(String expression) throws ArithmeticException, NumberFormatException {
        expression = expression.replace('÷', '/');
        expression = expression.replace('×', '*');
        return evaluateBodmas(expression);
    }

    private double evaluateBodmas(String expression) throws ArithmeticException, NumberFormatException {
        while (expression.contains("(")) {
            int openIndex = expression.lastIndexOf("(");
            int closeIndex = expression.indexOf(")", openIndex);
            String subExpression = expression.substring(openIndex + 1, closeIndex);
            double subResult = evaluateBodmas(subExpression);
            expression = expression.substring(0, openIndex) + subResult + expression.substring(closeIndex + 1);
        }

        String[] parts = expression.split("\\+");
        double result = evaluateMultiplyDivide(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            result += evaluateMultiplyDivide(parts[i]);
        }
        return result;
    }

    private double evaluateMultiplyDivide(String expression) throws ArithmeticException, NumberFormatException {
        String[] parts = expression.split("-");
        double result = evaluateTerm(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            result -= evaluateTerm(parts[i]);
        }
        return result;
    }

    private double evaluateTerm(String term) throws ArithmeticException, NumberFormatException {
        String[] factors = term.split("\\*");
        double result = Double.parseDouble(factors[0]);
        for (int i = 1; i < factors.length; i++) {
            result *= Double.parseDouble(factors[i]);
        }
        return result;
    }
}