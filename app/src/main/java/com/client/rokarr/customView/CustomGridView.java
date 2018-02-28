package com.client.rokarr.customView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.client.rokarr.R;
import com.client.rokarr.fragments.FourFragment;

import java.util.ArrayList;

/**
 * Created by Varun on 20-11-2015.
 */
public class CustomGridView extends BaseAdapter {

    private Context context;
    View popupView;
    int count = 0;
    private int id;
    LinearLayout linearLayout;
    Fragment fragment;
    private ArrayList prodGrid,priceGrid,billGrid, amtGrid,quantGrid;
    LayoutInflater inflater ;
    public CustomGridView(Context c, ArrayList namesGrid, ArrayList balanceGrid, int id)
    {
        this.context = c;
        this.prodGrid = namesGrid;
        this.priceGrid = balanceGrid;
        this.id= id;
    }
    public CustomGridView(Context c, ArrayList prodGrid, ArrayList priceGrid, ArrayList dateGrid, int id)
    {
        this.context = c;
        this.prodGrid = prodGrid;
        this.priceGrid = priceGrid;
        this.billGrid= dateGrid;
        this.id = id;
    }
    public CustomGridView(Context c, ArrayList prodGrid, ArrayList priceGrid, int id, FourFragment fragment)
    {
        this.context = c;
        this.prodGrid = prodGrid;
        this.priceGrid = priceGrid;
        this.id = id;
        this.fragment = fragment;
    }
    public CustomGridView(Context c, ArrayList prodGrid, ArrayList priceGrid, ArrayList quantGrid, ArrayList amtGrid, int id)
    {
        this.context = c;
        this.prodGrid = prodGrid;
        this.priceGrid = priceGrid;
        this.amtGrid = amtGrid;
        this.quantGrid = quantGrid;
        this.id = id;
    }
    @Override
    public int getCount() {
        return prodGrid.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class Holder
    {
        TextView productGrid,priceGrid,billgrid;
    }
    public class Holder1
    {
        TextView productGrid1, priceGrid1, qtyGrid,totalGrid;
    }
    public class Holder2
    {
        TextView  productGrid2, priceGrid2;
    }
    public class Holder3
    {
        TextView  productGrid3, priceGrid3;
    }
    public class Holder4
    {
        TextView productGrid4, priceGrid4;
    }
    public class Holder5
    {
        TextView productGrid5, qtyGrid5, taxGrid5, amtGrid5;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            if (id == 1) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.gridlayout, parent, false);

            }
            else if(id == 2)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.printgridlayout,parent,false);
            }
            else if(id == 3)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.balancesheet_layout,parent,false);
            }
            else if(id == 4)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.balancesheet_layout1,parent,false);
            }
            else if(id == 5)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.gridlayout, parent, false);
            }
            else if (id == 6)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.product_bill_details, parent, false);
            }
        }
        if(id == 1) {
            final Holder holder = new Holder();
            holder.billgrid = (TextView) convertView.findViewById(R.id.gridBill);
            holder.priceGrid = (TextView) convertView.findViewById(R.id.gridPrice);
            holder.productGrid = (TextView) convertView.findViewById(R.id.gridProduct);
            if (priceGrid.size() > 0 && prodGrid.size() > 0 && billGrid.size() > 0) {

                holder.productGrid.setText(prodGrid.get(position).toString());
                holder.priceGrid.setText(priceGrid.get(position).toString());
                holder.billgrid.setText(billGrid.get(position).toString());
            }
        }
        else if (id == 2)
        {
            final Holder1 holder1 = new Holder1();
            holder1.productGrid1 = (TextView) convertView.findViewById(R.id.gridPro);
            holder1.priceGrid1 = (TextView) convertView.findViewById(R.id.gridPri);
            holder1.qtyGrid = (TextView) convertView.findViewById(R.id.gridQuan);
            holder1.totalGrid = (TextView) convertView.findViewById(R.id.gridAmtt);
            if (priceGrid.size() > 0 && prodGrid.size() > 0 && quantGrid.size() > 0 && amtGrid.size() > 0) {
                holder1.productGrid1.setText(prodGrid.get(position).toString());
                holder1.priceGrid1.setText(priceGrid.get(position).toString());
                holder1.qtyGrid.setText(quantGrid.get(position).toString());
                holder1.totalGrid.setText(amtGrid.get(position).toString());
            }
        }
        else if (id == 3)
        {
            final FourFragment mainFragment = ((FourFragment) fragment);
            final Holder2 holder2 = new Holder2();
            holder2.priceGrid2 = (TextView) convertView.findViewById(R.id.receipt_amount);
            holder2.productGrid2 = (TextView) convertView.findViewById(R.id.receipt_product);
            if(priceGrid.size()>0 && prodGrid.size() > 0)
            {
                holder2.productGrid2.setText(prodGrid.get(position).toString());
                holder2.priceGrid2.setText(priceGrid.get(position).toString());
                if(position == 0)
                {
                    mainFragment.receipttotal.setText(holder2.priceGrid2.getText().toString());
                }
                else if (position >0 && !mainFragment.receipttotal.getText().toString().equals("")) {
                    Float value = Float.parseFloat(mainFragment.receipttotal.getText().toString()) +
                            Float.parseFloat(holder2.priceGrid2.getText().toString());
                    mainFragment.receipttotal.setText(String.valueOf(value));
                }
            }
            linearLayout = (LinearLayout) convertView.findViewById(R.id.balancelayout);
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Float val = Float.parseFloat(priceGrid.get(position).toString());
                    val = Float.parseFloat(mainFragment.receipttotal.getText().toString()) - val;
                    mainFragment.receipttotal.setText(String.valueOf(val));
                    priceGrid.remove(position);
                    prodGrid.remove(position);
                    notifyDataSetChanged();
                    return true;
                }
            });
        }
        else if (id == 4)
        {
            final FourFragment mainFragment = ((FourFragment) fragment);
            final Holder3 holder3 = new Holder3();
            holder3.priceGrid3 = (TextView) convertView.findViewById(R.id.receipt_amount1);
            holder3.productGrid3 = (TextView) convertView.findViewById(R.id.receipt_product1);
            if(priceGrid.size()>0 && prodGrid.size() > 0)
            {
                holder3.productGrid3.setText(prodGrid.get(position).toString());
                holder3.priceGrid3.setText(priceGrid.get(position).toString());
                if(position == 0)
                {
                    mainFragment.paymenttotal.setText(holder3.priceGrid3.getText().toString());
                }
                else if (position > 0) {
                    Float value  = Float.parseFloat(mainFragment.paymenttotal.getText().toString()) + Float.parseFloat(holder3.priceGrid3.getText().toString());
                    mainFragment.paymenttotal.setText(String.valueOf(value));
                }
            }

            linearLayout = (LinearLayout) convertView.findViewById(R.id.balancelayout1);
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Float val = Float.parseFloat(priceGrid.get(position).toString());
                    val = Float.parseFloat(mainFragment.paymenttotal.getText().toString()) - val;
                    mainFragment.paymenttotal.setText(String.valueOf(val));
                    priceGrid.remove(position);
                    prodGrid.remove(position);
                    notifyDataSetChanged();
                    return true;
                }
            });
        }
        else if(id == 5)
        {
            final Holder4 holder = new Holder4();
            holder.productGrid4 = (TextView) convertView.findViewById(R.id.gridProduct);
            holder.priceGrid4 = (TextView) convertView.findViewById(R.id.gridBill);
            if (prodGrid.size() > 0 && priceGrid.size() > 0) {
                holder.productGrid4.setText(prodGrid.get(position).toString());
                holder.priceGrid4.setText(priceGrid.get(position).toString());
            }
        }
        else if (id == 6)
        {
            final Holder5 holder = new Holder5();
            holder.productGrid5 = (TextView) convertView.findViewById(R.id.product_bill);
            holder.qtyGrid5 = (TextView) convertView.findViewById(R.id.product_qty);
            holder.taxGrid5 = (TextView) convertView.findViewById(R.id.product_amt);
            holder.amtGrid5 = (TextView) convertView.findViewById(R.id.product_tax);
            if (prodGrid.size() > 0 && priceGrid.size() > 0) {
                holder.productGrid5.setText(prodGrid.get(position).toString());
                holder.qtyGrid5.setText(priceGrid.get(position).toString());
                holder.taxGrid5.setText(String.valueOf(Float.parseFloat(amtGrid.get(position).toString()) * Float.parseFloat(priceGrid.get(position).toString())));
                holder.amtGrid5.setText(quantGrid.get(position).toString());
            }
        }
        return convertView;
    }
}
