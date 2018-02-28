package com.client.rokarr.fragments;

/**
 * Created by Varun on 10-11-2015.
 */
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.client.rokarr.AddProducts;
import com.client.rokarr.PrintPreview;
import com.client.rokarr.R;
import com.client.rokarr.customView.CustomAutoCompleteTextChangedListener;
import com.client.rokarr.customView.CustomAutoCompleteView;
import com.client.rokarr.customView.GenericTextWatcher;
import com.client.rokarr.loginandregistration.helper.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class OneFragment extends Fragment implements View.OnClickListener{

    static View rootview;
    static int INTEGER_VALUE = 1;
    Spinner paymode;
    private DatabaseHandler databaseH;
    ScrollView scrollView;
    public TextView quantity, total,price,tax,amount,discount, billNo, balance,paid;
    public Button save, addProducts, clear, preview;
    public CustomAutoCompleteView product,names,address;
    EditText date,paystyle;
    int dd,mm,yy;
    public String[] item1 = new String[] {"Please search..."};
    public String[] item2 = new String[] {"Please search..."};
    public String[] item3 = new String[] {"Please search..."};
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    public ArrayAdapter<String> myAdapter1, myAdapter2,myAdapter3;
    public ArrayList<String> prodDetails, prodPrice,quant;

    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootview = inflater.inflate(R.layout.fragment_one, container, false);
        databaseH = new DatabaseHandler(getContext());
        setfields();
        valueChanger();
        setMyAdapter();
        setDateTimeField();
        return rootview;
    }

    private void setMyAdapter()
    {
        product = (CustomAutoCompleteView)rootview.findViewById(R.id.product);
        product.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this, 1));
        myAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, item1);
        product.setAdapter(myAdapter1);

        names = (CustomAutoCompleteView) rootview.findViewById(R.id.clientName);
        names.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this, 2));
        myAdapter2 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,item2);
        names.setAdapter(myAdapter2);

        address = (CustomAutoCompleteView) rootview.findViewById(R.id.clientAddress);
        address.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this, 3));
        myAdapter3 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,item3);
        address.setAdapter(myAdapter3);

    }
    private void setDateTimeField() {
        date = (EditText) rootview.findViewById(R.id.billDate);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        yy = c.get(Calendar.YEAR);
        mm = c.get(Calendar.MONTH)+1;
        dd = c.get(Calendar.DAY_OF_MONTH);
        date.setText(dd + "-" + mm + "-" + yy);
        date.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    void setfields()
    {
        quantity = (TextView)rootview.findViewById(R.id.quantity);
        total = (TextView)rootview.findViewById(R.id.totalAmount);
        price = (TextView)rootview.findViewById(R.id.prodPrice);
        tax = (TextView)rootview.findViewById(R.id.tax);
        discount = (TextView)rootview.findViewById(R.id.discount);
        amount = (TextView)rootview.findViewById(R.id.NetAmount);
        save = (Button)rootview.findViewById(R.id.bt_save);
        billNo = (TextView)rootview.findViewById(R.id.billNo);
        addProducts = (Button)rootview.findViewById(R.id.bt_addProducts);
        balance = (TextView) rootview.findViewById(R.id.balance);
        paid = (TextView) rootview.findViewById(R.id.paid);
        total.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v) {
                                        focusOnView(total);
                                    }
                                }
        );
        paymode = (Spinner) rootview.findViewById(R.id.spinner2);
        paystyle = (EditText) rootview.findViewById(R.id.checkDD);
        scrollView = (ScrollView) rootview.findViewById(R.id.scrollViewFrag1);
        paystyle.setVisibility(View.GONE);

        paymode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (paymode.getSelectedItem().toString().equals("Bank")) {
                    paystyle.setVisibility(View.VISIBLE);
                } else {
                    paystyle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        paid.setText("0");
        preview = (Button) rootview.findViewById(R.id.bt_preview);
        clear = (Button) rootview.findViewById(R.id.clear);
        prodDetails = new ArrayList<String>();
        prodPrice = new ArrayList<String>();
        quant = new ArrayList<String>();
    }

    void valueChanger()
    {
        billNo.setText("" + (databaseH.getBillCount(1)+1)+"");
        price.addTextChangedListener(new GenericTextWatcher(this, price));
        quantity.addTextChangedListener(new GenericTextWatcher(this, quantity));
        total.addTextChangedListener(new GenericTextWatcher(this, total));
        tax.addTextChangedListener(new GenericTextWatcher(this, tax));
        discount.addTextChangedListener(new GenericTextWatcher(this, discount));
        amount.addTextChangedListener(new GenericTextWatcher(this, amount));
        addProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!product.getText().toString().equals("") && !price.getText().toString().equals("")
                        && !quantity.getText().toString().equals("")) {
                    String prodDet1 = product.getText().toString();
                    String prodPrice = price.getText().toString();
                    String quant = quantity.getText().toString();
                    String tax1 = tax.getText().toString();
                    String name = names.getText().toString();
                    String billno = billNo.getText().toString();
                    String fragment = "OneFragment";
                    Intent startbuttonintent = new Intent(getActivity(), AddProducts.class);
                    startbuttonintent.putExtra("name", name);
                    startbuttonintent.putExtra("billno", billno);
                    startbuttonintent.putExtra("prodDetails1", prodDet1);
                    startbuttonintent.putExtra("prodPrice1", prodPrice);
                    startbuttonintent.putExtra("quant1", quant);
                    startbuttonintent.putExtra("tax1", tax1);
                    startbuttonintent.putExtra("fragment", fragment);
                    startActivityForResult(startbuttonintent, INTEGER_VALUE);

                } else {
                    Toast.makeText(getContext(), "Enter first product details!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!total.getText().toString().equals("") && !names.getText().toString().equals("")
                        && !date.getText().toString().equals("") && !billNo.getText().toString().equals("")) {
                    if (paid.getText().toString().equals("")) {
                        paid.setText("0.0");
                    }
                    if (paymode.getSelectedItem().toString().equals("Cash")) {
                        if (true == databaseH.addUser(billNo.getText().toString(), names.getText().toString(), amount.getText().toString(),
                                date.getText().toString(), paid.getText().toString(), "0", "", 1)) {
                            if (!paid.getText().toString().equals("")) {
                                Float bal = Float.parseFloat(balance.getText().toString());
                                bal = bal - Float.parseFloat(paid.getText().toString());
                                if (bal >= 0) {
                                    if (true == databaseH.addBalance(names.getText().toString(), String.valueOf(bal), 1)) {
                                        Toast.makeText(getContext(), "Balance updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (true == databaseH.addBalance(names.getText().toString(), balance.getText().toString(), 1)) {
                                    Toast.makeText(getContext(), "Balance updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (prodPrice.size() == 0 && prodDetails.size() == 0) {
                                databaseH.addProdDetails(names.getText().toString(), product.getText().toString(),
                                        price.getText().toString(), quantity.getText().toString(), tax.getText().toString()
                                        , billNo.getText().toString(), 1);
                            }
                            databaseH.create(product.getText().toString(), 1);
                            databaseH.create(names.getText().toString(), 2);
                            databaseH.create(address.getText().toString(), 3);
                            Toast.makeText(getContext(), "Data Saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Unable to save data", Toast.LENGTH_SHORT).show();
                        }
                    } else if (paymode.getSelectedItem().toString().equals("Bank")) {
                        if (true == databaseH.addUser(billNo.getText().toString(), names.getText().toString(), amount.getText().toString(),
                                date.getText().toString(), paid.getText().toString(), "1", paystyle.getText().toString(), 1)) {
                            if (!paid.getText().toString().equals("")) {
                                Float bal = Float.parseFloat(balance.getText().toString());
                                bal = bal - Float.parseFloat(paid.getText().toString());
                                if (bal >= 0) {
                                    if (true == databaseH.addBalance(names.getText().toString(), String.valueOf(bal), 1)) {
                                        Toast.makeText(getContext(), "Balance updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (true == databaseH.addBalance(names.getText().toString(), balance.getText().toString(), 1)) {
                                    Toast.makeText(getContext(), "Balance updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (prodPrice.size() == 0 && prodDetails.size() == 0) {
                                databaseH.addProdDetails(names.getText().toString(), product.getText().toString(),
                                        price.getText().toString(), quantity.getText().toString(), tax.getText().toString()
                                        , billNo.getText().toString(), 1);
                            }
                            databaseH.create(product.getText().toString(), 1);
                            databaseH.create(names.getText().toString(), 2);
                            databaseH.create(address.getText().toString(), 3);
                            Toast.makeText(getContext(), "Data Saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Unable to save data", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (!names.getText().toString().equals("") && !balance.getText().toString().equals("")
                        && !paid.getText().toString().equals("")) {
                    Float bal = Float.parseFloat(balance.getText().toString());
                    bal = bal - Float.parseFloat(paid.getText().toString());
                    if (bal >= 0) {
                        if (true == databaseH.addBalance(names.getText().toString(), String.valueOf(bal), 1)) {
                            databaseH.addUser(billNo.getText().toString(),names.getText().toString(),"0",date.getText().toString(),
                                    paid.getText().toString(),"0","",1);
                            Toast.makeText(getContext(), "Balance updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Few fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm((ViewGroup) rootview.findViewById(R.id.relative));
                billNo.setText("" + (databaseH.getBillCount(1) + 1) + "");
                setDateTimeField();
                prodPrice.clear();
                prodDetails.clear();
                quant.clear();
                names.requestFocus();
                paid.setText("0");
            }
        });
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!total.getText().toString().equals("") && !names.getText().toString().equals("")
                        && !product.getText().toString().equals("") && !amount.getText().toString().equals("")
                        && !price.getText().toString().equals("") && !quantity.getText().toString().equals(""))
                {
                    save.performClick();
                    ArrayList<String> totalamt = new ArrayList<String>();
                    if(prodDetails.size() > 0) {
                        for (int i = 0; i < prodDetails.size(); i++) {
                            Float value = Float.parseFloat(prodPrice.get(i).toString()) * Float.parseFloat(quant.get(i).toString());
                            totalamt.add(i, String.valueOf(value));
                        }
                    }
                    if(prodDetails.size() == 0 && prodPrice.size() == 0 && quant.size() == 0)
                    {
                        prodDetails.add(0,product.getText().toString());
                        prodPrice.add(0,price.getText().toString());
                        quant.add(0,quantity.getText().toString());
                        Float value = Float.parseFloat(prodPrice.get(0).toString()) * Float.parseFloat(quant.get(0).toString());
                        totalamt.add(0,String.valueOf(value));
                    }
                    Intent intent = new Intent(getActivity(), PrintPreview.class);
                    intent.putExtra("name1",names.getText().toString());
                    intent.putExtra("address1",address.getText().toString());
                    intent.putExtra("price",total.getText().toString());
                    intent.putExtra("netamount",amount.getText().toString());
                    intent.putExtra("billNo",billNo.getText().toString());
                    intent.putExtra("presentdate",date.getText().toString());
                    intent.putExtra("disco",discount.getText().toString());
                    intent.putExtra("paidamt",paid.getText().toString());
                    intent.putStringArrayListExtra("listproduct1", prodDetails);
                    intent.putStringArrayListExtra("listprice1", prodPrice);
                    intent.putStringArrayListExtra("listquantity1", quant);
                    intent.putStringArrayListExtra("listTotal1",totalamt);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(),"Enter all the fields!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newText = data.getStringExtra("valueName");
                    prodDetails = data.getStringArrayListExtra("listProduct");
                    prodPrice = data.getStringArrayListExtra("listPrice");
                    quant = data.getStringArrayListExtra("listQuant");
                    total.setText(newText);
                    // TODO Update your TextView.
                }
                break;
            }
        }
    }
    // this function is used in CustomAutoCompleteTextChangedListener.java
    public String[] getItemsFromDb(String searchTerm, int id){

        // add items on the array dynamically
        if(id ==1) {
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
        else if(id==2)
        {
            List<String> products = databaseH.read1(searchTerm, 2);
            int rowCount = products.size();
            String[] item2 = new String[rowCount];
            int x = 0;

            for (String record : products) {

                item2[x] = record;
                x++;
            }
            return item2;
        }
        else if (id == 3)
        {
            List<String> products = databaseH.read1(searchTerm, 3);
            int rowCount = products.size();
            String[] item3 = new String[rowCount];
            int x = 0;

            for (String record : products) {

                item3[x] = record;
                x++;
            }
            return item3;
        }
        return null;
    }
    @Override
    public void onClick(View view) {
        if(view == date) {
            datePicker.show();
        }
    }



    private void focusOnView(final TextView editText){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


}