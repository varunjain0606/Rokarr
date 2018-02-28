package com.client.rokarr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Varun on 17-11-2015.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    Float val = Float.valueOf(0);
    LayoutInflater inflater;
    ArrayList product, quantity, prodprice, tax,total;
    String items = "abcdefgh";
    public ImageAdapter(Context c, ArrayList product,ArrayList quantity, ArrayList prodPrice, ArrayList tax, ArrayList total)
    {
        this.mContext = c;
        this.product = product;
        this.quantity = quantity;
        this.prodprice = prodPrice;
        this.tax = tax;
        this.total = total;
    }
    @Override
    public int getCount() {
        return product.size();
    }

    @Override
    public Object getItem(int position) {
        return items.charAt(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder
    {
        TextView product,price,quantity,tax,total;
        Button delete;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview,null);
        }
        //Our viewholder object
        final ViewHolder holder = new ViewHolder();
        final AddProducts addProducts = (AddProducts) mContext;
        holder.product = (TextView) convertView.findViewById(R.id.tb_prodlist);
        holder.price = (TextView) convertView.findViewById(R.id.tb_totallist);
        holder.quantity = (TextView) convertView.findViewById(R.id.tb_quantlist);
        holder.tax = (TextView) convertView.findViewById(R.id.tb_taxlist);
        holder.total = (TextView) convertView.findViewById(R.id.totallist);
        holder.delete = (Button)convertView.findViewById(R.id.bt_delete);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float val = Float.parseFloat(prodprice.get(position).toString())*Float.parseFloat(quantity.get(position).toString());
                if(tax.get(position).toString().equals("")) {
                    val = Float.parseFloat(addProducts.total.getText().toString()) - val;
                }
                else
                {
                    val = val + (Float.parseFloat(tax.get(position).toString())*val)/100;
                }
                if(val>0)
                {
                    addProducts.total.setText(String.valueOf(val));
                }
                if(position == 0)
                {
                    addProducts.total.setText("");
                }
                product.remove(position);
                prodprice.remove(position);
                quantity.remove(position);
                tax.remove(position);
                total.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.product.setText((product.get(position)).toString());
        holder.quantity.setText(quantity.get(position).toString());
        holder.price.setText(prodprice.get(position).toString());
        holder.tax.setText(tax.get(position).toString());
        holder.total.setText(total.get(position).toString());
/*
        Float value = Float.parseFloat(prodprice.get(position).toString())*Float.parseFloat(quantity.get(position).toString());
        if(!tax.get(position).toString().equals(""))
        value = value + (Float.parseFloat(tax.get(position).toString())*value)/100;
        total.add(String.valueOf(value));
        holder.total.setText(String.valueOf(value));

*/
        if (prodprice.size() > 0)
        {

            Float val = Float.valueOf(0);
            for(int i=0; i < total.size() ; i++)
            {
                val = val + Float.parseFloat(total.get(i).toString());
            }
            addProducts.total.setText(String.valueOf(val));
        }
        else
        {
            addProducts.total.setText("");
        }

        return convertView;

    }
}
