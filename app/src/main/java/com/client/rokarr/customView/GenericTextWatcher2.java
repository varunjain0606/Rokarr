package com.client.rokarr.customView;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.client.rokarr.R;
import com.client.rokarr.fragments.ThreeFragment;

import java.math.BigDecimal;


/**
 * Created by Varun on 07-12-2015.
 */
public class GenericTextWatcher2 implements TextWatcher {
    private View view;
    Fragment fragment;
    Context context;
    public GenericTextWatcher2(ThreeFragment fragment, View view) {
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        ThreeFragment mainFragment = (ThreeFragment) fragment;
        String text = charSequence.toString();
        switch (view.getId()) {
            case R.id.pop_product_price1:
                if (!mainFragment.qty.getText().toString().equals("") && !mainFragment.qty.getText().toString().equals("0")) {
                    if (text.length() != 0) {
                        if (!mainFragment.tax.getText().toString().equals("")) {
                            Float value = ((Float.parseFloat(mainFragment.qty.getText().toString())) * Float.parseFloat(text.toString()));
                            value = value + (Float.parseFloat(mainFragment.tax.getText().toString()) * value) / 100;
                            mainFragment.amount.setText(value.toString());
                        } else {
                            Float value = ((Float.parseFloat(mainFragment.qty.getText().toString())) * Float.parseFloat(text.toString()));
                            mainFragment.amount.setText(value.toString());
                        }
                    } else {
                        mainFragment.qty.setText("");
                        mainFragment.tax.setText("");
                        mainFragment.amount.setText("");
                    }
                }
                break;
            case R.id.pop_product_qty1:
                if (!mainFragment.price.getText().toString().equals("")) {
                    if (text.length() != 0) {
                        if (text.toString().equals("0")) {
                            Toast.makeText(this.context, "Value not accepted", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!mainFragment.tax.getText().toString().equals("")) {
                                Float value = ((Float.parseFloat(mainFragment.price.getText().toString())) * Float.parseFloat(text.toString()));
                                value = value + (Float.parseFloat(mainFragment.tax.getText().toString()) * value) / 100;
                                mainFragment.amount.setText(value.toString());
                            } else {
                                Float value = ((Float.parseFloat(mainFragment.price.getText().toString())) * Float.parseFloat(text.toString()));
                                mainFragment.amount.setText(value.toString());
                            }
                        }
                    }
                }
                break;
            case R.id.pop_product_tax1:
                if (!mainFragment.price.getText().toString().equals("") && !mainFragment.qty.getText().toString().equals("")) {
                    if (text.length() != 0) {
                        if(!text.toString().equals(".")) {
                            Double value = ((Double.parseDouble(mainFragment.price.getText().toString())) *
                                    Double.parseDouble(mainFragment.qty.getText().toString()));
                            value = value + (Double.parseDouble(text.toString()) * value) / 100;
                            value = round(value, 0, BigDecimal.ROUND_HALF_UP);
                            mainFragment.amount.setText(value.toString());
                        }
                    } else {
                        mainFragment.amount.setText("" + ((Double.parseDouble(mainFragment.price.getText().toString())) *
                                Double.parseDouble(mainFragment.qty.getText().toString())) + "");
                        Toast.makeText(this.context, "Enter both price and quantity!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
