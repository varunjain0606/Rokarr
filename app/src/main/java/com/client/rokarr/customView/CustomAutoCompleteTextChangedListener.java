package com.client.rokarr.customView;

/**
 * Created by Varun on 13-11-2015.
 */
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

import com.client.rokarr.AddProducts;
import com.client.rokarr.fragments.OneFragment;
import com.client.rokarr.fragments.ThreeFragment;
import com.client.rokarr.fragments.TwoFragment;
import com.client.rokarr.loginandregistration.helper.DatabaseHandler;

public class CustomAutoCompleteTextChangedListener implements TextWatcher{

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;
    Fragment fragment;
    int id=0;

    public CustomAutoCompleteTextChangedListener(OneFragment fragment, int flag){
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.id = flag;
    }


    public CustomAutoCompleteTextChangedListener(TwoFragment fragment, int flag){
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.id = flag;
    }

    public CustomAutoCompleteTextChangedListener(Context c, int id)
    {
        this.context = c;
        this.id = id;
    }
    public CustomAutoCompleteTextChangedListener(ThreeFragment fragment, int flag)
    {
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.id = flag;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        // update the adapater
        if(id == 1) {
            OneFragment mainFragment = ((OneFragment) fragment);
            mainFragment.item1 = mainFragment.getItemsFromDb(userInput.toString(),1);
            mainFragment.myAdapter1.notifyDataSetChanged();
            mainFragment.myAdapter1 = new ArrayAdapter<String>(mainFragment.getActivity(), android.R.layout.simple_dropdown_item_1line, mainFragment.item1);
            mainFragment.product.setAdapter(mainFragment.myAdapter1);
        }
        else if(id==2)
        {
            DatabaseHandler databaseHandler = new DatabaseHandler(context);

            OneFragment mainFragment = ((OneFragment) fragment);
            mainFragment.item2 = mainFragment.getItemsFromDb(userInput.toString(),2);
            String name = userInput.toString();
            name = name.replace("'","");
            if(true == databaseHandler.checkIfExists(name,5))
            {
                mainFragment.balance.setText(databaseHandler.getBalance(userInput.toString(),1));
            }
            mainFragment.myAdapter2.notifyDataSetChanged();
            mainFragment.myAdapter2 = new ArrayAdapter<String>(mainFragment.getActivity(), android.R.layout.simple_dropdown_item_1line, mainFragment.item2);
            mainFragment.names.setAdapter(mainFragment.myAdapter2);
        }
        else if(id==3)
        {
            OneFragment mainFragment = ((OneFragment) fragment);
            mainFragment.item3 = mainFragment.getItemsFromDb(userInput.toString(),3);
            mainFragment.myAdapter3.notifyDataSetChanged();
            mainFragment.myAdapter3 = new ArrayAdapter<String>(mainFragment.getActivity(), android.R.layout.simple_dropdown_item_1line, mainFragment.item3);
            mainFragment.address.setAdapter(mainFragment.myAdapter3);
        }
        else if(id == 4)
        {
            AddProducts addProducts =  (AddProducts) context;
            addProducts.item1 = addProducts.getItemsFromDb(userInput.toString());
            addProducts.myAdapter1.notifyDataSetChanged();
            addProducts.myAdapter1 = new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,addProducts.item1);
            addProducts.product.setAdapter(addProducts.myAdapter1);
        }

        else if (id == 5)
        {
            ThreeFragment threeFragment = (ThreeFragment) fragment;
            threeFragment.item1 = threeFragment.getItemsFromDb(userInput.toString(),1);
            threeFragment.myAdapter1.notifyDataSetChanged();
            threeFragment.myAdapter1 = new ArrayAdapter<String>(threeFragment.getActivity(),android.R.layout.simple_dropdown_item_1line,threeFragment.item1);
            threeFragment.product1.setAdapter(threeFragment.myAdapter1);
        }

        else if(id == 6)
        {
            ThreeFragment threeFragment = (ThreeFragment) fragment;
            threeFragment.item2 = threeFragment.getItemsFromDb(userInput.toString(),2);
            threeFragment.myAdapter2.notifyDataSetChanged();
            threeFragment.myAdapter2 = new ArrayAdapter<String>(threeFragment.getActivity(),android.R.layout.simple_dropdown_item_1line,threeFragment.item2);
            threeFragment.names1.setAdapter(threeFragment.myAdapter2);
        }

        else if (id == 7)
        {
            TwoFragment mainFragment = ((TwoFragment) fragment);
            mainFragment.item1 = mainFragment.getItemsFromDb(userInput.toString(),1);
            mainFragment.myAdapter1.notifyDataSetChanged();
            mainFragment.myAdapter1 = new ArrayAdapter<String>(mainFragment.getActivity(), android.R.layout.simple_dropdown_item_1line, mainFragment.item1);
            mainFragment.product.setAdapter(mainFragment.myAdapter1);
        }
        else if (id == 8)
        {
            DatabaseHandler databaseHandler = new DatabaseHandler(context);

            TwoFragment mainFragment = ((TwoFragment) fragment);
            mainFragment.item2 = mainFragment.getItemsFromDb(userInput.toString(),2);
            String name = userInput.toString();
            name = name.replace("'","");
            if(true == databaseHandler.checkIfExists(name,8))
            {
                mainFragment.balance.setText(databaseHandler.getBalance(userInput.toString(),2));
            }
            mainFragment.myAdapter2.notifyDataSetChanged();
            mainFragment.myAdapter2 = new ArrayAdapter<String>(mainFragment.getActivity(), android.R.layout.simple_dropdown_item_1line, mainFragment.item2);
            mainFragment.names.setAdapter(mainFragment.myAdapter2);
        }
        else if (id == 9)
        {
            TwoFragment mainFragment = ((TwoFragment) fragment);
            mainFragment.item3 = mainFragment.getItemsFromDb(userInput.toString(),3);
            mainFragment.myAdapter3.notifyDataSetChanged();
            mainFragment.myAdapter3 = new ArrayAdapter<String>(mainFragment.getActivity(), android.R.layout.simple_dropdown_item_1line, mainFragment.item3);
            mainFragment.address.setAdapter(mainFragment.myAdapter3);
        }

        else if (id == 10)
        {
            ThreeFragment threeFragment = (ThreeFragment) fragment;
            threeFragment.item = threeFragment.getItemsFromDb(userInput.toString(),1);
            threeFragment.myAdapter.notifyDataSetChanged();
            threeFragment.myAdapter = new ArrayAdapter<String>(threeFragment.getActivity(),android.R.layout.simple_dropdown_item_1line,threeFragment.item);
            threeFragment.product.setAdapter(threeFragment.myAdapter);
        }

    }

}
