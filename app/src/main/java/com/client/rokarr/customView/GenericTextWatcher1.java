package com.client.rokarr.customView;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.client.rokarr.R;
import com.client.rokarr.fragments.TwoFragment;
import com.client.rokarr.loginandregistration.helper.DatabaseHandler;

import java.math.BigDecimal;

/**
 * Created by Varun on 01-12-2015.
 */
public class GenericTextWatcher1 implements TextWatcher {
    private View view;
    Fragment fragment;
    Context context;
    public GenericTextWatcher1(TwoFragment fragment, View view) {
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.view = view;
    }
    public static double round(double unrounded, int precision, int roundingMode)
    {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        TwoFragment mainFragment = ((TwoFragment) fragment);
        String text = charSequence.toString();
        switch(view.getId()){
            case R.id.purprodPrice:
                if (!mainFragment.quantity.getText().toString().equals("") && !mainFragment.quantity.getText().toString().equals("0")) {
                    if (text.length() != 0) {
                        if (!mainFragment.tax.getText().toString().equals("")) {
                            Float value = ((Float.parseFloat(mainFragment.quantity.getText().toString())) * Float.parseFloat(text.toString()));
                            value = value + (Float.parseFloat(mainFragment.tax.getText().toString()) * value) / 100;
                            mainFragment.total.setText(value.toString());
                        } else {
                            Float value = ((Float.parseFloat(mainFragment.quantity.getText().toString())) * Float.parseFloat(text.toString()));
                            mainFragment.total.setText(value.toString());
                        }
                    } else {
                        mainFragment.quantity.setText("");
                        mainFragment.tax.setText("");
                        mainFragment.total.setText("");
                    }
                }
                break;
            case R.id.purdiscount:
                if(!mainFragment.total.getText().toString().equals("") && !mainFragment.total.getText().toString().equals("0"))
                {
                    if(text.length() != 0)
                    {
                        if(!text.toString().equals(".")) {
                            Double value = Double.parseDouble(mainFragment.total.getText().toString());
                            value = value - (Double.parseDouble(text.toString()) * value) / 100;
                            value = round(value, 0, BigDecimal.ROUND_HALF_UP);
                            if (value > 0)
                                mainFragment.amount.setText(value.toString());
                            else {
                                Toast.makeText(this.context, "Invalid discount value!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else
                    {
                        mainFragment.amount.setText(mainFragment.total.getText().toString());
                    }
                }
                break;
            case R.id.purtotalAmount:
                mainFragment.amount.setText(text.toString());
                break;
            case R.id.purquantity:
                if(!mainFragment.price.getText().toString().equals(""))
                {
                    if(text.length() != 0)
                    {
                        if(text.toString().equals("0"))
                        {
                            Toast.makeText(this.context,"Value not accepted",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(!mainFragment.tax.getText().toString().equals(""))
                            {
                                Float value = ((Float.parseFloat(mainFragment.price.getText().toString())) * Float.parseFloat(text.toString()));
                                value = value + (Float.parseFloat(mainFragment.tax.getText().toString())*value)/100;
                                mainFragment.total.setText(value.toString());
                            }
                            else {
                                Float value = ((Float.parseFloat(mainFragment.price.getText().toString())) * Float.parseFloat(text.toString()));
                                mainFragment.total.setText(value.toString());
                            }
                        }
                    }
                }
                break;
            case R.id.purtax:
                if(!mainFragment.price.getText().toString().equals("") && !mainFragment.quantity.getText().toString().equals(""))
                {
                    if(text.length() != 0) {
                        if(!text.toString().equals(".")) {
                            Double value = ((Double.parseDouble(mainFragment.price.getText().toString())) *
                                    Double.parseDouble(mainFragment.quantity.getText().toString()));
                            value = value + (Double.parseDouble(text.toString()) * value) / 100;
                            value = round(value, 0, BigDecimal.ROUND_HALF_UP);
                            mainFragment.total.setText(value.toString());
                        }
                    }
                    else
                    {
                        mainFragment.total.setText(""+((Double.parseDouble(mainFragment.price.getText().toString())) *
                                Double.parseDouble(mainFragment.quantity.getText().toString()))+"");
                        Toast.makeText(this.context,"Enter both price and quantity!",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.purNetAmount:
                if(mainFragment.balance.getText().toString().equals("") && !mainFragment.names.getText().toString().equals("")) {
                    mainFragment.balance.setText(mainFragment.amount.getText().toString());
                }
                else if(!mainFragment.balance.getText().toString().equals(""))
                {
                    DatabaseHandler databaseHandler = new DatabaseHandler(context);
                    if(!mainFragment.amount.getText().toString().equals("")) {
                        String value = null;
                        Float value1 = Float.valueOf(0);
                        if(true == databaseHandler.checkIfExists(mainFragment.names.getText().toString(),5))
                        {
                            value = databaseHandler.getBalance(mainFragment.names.getText().toString(),2);
                        }
                        //String xyz = mainFragment.balance.getText().toString();
                        if(!mainFragment.balance.getText().toString().equals("")) {
                            if (value != "" && value != "0.0" && value != "0" && value != null) {
                                value1 = Float.parseFloat(value) + Float.parseFloat(mainFragment.amount.getText().toString());
                            } else {
                                value1 = Float.parseFloat(mainFragment.amount.getText().toString());
                            }
                        }
                        mainFragment.balance.setText(String.valueOf(value1));
                    }
                }
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
