package com.example.s984904.eztips;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    String taxPS, tipsPS;
    EditText taxInput, tipsInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.btn);
        taxInput = (EditText) findViewById(R.id.taxP);
        tipsInput = (EditText) findViewById(R.id.tipsP);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                taxPS = taxInput.getText().toString();
                if (taxPS.equals("")) {
                    taxPS = taxInput.getHint().toString();
                }

                tipsPS = tipsInput.getText().toString();
                if (tipsPS.equals("")) {
                    tipsPS = tipsInput.getHint().toString();
                }
            }
        };
        taxInput.addTextChangedListener(textWatcher);
        tipsInput.addTextChangedListener(textWatcher);

        View.OnTouchListener cal = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String priceS = ((EditText) findViewById(R.id.price)).getText().toString();
                if (priceS.equals("")) {
                    priceS = "0";
                }
                double price = Double.parseDouble(priceS);

                taxPS = taxInput.getText().toString();
                if (taxPS.equals("")) {
                    taxPS = taxInput.getHint().toString();
                }
                Double taxP = Double.parseDouble(taxPS) / 100;

                tipsPS = tipsInput.getText().toString();
                if (tipsPS.equals("")) {
                    tipsPS = tipsInput.getHint().toString();
                }
                Double tipsP = Double.parseDouble(tipsPS) / 100;

                double charge = (double) (Math.round((price + price * taxP) * 100)) / 100;

                double tips1 = (double) (Math.round(price * tipsP * 100)) / 100;
                double total1 = charge + tips1;

                double total2 = Math.floor(total1);
                double tips2 = (double) (Math.round((total2 - charge) * 100)) / 100;
                double tipsP2 = (double) (Math.round(tips2 / price * 10000)) / 100;

                double total3 = total2 + 1;
                double tips3 = (double) (Math.round((total3 - charge) * 100)) / 100;
                double tipsP3 = 0;
                if (price == 0) {
                    tipsP3 = 100;
                } else {
                    tipsP3 = (double) (Math.round(tips3 / price * 10000)) / 100;
                }

                double tips4 = Math.floor(tips1);
                double total4 = tips4 + charge;
                double tipsP4 = (double) (Math.round(tips4 / price * 10000)) / 100;

                double tips5 = tips4 + 1;
                double total5 = tips5 + charge;
                double tipsP5 = 0;
                if (price == 0) {
                    tipsP5 = 100;
                } else {
                    tipsP5 = (double) (Math.round(tips5 / price * 10000)) / 100;
                }


                ((TextView) findViewById(R.id.tips1)).setText(String.valueOf(tips1) + "$");
                ((TextView) findViewById(R.id.total1)).setText(String.valueOf(total1) + "$");

                if (tips2 >= 0 && total2 >= 0) {
                    ((TextView) findViewById(R.id.tips2)).setText(formate(tipsP2, tips2));
                    ((TextView) findViewById(R.id.total2)).setText(String.valueOf(total2) + "$");
                } else {
                    ((TextView) findViewById(R.id.tips2)).setText("N/A");
                    ((TextView) findViewById(R.id.total2)).setText("N/A");
                }

                ((TextView) findViewById(R.id.tips3)).setText(formate(tipsP3, tips3));
                ((TextView) findViewById(R.id.total3)).setText(String.valueOf(total3) + "$");

                ((TextView) findViewById(R.id.tips4)).setText(formate(tipsP4, tips4));
                ((TextView) findViewById(R.id.total4)).setText(String.valueOf(total4) + "$");

                ((TextView) findViewById(R.id.tips5)).setText(formate(tipsP5, tips5));
                ((TextView) findViewById(R.id.total5)).setText(String.valueOf(total5) + "$");


                return false;
            }
        };
        btn.setOnTouchListener(cal);
    }

    @Override
    protected void onPause() {
        SharedPreferences prefs = getSharedPreferences("EZtips", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("tempTax", taxPS);
        editor.putString("tempTips", tipsPS);
        editor.commit();

        super.onPause();

    }

    @Override
    protected void onResume() {
        SharedPreferences prefs = getSharedPreferences("EZtips", MODE_PRIVATE);
        taxPS = prefs.getString("tempTax", "7.0");
        ((EditText) findViewById(R.id.taxP)).setHint(taxPS);
        tipsPS = prefs.getString("tempTips", "15.0");
        ((EditText) findViewById(R.id.tipsP)).setHint(tipsPS);

        super.onResume();
    }


    private SpannableStringBuilder formate(double tipsP, double tips) {
        String formated2 = String.format("(%s%s)%s%s", tipsP, "%", String.valueOf(tips), "$");

        SpannableStringBuilder sb = new SpannableStringBuilder(formated2);
        int start = formated2.indexOf("(");
        int stop = formated2.indexOf(")");
        // Span to set text color to some RGB value
        final ForegroundColorSpan fcs = new ForegroundColorSpan(0xFFFFFF00);
        // Set the text color for first 4 characters
        sb.setSpan(fcs, start, stop + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }
}