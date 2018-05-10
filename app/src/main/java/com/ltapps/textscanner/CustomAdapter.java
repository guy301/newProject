package com.ltapps.textscanner;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Guy on 05/05/2018.
 */


import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<Item> items;
    int quantity[];
    LayoutInflater inflter;
    Map<Button,TextView> plusList=new HashMap<Button,TextView>();
    PickingItemsActivity p;

    public CustomAdapter(Context applicationContext, List<Item> items, int[] quantity , PickingItemsActivity p) {
        this.context = context;
        this.items = items;
        this.quantity = quantity;
        inflter = (LayoutInflater.from(applicationContext));
        this.p=p;


    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.row, null);
        Button itemNameLayout = (Button) view.findViewById(R.id.item_name);
        TextView itemQuantityLayout = (TextView) view.findViewById(R.id.itemQuantity);
        itemQuantityLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Item itm=items.get(i);
        String itemName=itm.getName();
        itemNameLayout.setText(itemName);
        int itemQuantity=quantity[i];
        itemQuantityLayout.setText(String.valueOf(itemQuantity));
        Button plus=(Button)view.findViewById(R.id.plus);
        plus.setTag(itemQuantityLayout);
        Button minus=(Button)view.findViewById(R.id.minus);
        minus.setTag(itemQuantityLayout);
        setOnClickPlus(plus,itm);
        setOnClickMinus(minus,itm);
       // itemNameLayout.setGravity(Gravit);
        AlertDialog dialog=p.getDialog(itm.getPrice());
        setOnClickItems(itemNameLayout,itm,dialog);

        return view;
    }
    private void setOnClickPlus(final Button btn,final Item itm){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView itemQuantityLayout=(TextView)btn.getTag();
                String itemQuantityString= (String) itemQuantityLayout.getText();
                User activeUser=p.getActiveUser();
                if(activeUser!=null) {
                    Map<Item,Integer> appMap=p.getAppItemsMap();
                    int mapItempQuantity=appMap.get(itm);
                    if(mapItempQuantity>0)
                    {
                        int itemQuantity = Integer.parseInt(itemQuantityString) + 1;
                        itemQuantityLayout.setText(String.valueOf(itemQuantity));
                        appMap.put(itm,mapItempQuantity-1);
                        int userQuantity=activeUser.getQuantityOfItem(itm);
                        activeUser.updateItem(itm,userQuantity+1);
                    }
                }
            }
        });
    }

    private void setOnClickMinus(final Button btn,final Item itm){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView itemQuantityLayout=(TextView)btn.getTag();
                String itemQuantityString= (String) itemQuantityLayout.getText();
                User activeUser=p.getActiveUser();
                if(activeUser!=null) {
                    Map<Item,Integer> appMap=p.getAppItemsMap();
                    int mapItempQuantity=appMap.get(itm);
                    int userQuantity=activeUser.getQuantityOfItem(itm);
                    if(userQuantity>0)
                    {
                        userQuantity--;
                        itemQuantityLayout.setText(String.valueOf(userQuantity));
                        appMap.put(itm,mapItempQuantity+1);

                        activeUser.updateItem(itm,userQuantity);
                    }
                }
            }
        });
    }


    private void setOnClickItems(Button btn,final Item itm,final AlertDialog dialog ){

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    public Map<Button,TextView> getPlusList()
    {
        return this.plusList;
    }
}