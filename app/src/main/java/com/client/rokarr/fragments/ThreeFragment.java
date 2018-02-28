package com.client.rokarr.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.client.rokarr.PrintPreview;
import com.client.rokarr.R;
import com.client.rokarr.customView.CustomAutoCompleteTextChangedListener;
import com.client.rokarr.customView.CustomAutoCompleteView;
import com.client.rokarr.customView.CustomGridView;
import com.client.rokarr.customView.GenericTextWatcher2;
import com.client.rokarr.loginandregistration.helper.DatabaseHandler;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Varun on 15-11-2015.
 */
public class ThreeFragment extends Fragment implements View.OnClickListener {
    static View rootview;
    DatabaseHandler databaseH;
    int dd,mm,yy;
    static int balance = 0;
    public EditText qty,price,tax, amount;
    CustomGridView customGridView, customGridView1, customGridView4;
    TextView abcde,abcde1,abcde2;
    private DatePickerDialog datePicker1, datePicker2;
    private SimpleDateFormat dateFormatter;
    EditText date1,date2;
    public CustomAutoCompleteView product1,names1, product;
    ListView gridView;
    ArrayList gridProd, gridPrice,gridBill, gridName, gridBalance, gridPayID;
    Spinner spinner;
    public String[] item = new String[]{"Please search..."};
    public String[] item1 = new String[] {"Please search..."};
    public String[] item2 = new String[] {"Please search..."};
    public ArrayAdapter<String> myAdapter1, myAdapter2, myAdapter;
    Button search, checkbalance, paymode;
    public ThreeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootview = inflater.inflate(R.layout.fragment_three, container, false);
        databaseH = new DatabaseHandler(getContext());
        setFields();
        setAdapter();
        setDataTimefield();
        // Inflate the layout for this fragment
        return rootview;
    }
    void setDataTimefield()
    {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        date1.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();

        datePicker1 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                dd = dayOfMonth;
                mm = monthOfYear;
                yy = year;
                newDate.set(year, monthOfYear, dayOfMonth);
                date1.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        date2.setOnClickListener(this);
        if(!date1.getText().toString().equals("")) {
            datePicker2.getDatePicker().setMinDate(Long.parseLong(date1.getText().toString()));
        }
            datePicker2 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    //view.setMinDate();
                    if(year < yy)
                    {
                        Toast.makeText(getContext(),"Invalid date entered",Toast.LENGTH_SHORT).show();
                        date2.setText("");
                    }
                    else if(year == yy && monthOfYear < mm && (dayOfMonth < dd || dayOfMonth > dd))
                    {
                        Toast.makeText(getContext(),"Invalid date entered",Toast.LENGTH_SHORT).show();
                        date2.setText("");
                    }
                    else if(year == yy && monthOfYear == mm && dayOfMonth < dd)
                    {
                        Toast.makeText(getContext(),"Invalid date entered",Toast.LENGTH_SHORT).show();
                        date2.setText("");
                    }
                    else {
                        newDate.set(year, monthOfYear, dayOfMonth);
                        date2.setText(dateFormatter.format(newDate.getTime()));
                    }
                }

            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        }


    void setAdapter()
    {
        product1 = (CustomAutoCompleteView)rootview.findViewById(R.id.product12);
        product1.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this, 5));
        myAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, item1);
        product1.setAdapter(myAdapter1);

        names1 = (CustomAutoCompleteView) rootview.findViewById(R.id.name1);
        names1.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this,6));
        myAdapter2 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,item2);
        names1.setAdapter(myAdapter2);
    }

    void setFields()
    {
        date1 = (EditText) rootview.findViewById(R.id.fromDate);
        abcde = (TextView) rootview.findViewById(R.id.abcde);
        abcde1 = (TextView) rootview.findViewById(R.id.abcde1);
        abcde2 = (TextView) rootview.findViewById(R.id.abcde2);
        date2 = (EditText) rootview.findViewById(R.id.toDate);
        search = (Button) rootview.findViewById(R.id.bt_search);
        checkbalance = (Button) rootview.findViewById(R.id.checkBalance);
        paymode = (Button) rootview.findViewById(R.id.bt_paymode);
        gridView = (ListView) rootview.findViewById(R.id.gridView);
        spinner = (Spinner) rootview.findViewById(R.id.spinner);

        paymode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (names1.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Enter name to get Payment Details", Toast.LENGTH_SHORT).show();
                } else {
                    abcde.setText("BillNo");
                    abcde1.setText("");
                    abcde2.setText("PaymentID");
                    if (spinner.getSelectedItem().equals("Sales")) {
                        gridBill = databaseH.getPaymentID(names1.getText().toString(), true, 1);
                        gridPayID = databaseH.getPaymentID(names1.getText().toString(), true, 2);
                    } else {
                        gridBill = databaseH.getPaymentID(names1.getText().toString(), false, 1);
                        gridPayID = databaseH.getPaymentID(names1.getText().toString(), false, 2);
                    }
                    customGridView = new CustomGridView(getActivity(), gridBill, gridPayID, 5);
                    gridView.setAdapter(customGridView);
                }
            }
        });
        checkbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                balance = 1;
                abcde.setText("Name");
                abcde1.setText("");
                abcde2.setText("Balance");
                if (spinner.getSelectedItem().equals("Sales")) {
                    gridName = databaseH.getbalanceNames(true, 1);
                    gridBalance = databaseH.getbalanceNames(true, 2);

                } else {
                    gridName = databaseH.getbalanceNames(false, 1);
                    gridBalance = databaseH.getbalanceNames(false, 2);
                }
                customGridView = new CustomGridView(getActivity(), gridName, gridBalance, 5);
                gridView.setAdapter(customGridView);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (balance == 1) {
                                Calendar cal = Calendar.getInstance();
                                Intent intent = new Intent(Intent.ACTION_EDIT);
                                intent.setType("vnd.android.cursor.item/event");
                                intent.putExtra("beginTime", cal.getTimeInMillis());
                                intent.putExtra("allDay", false);
                                intent.putExtra("rrule", "FREQ=DAILY");
                                intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                                intent.putExtra("title", gridName.get(position).toString()+
                                        " Amount: " + gridBalance.get(position).toString());
                                startActivity(intent);
                            }

                    }
                });

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (!names1.getText().toString().equals("") && !product1.getText().toString().equals("")) {
                    abcde.setText("Product");
                    abcde1.setText("Price");
                    abcde2.setText("BillNo");
                    if (spinner.getSelectedItem().equals("Sales")) {
                        gridProd = databaseH.getData1(names1.getText().toString(), product1.getText().toString(), 1, true);
                        gridPrice = databaseH.getData1(names1.getText().toString(), product1.getText().toString(), 2, true);
                        gridBill = databaseH.getData1(names1.getText().toString(), product1.getText().toString(), 3, true);
                    } else if (spinner.getSelectedItem().equals("Purchase")) {
                        gridProd = databaseH.getData1(names1.getText().toString(), product1.getText().toString(), 1, false);
                        gridPrice = databaseH.getData1(names1.getText().toString(), product1.getText().toString(), 2, false);
                        gridBill = databaseH.getData1(names1.getText().toString(), product1.getText().toString(), 3, false);
                    }

                    customGridView = new CustomGridView(getActivity(), gridProd, gridPrice, gridBill, 1);
                    gridView.setAdapter(customGridView);
                } else if (product1.getText().toString().equals("") && !names1.getText().toString().equals("") &&
                        date1.getText().toString().equals("") && date2.getText().toString().equals("")) {
                    balance = 0;
                    abcde.setText("BillNo.");
                    abcde1.setText("Amount");
                    abcde2.setText("Date");
                    // final String name = names1.getText().toString();
                    final String clientname = names1.getText().toString();
                    if (spinner.getSelectedItem().equals("Sales")) {
                        gridProd = databaseH.getData(clientname, 1, true);
                        gridPrice = databaseH.getData(clientname, 2, true);
                        gridBill = databaseH.getData(clientname, 3, true);
                        customGridView1 = new CustomGridView(getActivity(), gridProd, gridPrice, gridBill, 1);
                        gridView.setAdapter(customGridView1);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position1, long id) {
                                final String bill = gridProd.get(position1).toString();
                                final Dialog dlg = new Dialog(getContext());
                                LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                if (balance == 0) {
                                    View v = li.inflate(R.layout.popupbill, null, false);
                                    dlg.setContentView(v);
                                    Button bt_dismiss = (Button) dlg.findViewById(R.id.dismiss1);
                                    Button bt_delete = (Button) dlg.findViewById(R.id.bt_deletebill);
                                    Button bt_addProd = (Button) dlg.findViewById(R.id.bt_addprod);


                                    bt_delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
                                            a_builder.setMessage("Are you sure, you want to delete the bill?").setCancelable(false)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            float amt = Float.parseFloat(databaseH.getBalance(clientname, 1));
                                                            amt = amt - Float.parseFloat(gridPrice.get(position1).toString());
                                                            if (databaseH.deleteBill(bill, true) && databaseH.deleteBill1(bill, true)) {
                                                                Toast.makeText(getContext(), "Bill removed from database",
                                                                        Toast.LENGTH_SHORT).show();
                                                                databaseH.addBalance(clientname, String.valueOf(amt), 1);
                                                                gridPrice.remove(position1);
                                                                gridBill.remove(position1);
                                                                gridProd.remove(position1);
                                                                customGridView1.notifyDataSetChanged();
                                                            }
                                                        }
                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            AlertDialog alert = a_builder.create();
                                            alert.setTitle("Delete Bill " + bill);
                                            alert.show();
                                            dlg.dismiss();
                                        }
                                    });

                                    bt_dismiss.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dlg.dismiss();
                                        }
                                    });
                                    final ListView listView = (ListView) dlg.findViewById(R.id.listViewbill);
                                    final ArrayList gridProd1, gridQuant, gridTax, gridAmt;
                                    if (spinner.getSelectedItem().equals("Sales")) {
                                        gridProd1 = databaseH.getProDetailsBill(gridProd.get(position1).toString(), 1, true);
                                        gridQuant = databaseH.getProDetailsBill(gridProd.get(position1).toString(), 2, true);
                                        gridTax = databaseH.getProDetailsBill(gridProd.get(position1).toString(), 3, true);
                                        gridAmt = databaseH.getProDetailsBill(gridProd.get(position1).toString(), 4, true);
                                        customGridView4 = new CustomGridView(getActivity(), gridProd1, gridQuant, gridTax, gridAmt, 6);
                                        listView.setAdapter(customGridView4);
                                        dlg.setTitle("Bill No:" + gridProd.get(position1).toString());
                                        dlg.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                                        bt_addProd.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final Dialog dlg1 = new Dialog(getContext());
                                                LayoutInflater layoutInflater
                                                        = (LayoutInflater) getActivity().getBaseContext()
                                                        .getSystemService(getContext().LAYOUT_INFLATER_SERVICE);

                                                View view1 = layoutInflater.inflate(R.layout.popupbillofbill1, null, false);
                                                dlg1.setContentView(view1);
                                                dlg1.setTitle("Add Product");
                                                dlg1.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                                dlg1.show();
                                                final Button bt_dismiss = (Button) view1.findViewById(R.id.bt_dismissBill1);
                                                final Button bt_Add = (Button) view1.findViewById(R.id.bt_pop_add1);


                                                product = (CustomAutoCompleteView) view1.findViewById(R.id.pop_product_det1);
                                                product.addTextChangedListener(new CustomAutoCompleteTextChangedListener(ThreeFragment.this, 10));
                                                myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, item1);
                                                product.setAdapter(myAdapter);

                                                qty = (EditText) view1.findViewById(R.id.pop_product_qty1);
                                                price = (EditText) view1.findViewById(R.id.pop_product_price1);
                                                tax = (EditText) view1.findViewById(R.id.pop_product_tax1);
                                                amount = (EditText) view1.findViewById(R.id.pop_product_amt1);
                                                price.addTextChangedListener(new GenericTextWatcher2(ThreeFragment.this, price));
                                                qty.addTextChangedListener(new GenericTextWatcher2(ThreeFragment.this, qty));
                                                tax.addTextChangedListener(new GenericTextWatcher2(ThreeFragment.this, tax));

                                                bt_Add.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (!product.getText().toString().equals("") && !amount.getText().toString().equals("")
                                                                && !qty.getText().toString().equals("") && !price.getText().toString().equals("")) {
                                                            if (!gridProd1.contains(product.getText().toString())) {
                                                                Float amt = Float.parseFloat(amount.getText().toString()) / Float.parseFloat(qty.getText().toString());
                                                                Float value = Float.parseFloat(gridPrice.get(position1).toString());
                                                                value = value + Float.parseFloat(amount.getText().toString());
                                                                Float balance = Float.parseFloat(databaseH.getBalance(clientname, 1));
                                                                balance = balance + Float.parseFloat(amount.getText().toString());
                                                                if (databaseH.addProdDetails(clientname, product.getText().toString(), String.valueOf(amt),
                                                                        qty.getText().toString(), tax.getText().toString(), bill, 1)
                                                                        && databaseH.updateProdBill(bill, String.valueOf(value), true)
                                                                        && databaseH.addBalance(clientname, String.valueOf(balance), 1)) {
                                                                    Toast.makeText(getContext(), "Product successfully added to bill", Toast.LENGTH_SHORT).show();
                                                                    gridProd1.add(product.getText().toString());
                                                                    gridAmt.add(String.valueOf(amt));
                                                                    gridQuant.add(qty.getText().toString());
                                                                    gridTax.add(tax.getText().toString());
                                                                    gridPrice.set(position1, String.valueOf(value));
                                                                    customGridView1.notifyDataSetChanged();
                                                                    customGridView4.notifyDataSetChanged();
                                                                    dlg1.dismiss();
                                                                }
                                                            } else {
                                                                Toast.makeText(getContext(), "Product Already exists! Please update from list", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(getContext(), "Few fields empty", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                bt_dismiss.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dlg1.dismiss();
                                                    }
                                                });
                                            }
                                        });
                                        Button bt_preview = (Button) dlg.findViewById(R.id.bt_previewBill);
                                        bt_preview.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Float totalamount = Float.valueOf(0);
                                                if (!clientname.equals("")) {
                                                    ArrayList<String> totalamt = new ArrayList<String>();
                                                    if (gridProd1.size() > 0) {
                                                        for (int i = 0; i < gridProd1.size(); i++) {
                                                            Float value = Float.parseFloat(gridAmt.get(i).toString()) * Float.parseFloat(gridQuant.get(i).toString());
                                                            if (!gridTax.get(i).toString().equals("")) {
                                                                value = value + (Float.valueOf(gridTax.get(i).toString()) * value) / 100;
                                                            }
                                                            totalamt.add(i, String.valueOf(value));
                                                        }
                                                    }
                                                    if (totalamt.size() > 0) {
                                                        for (int i = 0; i < totalamt.size(); i++) {
                                                            totalamount = totalamount + Float.parseFloat(totalamt.get(i).toString());
                                                        }
                                                    }
                                                    Double discount = ((totalamount - Double.parseDouble(gridPrice.get(position1).toString()))
                                                            / totalamount) * 100;
                                                    discount = round(discount, 0, BigDecimal.ROUND_HALF_UP);
                                                    final String paid = databaseH.getPaid(bill, true);
                                                    Intent intent = new Intent(getActivity(), PrintPreview.class);
                                                    intent.putExtra("name1", clientname);
                                                    intent.putExtra("netamount", gridPrice.get(position1).toString());
                                                    intent.putExtra("disco", String.valueOf(discount));
                                                    intent.putExtra("price", String.valueOf(totalamount));
                                                    intent.putExtra("address1", "");
                                                    intent.putExtra("paidamt", paid);
                                                    intent.putExtra("billNo", bill);
                                                    intent.putExtra("presentdate", gridBill.get(position1).toString());
                                                    intent.putStringArrayListExtra("listproduct1", gridProd1);
                                                    intent.putStringArrayListExtra("listprice1", gridAmt);
                                                    intent.putStringArrayListExtra("listquantity1", gridQuant);
                                                    intent.putStringArrayListExtra("listTotal1", totalamt);
                                                    startActivity(intent);

                                                } else {
                                                    Toast.makeText(getContext(), "Enter all the fields!!", Toast.LENGTH_SHORT).show();
                                                }


                                            }
                                        });


                                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                            @Override
                                            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                                LayoutInflater layoutInflater
                                                        = (LayoutInflater) getActivity().getBaseContext()
                                                        .getSystemService(getContext().LAYOUT_INFLATER_SERVICE);

                                                final View popupView = layoutInflater.inflate(R.layout.popupbillofbill, null);
                                                final PopupWindow popupWindow = new PopupWindow(
                                                        popupView,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                                popupWindow.setFocusable(true);
                                                popupWindow.update();
                                                popupWindow.showAsDropDown(listView, 110, -850);
                                                final Button bt_dismiss = (Button) popupView.findViewById(R.id.bt_dismissBill);
                                                Button bt_delete = (Button) popupView.findViewById(R.id.bt_pop_delete);
                                                Button bt_update = (Button) popupView.findViewById(R.id.bt_pop_update);
                                                final EditText product = (EditText) popupView.findViewById(R.id.pop_product_det);
                                                final EditText qty = (EditText) popupView.findViewById(R.id.pop_product_qty);
                                                final EditText amount = (EditText) popupView.findViewById(R.id.pop_product_amt);
                                                final String price1 = gridAmt.get(position).toString();
                                                final String product1 = gridProd1.get(position).toString();
                                                product.setText(product1);
                                                final String qty1 = gridQuant.get(position).toString();
                                                final String tax1 = gridTax.get(position).toString();
                                                qty.setText(qty1);
                                                final Float amt1 = Float.parseFloat(gridAmt.get(position).toString()) * Float.parseFloat(qty1);
                                                amount.setText(String.valueOf(amt1));
                                                bt_update.setOnClickListener(new View.OnClickListener() {
                                                                                 @Override
                                                                                 public void onClick(View v) {
                                                                                     if (!product.getText().toString().equals(product1) ||
                                                                                             qty.getText().toString().equals(qty1)) {
                                                                                         Toast.makeText(getContext(), "Product invalid to update",
                                                                                                 Toast.LENGTH_SHORT).show();
                                                                                     } else {
                                                                                         //String tax1 = tax.getText().toString();
                                                                                         String qty1 = qty.getText().toString();
                                                                                         Float value = Float.parseFloat(price1) * Float.parseFloat
                                                                                                 (qty1);
                                                                                         Float value1 = Float.valueOf(0);

                                                                                         if (value >= amt1) {
                                                                                             value1 = value - amt1;
                                                                                             value1 = Float.parseFloat(gridPrice.get(position1).toString())
                                                                                                     + value1;
                                                                                         } else {
                                                                                             value1 = amt1 - value;
                                                                                             value1 = Float.parseFloat(gridPrice.get(position1).toString())
                                                                                                     - value1;
                                                                                         }

                                                                                         amount.setText(String.valueOf(value));
                                                                                         if (databaseH.updateProdDetails(bill, product1,
                                                                                                 qty1, tax1,
                                                                                                 String.valueOf(value / Float.parseFloat(qty1)), true)
                                                                                                 && databaseH.updateProdBill(bill, String.valueOf(value1), true)
                                                                                                 ) {
                                                                                             gridAmt.set(position, String.valueOf(value / Float.parseFloat(qty1)));
                                                                                             gridQuant.set(position, qty1);
                                                                                             gridTax.set(position, tax1);
                                                                                             Toast.makeText(getContext(), "Product updated in bill",
                                                                                                     Toast.LENGTH_SHORT).show();
                                                                                             gridPrice.set(position1, String.valueOf(value1));
                                                                                             customGridView4.notifyDataSetChanged();
                                                                                             customGridView1.notifyDataSetChanged();
                                                                                             popupWindow.dismiss();
                                                                                         }
                                                                                     }
                                                                                 }
                                                                             }
                                                );
                                                bt_delete.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (!product.getText().toString().equals("") && product.getText().toString().equals(product1)) {
                                                            AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
                                                            a_builder.setMessage("Are you sure to delete this product from bill?").setCancelable(false)
                                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            Float balance = Float.parseFloat(databaseH.getBalance(clientname, 1));
                                                                            balance = balance - Float.parseFloat(amount.getText().toString());
                                                                            Float value = Float.parseFloat(gridPrice.get(position1).toString());
                                                                            value = value - Float.parseFloat(amount.getText().toString());
                                                                            if (databaseH.deleteProdBill(bill, gridProd1.get(position).toString(), true)
                                                                                    && databaseH.updateProdBill(bill, String.valueOf(value), true)) {
                                                                                Toast.makeText(getContext(), "Product removed from bill",
                                                                                        Toast.LENGTH_SHORT).show();
                                                                                if (balance < 0) {
                                                                                    databaseH.addBalance(clientname, String.valueOf(balance), 2);
                                                                                } else {
                                                                                    databaseH.addBalance(clientname, String.valueOf(balance), 1);
                                                                                }
                                                                                gridProd1.remove(position);
                                                                                gridAmt.remove(position);
                                                                                gridQuant.remove(position);
                                                                                gridTax.remove(position);
                                                                                gridPrice.set(position1, String.valueOf(value));
                                                                                customGridView1.notifyDataSetChanged();
                                                                                customGridView4.notifyDataSetChanged();
                                                                                popupWindow.dismiss();
                                                                            }

                                                                        }
                                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.cancel();
                                                                }
                                                            });
                                                            AlertDialog alert = a_builder.create();
                                                            alert.setTitle("Delete Bill " + bill);
                                                            alert.show();
                                                        } else {
                                                            Toast.makeText(getContext(), "Unable to delete product",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                bt_dismiss.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        popupWindow.dismiss();
                                                    }
                                                });
                                                return true;
                                            }
                                        });
                                        dlg.show();
                                    }
                                    if (balance == 1) {
                                        Calendar cal = Calendar.getInstance();
                                        Intent intent = new Intent(Intent.ACTION_EDIT);
                                        intent.setType("vnd.android.cursor.item/event");
                                        intent.putExtra("beginTime", cal.getTimeInMillis());
                                        intent.putExtra("allDay", false);
                                        intent.putExtra("rrule", "FREQ=DAILY");
                                        intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                                        intent.putExtra("title", gridName.get(position1).toString() +
                                                " Amount: " + gridBalance.get(position1).toString());
                                        startActivity(intent);
                                    }
                                }

                            }

                        });

                    } else if (spinner.getSelectedItem().equals("Purchase")) {
                        gridProd = databaseH.getData(names1.getText().toString(), 1, false);
                        gridPrice = databaseH.getData(names1.getText().toString(), 2, false);
                        gridBill = databaseH.getData(names1.getText().toString(), 3, false);
                        customGridView1 = new CustomGridView(getActivity(), gridProd, gridPrice, gridBill, 1);
                        gridView.setAdapter(customGridView1);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position1, long id) {
                                final String bill = gridProd.get(position1).toString();
                                final String total = gridPrice.get(position1).toString();
                                final Dialog dlg = new Dialog(getContext());
                                LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                if (balance == 0) {
                                    View v = li.inflate(R.layout.popupbill, null, false);
                                    dlg.setContentView(v);
                                    Button bt_dismiss = (Button) dlg.findViewById(R.id.dismiss1);
                                    Button bt_delete = (Button) dlg.findViewById(R.id.bt_deletebill);
                                    Button bt_addProd = (Button) dlg.findViewById(R.id.bt_addprod);

                                    bt_delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
                                            a_builder.setMessage("Are you sure, you want to delete the bill?").setCancelable(false)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            float amt = Float.parseFloat(databaseH.getBalance(clientname, 2));
                                                            amt = amt - Float.parseFloat(gridPrice.get(position1).toString());
                                                            if (databaseH.deleteBill(bill, false) && databaseH.deleteBill1(bill, false)) {
                                                                Toast.makeText(getContext(), "Bill removed from database",
                                                                        Toast.LENGTH_SHORT).show();
                                                                databaseH.addBalance(clientname, String.valueOf(amt), 2);
                                                                gridPrice.remove(position1);
                                                                gridBill.remove(position1);
                                                                gridProd.remove(position1);
                                                                customGridView1.notifyDataSetChanged();
                                                            }
                                                        }
                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            AlertDialog alert = a_builder.create();
                                            alert.setTitle("Delete Bill " + bill);
                                            alert.show();
                                            dlg.dismiss();
                                        }
                                    });

                                    bt_dismiss.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dlg.dismiss();
                                        }
                                    });
                                    final ListView listView = (ListView) dlg.findViewById(R.id.listViewbill);
                                    final ArrayList gridProd1, gridQuant, gridTax, gridAmt;
                                    if (spinner.getSelectedItem().equals("Purchase")) {
                                        gridProd1 = databaseH.getProDetailsBill(gridProd.get(position1).toString(), 1, false);
                                        gridQuant = databaseH.getProDetailsBill(gridProd.get(position1).toString(), 2, false);
                                        gridTax = databaseH.getProDetailsBill(gridProd.get(position1).toString(), 3, false);
                                        gridAmt = databaseH.getProDetailsBill(gridProd.get(position1).toString(), 4, false);
                                        customGridView4 = new CustomGridView(getActivity(), gridProd1, gridQuant, gridTax, gridAmt, 6);
                                        listView.setAdapter(customGridView4);
                                        dlg.setTitle("Bill No:" + gridProd.get(position1).toString());
                                        dlg.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                                        bt_addProd.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final Dialog dlg1 = new Dialog(getContext());
                                                LayoutInflater layoutInflater
                                                        = (LayoutInflater) getActivity().getBaseContext()
                                                        .getSystemService(getContext().LAYOUT_INFLATER_SERVICE);

                                                View view1 = layoutInflater.inflate(R.layout.popupbillofbill1, null, false);
                                                dlg1.setContentView(view1);
                                                dlg1.setTitle("Add Product");
                                                dlg1.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                                dlg1.show();
                                                final Button bt_dismiss = (Button) view1.findViewById(R.id.bt_dismissBill1);
                                                final Button bt_Add = (Button) view1.findViewById(R.id.bt_pop_add1);

                                                product = (CustomAutoCompleteView) view1.findViewById(R.id.pop_product_det1);
                                                product.addTextChangedListener(new CustomAutoCompleteTextChangedListener(ThreeFragment.this, 10));
                                                myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, item1);
                                                product.setAdapter(myAdapter);

                                                qty = (EditText) view1.findViewById(R.id.pop_product_qty1);
                                                price = (EditText) view1.findViewById(R.id.pop_product_price1);
                                                tax = (EditText) view1.findViewById(R.id.pop_product_tax1);
                                                amount = (EditText) view1.findViewById(R.id.pop_product_amt1);
                                                price.addTextChangedListener(new GenericTextWatcher2(ThreeFragment.this, price));
                                                qty.addTextChangedListener(new GenericTextWatcher2(ThreeFragment.this, qty));
                                                tax.addTextChangedListener(new GenericTextWatcher2(ThreeFragment.this, tax));
                                                bt_Add.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (!product.getText().toString().equals("") && !amount.getText().toString().equals("")
                                                                && !qty.getText().toString().equals("") && !price.getText().toString().equals("")) {
                                                            if (!gridProd1.contains(product.getText().toString())) {
                                                                Float amt = Float.parseFloat(amount.getText().toString()) / Float.parseFloat(qty.getText().toString());
                                                                Float value = Float.parseFloat(total);
                                                                Float balance = Float.parseFloat(databaseH.getBalance(clientname, 2));
                                                                balance = balance + Float.parseFloat(amount.getText().toString());
                                                                value = value + Float.parseFloat(amount.getText().toString());
                                                                if (databaseH.addProdDetails(clientname, product.getText().toString(), String.valueOf(amt),
                                                                        qty.getText().toString(), tax.getText().toString(), bill, 2)
                                                                        && databaseH.updateProdBill(bill, String.valueOf(value), false)
                                                                        && databaseH.addBalance(clientname, String.valueOf(balance), 2)) {
                                                                    Toast.makeText(getContext(), "Product successfully added to bill", Toast.LENGTH_SHORT).show();
                                                                    gridProd1.add(product.getText().toString());
                                                                    gridAmt.add(String.valueOf(amt));
                                                                    gridQuant.add(qty.getText().toString());
                                                                    gridTax.add(tax.getText().toString());
                                                                    gridPrice.set(position1, String.valueOf(value));
                                                                    customGridView1.notifyDataSetChanged();
                                                                    customGridView4.notifyDataSetChanged();
                                                                    dlg1.dismiss();
                                                                }
                                                            } else {
                                                                Toast.makeText(getContext(), "Product Already exists! Please update from list", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(getContext(), "Few fields empty", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                bt_dismiss.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dlg1.dismiss();
                                                    }
                                                });
                                            }
                                        });

                                        Button bt_preview = (Button) dlg.findViewById(R.id.bt_previewBill);
                                        bt_preview.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Float totalamount = Float.valueOf(0);
                                                if (!clientname.equals("")) {
                                                    ArrayList<String> totalamt = new ArrayList<String>();
                                                    if (gridProd1.size() > 0) {
                                                        for (int i = 0; i < gridProd1.size(); i++) {
                                                            Float value = Float.parseFloat(gridAmt.get(i).toString()) * Float.parseFloat(gridQuant.get(i).toString());
                                                            if (!gridTax.get(i).toString().equals("")) {
                                                                value = value + (Float.parseFloat(gridTax.get(i).toString()) * value) / 100;
                                                            }
                                                            totalamt.add(i, String.valueOf(value));
                                                        }
                                                    }
                                                    if (totalamt.size() > 0) {
                                                        for (int i = 0; i < totalamt.size(); i++) {
                                                            totalamount = totalamount + Float.parseFloat(totalamt.get(i).toString());
                                                        }
                                                    }

                                                    Double discount = ((totalamount - Double.parseDouble(gridPrice.get(position1).toString()))
                                                            / totalamount) * 100;
                                                    discount = round(discount, 0, BigDecimal.ROUND_HALF_UP);
                                                    final String paid = databaseH.getPaid(bill, false);
                                                    Intent intent = new Intent(getActivity(), PrintPreview.class);
                                                    intent.putExtra("name1", clientname);
                                                    intent.putExtra("netamount", gridPrice.get(position1).toString());
                                                    intent.putExtra("disco", String.valueOf(discount));
                                                    intent.putExtra("price", String.valueOf(totalamount));
                                                    intent.putExtra("address1", "");
                                                    intent.putExtra("paidamt", paid);
                                                    intent.putExtra("billNo", bill);
                                                    intent.putExtra("presentdate", gridBill.get(position1).toString());
                                                    intent.putStringArrayListExtra("listproduct1", gridProd1);
                                                    intent.putStringArrayListExtra("listprice1", gridAmt);
                                                    intent.putStringArrayListExtra("listquantity1", gridQuant);
                                                    intent.putStringArrayListExtra("listTotal1", totalamt);
                                                    startActivity(intent);

                                                } else {
                                                    Toast.makeText(getContext(), "Enter all the fields!!", Toast.LENGTH_SHORT).show();
                                                }


                                            }
                                        });

                                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                            @Override
                                            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                                LayoutInflater layoutInflater
                                                        = (LayoutInflater) getActivity().getBaseContext()
                                                        .getSystemService(getContext().LAYOUT_INFLATER_SERVICE);

                                                final View popupView = layoutInflater.inflate(R.layout.popupbillofbill, null);
                                                final PopupWindow popupWindow = new PopupWindow(
                                                        popupView,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                                popupWindow.setFocusable(true);
                                                popupWindow.update();
                                                popupWindow.showAsDropDown(listView, 110, -850);
                                                final Button bt_dismiss = (Button) popupView.findViewById(R.id.bt_dismissBill);
                                                Button bt_delete = (Button) popupView.findViewById(R.id.bt_pop_delete);
                                                Button bt_update = (Button) popupView.findViewById(R.id.bt_pop_update);
                                                final EditText product = (EditText) popupView.findViewById(R.id.pop_product_det);
                                                final EditText qty = (EditText) popupView.findViewById(R.id.pop_product_qty);
                                                final EditText amount = (EditText) popupView.findViewById(R.id.pop_product_amt);
                                                final String price1 = gridAmt.get(position).toString();
                                                final String product1 = gridProd1.get(position).toString();
                                                product.setText(product1);
                                                final String qty1 = gridQuant.get(position).toString();
                                                final String tax1 = gridTax.get(position).toString();
                                                qty.setText(qty1);
                                                final Float amt1 = Float.parseFloat(gridAmt.get(position).toString()) * Float.parseFloat(qty1);
                                                amount.setText(String.valueOf(amt1));
                                                bt_update.setOnClickListener(new View.OnClickListener() {
                                                                                 @Override
                                                                                 public void onClick(View v) {
                                                                                     if (!product.getText().toString().equals(product1) ||
                                                                                             qty.getText().toString().equals(qty1)) {
                                                                                         Toast.makeText(getContext(), "Product invalid to update",
                                                                                                 Toast.LENGTH_SHORT).show();
                                                                                     } else {
                                                                                         //String tax1 = tax.getText().toString();
                                                                                         String qty1 = qty.getText().toString();
                                                                                         Float value = Float.parseFloat(price1) * Float.parseFloat
                                                                                                 (qty1);
                                                                                         Float value1 = Float.valueOf(0);

                                                                                         if (value >= amt1) {
                                                                                             value1 = value - amt1;
                                                                                             value1 = Float.parseFloat(total) + value1;
                                                                                         } else {
                                                                                             value1 = amt1 - value;
                                                                                             value1 = Float.parseFloat(total) - value1;
                                                                                         }

                                                                                         amount.setText(String.valueOf(value));
                                                                                         if (databaseH.updateProdDetails(bill, product1,
                                                                                                 qty1, tax1,
                                                                                                 String.valueOf(value / Float.parseFloat(qty1)), false)
                                                                                                 && databaseH.updateProdBill(bill, String.valueOf(value1), false)
                                                                                                 ) {
                                                                                             gridAmt.set(position, String.valueOf(value / Float.parseFloat(qty1)));
                                                                                             gridQuant.set(position, qty1);
                                                                                             gridTax.set(position, tax1);
                                                                                             Toast.makeText(getContext(), "Product updated in bill",
                                                                                                     Toast.LENGTH_SHORT).show();
                                                                                             gridPrice.set(position1, String.valueOf(value1));
                                                                                             customGridView4.notifyDataSetChanged();
                                                                                             customGridView1.notifyDataSetChanged();
                                                                                             popupWindow.dismiss();
                                                                                         }
                                                                                     }
                                                                                 }
                                                                             }
                                                );
                                                bt_delete.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (!product.getText().toString().equals("") && product.getText().toString().equals(product1)) {
                                                            AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
                                                            a_builder.setMessage("Are you sure to delete this product from bill?").setCancelable(false)
                                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            Float balance = Float.parseFloat(databaseH.getBalance(clientname, 2));
                                                                            balance = balance - Float.parseFloat(amount.getText().toString());

                                                                            Float value = Float.parseFloat(total);
                                                                            value = value - Float.parseFloat(amount.getText().toString());
                                                                            if (databaseH.deleteProdBill(bill, gridProd1.get(position).toString(), false)
                                                                                    && databaseH.updateProdBill(bill, String.valueOf(value), false)) {
                                                                                if (balance < 0) {
                                                                                    databaseH.addBalance(clientname, String.valueOf(balance), 1);
                                                                                } else {
                                                                                    databaseH.addBalance(clientname, String.valueOf(balance), 2);
                                                                                }
                                                                                Toast.makeText(getContext(), "Product removed from bill",
                                                                                        Toast.LENGTH_SHORT).show();
                                                                                gridProd1.remove(position);
                                                                                gridAmt.remove(position);
                                                                                gridQuant.remove(position);
                                                                                gridTax.remove(position);
                                                                                gridPrice.set(position1, String.valueOf(value));
                                                                                customGridView1.notifyDataSetChanged();
                                                                                customGridView4.notifyDataSetChanged();
                                                                            }

                                                                        }
                                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.cancel();
                                                                }
                                                            });
                                                            AlertDialog alert = a_builder.create();
                                                            alert.setTitle("Delete Bill " + bill);
                                                            alert.show();
                                                            dlg.dismiss();
                                                        } else {
                                                            Toast.makeText(getContext(), "Unable to delete product",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                bt_dismiss.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        popupWindow.dismiss();
                                                    }
                                                });
                                                return true;
                                            }
                                        });
                                        dlg.show();
                                    }
                                    if (balance == 1) {
                                        Calendar cal = Calendar.getInstance();
                                        Intent intent = new Intent(Intent.ACTION_EDIT);
                                        intent.setType("vnd.android.cursor.item/event");
                                        intent.putExtra("beginTime", cal.getTimeInMillis());
                                        intent.putExtra("allDay", false);
                                        intent.putExtra("rrule", "FREQ=DAILY");
                                        intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                                        intent.putExtra("title", gridName.get(position1).toString() +
                                                " Amount: " + gridBalance.get(position1).toString());
                                        startActivity(intent);
                                    }
                                }

                            }

                        });

                    }
                } else if (!names1.getText().toString().equals("") && !date1.getText().toString().equals("")
                        && !date2.getText().toString().equals("")) {
                    abcde.setText("Product");
                    abcde1.setText("Price");
                    abcde2.setText("BillNo");
                    gridProd = databaseH.getDatawithinDates(names1.getText().toString(), date1.getText().toString(),
                            date2.getText().toString(), 1);
                    gridPrice = databaseH.getDatawithinDates(names1.getText().toString(), date1.getText().toString(),
                            date2.getText().toString(), 2);
                    gridBill = databaseH.getDatawithinDates(names1.getText().toString(), date1.getText().toString(),
                            date2.getText().toString(), 3);
                    customGridView = new CustomGridView(getActivity(), gridProd, gridPrice, gridBill, 1);
                    gridView.setAdapter(customGridView);
                    date1.setText("");
                    date2.setText("");

                    //if search prod has some value disable date fields and if date fields selected disable product field
                } else if (names1.getText().toString().equals("") && !date1.getText().toString().equals("")
                        && !date2.getText().toString().equals("")) {
                    gridProd = databaseH.getDatawithinDates1(date1.getText().toString(),
                            date2.getText().toString(), 1);
                    gridPrice = databaseH.getDatawithinDates1(date1.getText().toString(),
                            date2.getText().toString(), 2);
                    gridBill = databaseH.getDatawithinDates1(date1.getText().toString(),
                            date2.getText().toString(), 3);
                    abcde.setText("Name");
                    abcde1.setText("Product");
                    abcde2.setText("Price");
                    customGridView = new CustomGridView(getActivity(), gridBill, gridProd, gridPrice, 1);
                    gridView.setAdapter(customGridView);
                    date1.setText("");
                    date2.setText("");
                } else {
                    Toast.makeText(getContext(), "Enter name to search", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static double round(double unrounded, int precision, int roundingMode)
    {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }
    public String[] getItemsFromDb(String searchTerm, int id) {

        // add items on the array dynamically
        if (id == 1) {
            List<String> products = databaseH.read1(searchTerm, 1);
            int rowCount = products.size();
            String[] item1 = new String[rowCount];
            int x = 0;

            for (String record : products) {

                item1[x] = record;
                x++;
            }
            return item1;
        } else if (id == 2) {
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
        return null;
    }


    @Override
    public void onClick(View view) {
        if (view == date1) {
            datePicker1.show();
        }
        else if(view == date2)
        {
            datePicker2.show();
        }
    }
}
