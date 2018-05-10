package com.ltapps.textscanner;

/**
 * Created by Guy on 06/05/2018.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class PaymentActivity2 extends Activity {
    private Toolbar toolbar;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader=new ArrayList<String>();;
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    private double tip;
    private List<User> users;
    private  HashMap<Item,Integer> remainMap;
    private Button tipButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ViewCompat.setElevation(toolbar,10);
        tip=1;
        final Dialog dialog = new Dialog(this);
        addTip(dialog);
        users = (ArrayList<User>) getIntent().getSerializableExtra("Users");
        remainMap= (HashMap<Item,Integer>)getIntent().getSerializableExtra("remainItems");


        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        setUserItems();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }


    private void setUserHeaders()
    {
        String userName;
        for(User usr:users)
        {
            userName=usr.getName();
            listDataHeader.add(userName);
        }
    }

    private void setUserItems()
    {
        String userName;
        List<String> items=null;
        HashMap<Item,Integer> userItempsMap;
        HashMap<Integer,SharedWraper> userSherdItemsWraper;
        HashMap<Item,Double> userSharedItems;
        int i=0;
        for(User usr:users)
        {
            items=getNewList();
            userName=usr.getName();
            listDataHeader.add(userName);
            userItempsMap=usr.getItems();
            userSherdItemsWraper=usr.getSharedItems();

            SharedWraper tmpShare;
            for (Map.Entry<Item,Integer> entry : userItempsMap.entrySet())
            {
                if(entry.getValue()>0)
                     items.add(entry.getKey().getName());
            }
            for (Map.Entry<Integer,SharedWraper> entry : userSherdItemsWraper.entrySet())
            {
                tmpShare=entry.getValue();
                userSharedItems=tmpShare.getItems();
                for (Map.Entry<Item,Double> entry2 : userSharedItems.entrySet()) {
                    if (entry2.getValue() > 0)
                        items.add(entry2.getKey().getName());
                }
            }
            listDataChild.put(listDataHeader.get(i),items);
            i++;
        }
        items=getNewList();
        for (Map.Entry<Item,Integer> entry : remainMap.entrySet())
        {
            if(entry.getValue()>0)
                items.add(entry.getKey().getName());
        }
        if(items!=null && items.size()>0)
        {
            listDataHeader.add("Remain Items");
            listDataChild.put("Remain Items",items);
        }
    }
    private List<String> getNewList()
    {
        return new ArrayList<String>();
    }

    private void addTip( final Dialog dialog)
    {
        tipButton = (Button) findViewById(R.id.tipbutton);
// add button listener
        tipButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog

                dialog.setContentView(R.layout.tipdialog);
                setSeekBar(dialog);
                dialog.show();
            }
        });
    }

    private void setSeekBar(final Dialog dialog)
    {
        TextView textView=(TextView) dialog.findViewById(R.id.textView1);

        SeekBar seekBar=(SeekBar) dialog.findViewById(R.id.seekBar1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
//                progress = progresValue;
//                Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                TextView textView=(TextView) dialog.findViewById(R.id.textView1);
//                textView.setText("Covered: " + progress + "/" + seekBar.getMax());
//                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });
    }




}

//android:paddingLeft="?android:attr/expandableListPreferredItemPaddingLeft"