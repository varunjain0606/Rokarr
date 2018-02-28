package com.client.rokarr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.client.rokarr.customView.CustomAutoCompleteTextChangedListener;
import com.client.rokarr.customView.CustomAutoCompleteView;
import com.client.rokarr.loginandregistration.helper.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varun on 17-11-2015.
 */
public class AddProducts extends Activity{
    ListView lv;
    String frag;
    public CustomAutoCompleteView product;
    public Button addProd, saveButton;
    ImageAdapter adapter;
    String name, billNo;
    public TextView price, productDet, quant, tax1, total;
    public String[] item1 = new String[] {"Please search..."};
    public ArrayAdapter<String> myAdapter1;
    private DatabaseHandler databaseH;
    ArrayList<String> prodDet = new ArrayList<String>();
    ArrayList<String> prodPrice = new ArrayList<String>();
    ArrayList<String> quantity = new ArrayList<String>();
    ArrayList<String> tax = new ArrayList<String>();
    ArrayList<String> total1 = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseH = new DatabaseHandler(getApplicationContext());
        setContentView(R.layout.add_products);
        Bundle bundle = getIntent().getExtras();
        frag = bundle.getString("fragment");
        prodDet.add(bundle.getString("prodDetails1"));
        prodPrice.add(bundle.getString("prodPrice1"));
        quantity.add(bundle.getString("quant1"));
        tax.add(bundle.getString("tax1"));
        name = bundle.getString("name");
        billNo = bundle.getString("billno");
        Float value = Float.parseFloat(prodPrice.get(0).toString()) * Float.parseFloat(quantity.get(0).toString());
        if (!tax.get(0).toString().equals(""))
            value = value + (Float.parseFloat(tax.get(0).toString()) * value) / 100;
        total1.add(String.valueOf(value));
        lv = (ListView) findViewById(R.id.listView);
        setfields();
        adapter = new ImageAdapter(this,prodDet,quantity,prodPrice,tax,total1);
        lv.setAdapter(adapter);
        setMyAdapter();
    }

    private void setfields()
    {
        addProd = (Button) findViewById(R.id.bt_addProducts);
        price = (TextView) findViewById(R.id.prodPrice2);
        quant = (TextView) findViewById(R.id.quantity2);
        productDet = (TextView) findViewById(R.id.product2);
        tax1 = (TextView) findViewById(R.id.tax2);
        total = (TextView) findViewById(R.id.tb_totalprice);
        saveButton = (Button) findViewById(R.id.bt_saveAndMove);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(AddProducts.this);
                a_builder.setMessage("Do you want to save?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lv.scrollTo(0, lv.getHeight());
                        //lv.smoothScrollToPosition(prodDet.size());
                        Intent resultData = new Intent();
                        resultData.putExtra("valueName", total.getText().toString());
                        resultData.putStringArrayListExtra("listProduct",prodDet);
                        resultData.putStringArrayListExtra("listPrice",prodPrice);
                        resultData.putStringArrayListExtra("listQuant",quantity);
                        setResult(Activity.RESULT_OK, resultData);
                        int i =0;
                        while(i < prodDet.size())
                        {
                            Float price = Float.parseFloat(prodPrice.get(i).toString());
                            if(!tax.get(i).toString().equals(""))
                            price = price + (Float.parseFloat(tax.get(i).toString())*price)/100;
                            if(frag.contains("OneFragment")) {
                                databaseH.addProdDetails(name, prodDet.get(i).toString(), String.valueOf(price),quantity.get(i).toString(),
                                        tax.get(i).toString(), billNo,1);
                            }
                            else if (frag.contains("TwoFragment"))
                            {
                                databaseH.addProdDetails(name, prodDet.get(i).toString(), String.valueOf(price), quantity.get(i).toString(),
                                        tax.get(i).toString() , billNo,2);
                            }
                            databaseH.create(prodDet.get(i).toString(),1);
                            i = i+1;
                        }
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Save Details");
                alert.show();
            }
        });

        addProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!productDet.getText().toString().equals("") && !price.getText().toString().equals("")
                        && !quant.getText().toString().equals(""))
                {
                    if(prodDet.contains(productDet.getText().toString()))
                    {
                        Toast.makeText(getApplicationContext(),"Product already exists! Please update that",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        prodDet.add(productDet.getText().toString());
                        prodPrice.add(price.getText().toString());
                        quantity.add(quant.getText().toString());
                        tax.add(tax1.getText().toString());
                        Float value = Float.parseFloat(price.getText().toString()) * Float.parseFloat(quant.getText().toString());
                        if (!tax1.getText().toString().equals(""))
                            value = value + (Float.parseFloat(tax1.getText().toString()) * value) / 100;
                        total1.add(String.valueOf(value));

                        adapter = new ImageAdapter(AddProducts.this, prodDet, quantity, prodPrice, tax, total1);
                        lv.setAdapter(adapter);
                        setMyAdapter();
                        productDet.setText("");
                        price.setText("");
                        quant.setText("");
                        tax1.setText("");
                        productDet.requestFocus();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter required fields!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setMyAdapter() {
        product = (CustomAutoCompleteView) findViewById(R.id.product2);
        product.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this, 4));
        myAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item1);
        product.setAdapter(myAdapter1);
    }

    public String[] getItemsFromDb(String searchTerm){

        // add items on the array dynamically
            List<String> products = databaseH.read1(searchTerm, 1);
            int rowCount = products.size();
            String[] item1 = new String[rowCount];
            int x = 0;

            for (String record : products) {

                item1[x] = record;
                x++;
            }
            return item1;
    }
}


