package com.client.rokarr.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.client.rokarr.R;
import com.client.rokarr.customView.CustomGridView;
import com.client.rokarr.loginandregistration.helper.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Varun on 30-11-2015.
 */
public class FourFragment extends Fragment implements View.OnClickListener{

    static View rootview;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    ArrayList gridProd1, gridPrice1,gridProd2,gridPrice2;
    CustomGridView customGridView1,customGridView2;
    DatabaseHandler databaseH;
    ListView gridView1, gridView2;
    int yy,mm,dd;
    EditText date;
    public TextView receipttotal,paymenttotal;
    public FourFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_four, container, false);
        setDateTimeField();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        gridView1 = (ListView) rootview.findViewById(R.id.receiptlist);
        gridView2 = (ListView) rootview.findViewById(R.id.paymentlist);
        receipttotal = (TextView) rootview.findViewById(R.id.totalsheet1);
        paymenttotal = (TextView) rootview.findViewById(R.id.totalsheet2);
        filllist1();
        filllist2();
        return rootview;
    }

    private void setDateTimeField() {
        date = (EditText) rootview.findViewById(R.id.sheetdate);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        yy = c.get(Calendar.YEAR);
        mm = c.get(Calendar.MONTH)+1;
        dd = c.get(Calendar.DAY_OF_MONTH);
        date.setText(dd + "-" + mm + "-" + yy);
        databaseH = new DatabaseHandler(getContext());
        date.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
                filllist1();
                filllist2();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    void filllist1()
    {
        gridProd1 = databaseH.getDataFromDate(date.getText().toString(), 1,true);
        gridPrice1 = databaseH.getDataFromDate(date.getText().toString(), 2,true);
        if (gridPrice1.size() > 0 && gridProd1.size() > 0) {
            customGridView1 = new CustomGridView(getContext(), gridProd1, gridPrice1, 3,this);
            gridView1.setAdapter(customGridView1);
        }
    }

    void filllist2()
    {
        gridProd2 = databaseH.getDataFromDate(date.getText().toString(), 1,false);
        gridPrice2 = databaseH.getDataFromDate(date.getText().toString(), 2,false);
        if (gridPrice1.size() > 0 && gridProd1.size() > 0) {
            customGridView2 = new CustomGridView(getContext(), gridProd2, gridPrice2, 4, this);
            gridView2.setAdapter(customGridView2);
        }
    }
    @Override
    public void onClick(View v) {
        if(v == date) {
            datePicker.show();
        }
    }
}
