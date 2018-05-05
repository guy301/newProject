package com.ltapps.textscanner;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class settingItemsActivity extends AppCompatActivity {
    private Button go_back_b, Continue, addFriends;
    private String text;
    private HashMap<Item, Integer> AllItems = new HashMap<Item, Integer>();
    private ArrayList<User> AllUsers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_items);
        go_back_b = (Button) findViewById(R.id.goBack_b);
        addFriends = (Button) findViewById(R.id.add_friends);
        Continue = (Button) findViewById(R.id.Continue);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueToPicking();
            }
        });
        text = getIntent().getStringExtra("OCR_text");
        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriends();
            }
        });
        addButtons(text);

    }

    public void goBack(View view){
        finish();
    }

    public void addFriends(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How many friends are you?");
        final EditText friends_qntty = new EditText(this);
        friends_qntty.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        builder.setView(friends_qntty);
        builder.setNegativeButton("cancel",null);
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EnterFriendsName(friends_qntty.getText().toString());
            }
        });
        builder.show();
    }


    public void EnterFriendsName(String numOfFriends) {
        Integer friends_qntty = Integer.parseInt(numOfFriends);
        String FN = "\0";
        for (int i = 0; i < friends_qntty ; i++){
            FN += Integer.toString(i+1) + "\n";
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Would you like to edit your friends' names?");
        final TextView friends = new EditText(this);
        friends.setMaxLines(friends_qntty);
        friends.setPadding(3,3,3,3);
        friends.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        friends.setSingleLine(false);
        friends.setText(FN);
        friends.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        builder.setView(friends);
        builder.setNegativeButton("cancel",null);
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EnterUsersButtons(friends.getText().toString());
            }
        });
        builder.show();

    }

    /*******************************************************************************/
    private void EnterUsersButtons(String s) {
        if (s.isEmpty())
            return;
        final String[] words = s.split("\\s+");
        int i=0;
        for (String name : words){
            User curr_user = new User(name, copyMap(AllItems));
            AllUsers.add(curr_user);
            final Button yourButton = new Button(this);
            yourButton.setText(name);
            LinearLayout lm = (LinearLayout) findViewById(R.id.items);
            lm.addView(yourButton );
        }
    }


    public void addButtons(String s)
    {
        processText(s);
        if (AllItems.isEmpty()) {
            return;
        }
        int i=0;
        for (Map.Entry<Item, Integer> entry : AllItems.entrySet()){
            final String b_text = entry.getKey().getName() + "\n" + entry.getKey().getPrice() + "\n"
                    + entry.getValue();
            final Button yourButton = new Button(this);
            yourButton.setText(b_text);
            yourButton.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            LinearLayout lm = (LinearLayout) findViewById(R.id.users);
            lm.addView(yourButton );
            yourButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    showDialog(b_text, yourButton);
                    return true;
                }
            });
        }
    }

    private void showDialog(final String str, final Button btn) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("input text");
        final EditText edit = new EditText(this);
        edit.setText(str);
        builder.setView(edit);
        builder.setNegativeButton("cancel",null);
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btn.setText(edit.getText().toString());
                updateItem(str, edit.getText().toString());
            }
        });
        builder.show();
    }
    /**updating an item in the AllItems map in case the user changed it's details*/
    public void updateItem(String old_details, String new_details){
        final String[] details = old_details.split("\\s+");
        final String[] new_det = new_details.split("\\s+");
        for (Map.Entry<Item, Integer> entry : AllItems.entrySet()){
            if (entry.getKey().getName().equals(details[0])){
                int new_price = (int)Double.parseDouble(new_det[1]);
                Item updated = new Item(new_det[0], new_price);
                AllItems.remove(entry.getKey());
                AllItems.put(updated, Integer.parseInt(new_det[2]));
                return;
            }
        }
    }

    public void processText(String s){
        final String[] lines = s.split("\\n");
        String name = "";
        int amount_double = 0;
        double price_double = 0.0;
        /**3 col version: quantity-name-price*/
        int col_count = 0;
        int lines_num = lines.length;
        for (String line : lines) {
            String[] words = line.split("\\s");
            int words_num = words.length;
            for (String word : words){
                if(col_count == 0) {
                    if (!isNumeric(word)) {//OCR wrong, should be quantity
                        popError();
                        break;
                    }
                    amount_double = Integer.parseInt(word);
                    col_count++;
                    words_num--;
                    continue;
                }
                if (!isNumeric(word)){
                    if (words_num == 1) { //there is no price col
                        popError();
                        break;
                    }
                    name = name + word + " ";
                    words_num--;
                    continue;
                }
                else // got to the price col
                    price_double = Double.parseDouble(word);

            }
            Item new_item = new Item(name, price_double);
            AllItems.put(new_item, amount_double);
            col_count = 0;
            name = "";
            amount_double = 0;
            price_double = 0;
        }
    }

    private void popError() {
        Dialog error = new Dialog(this);
        error.setContentView(R.layout.activity_setting_items);
        TextView msg = new TextView(this);
        msg.setTextSize(30);
        msg.setText("       Invalid Bill\nTake a new picture");
        error.setContentView(msg);
        error.show();
    }

    private void continueToPicking()
    {
        Intent intent = new Intent(settingItemsActivity.this, PickingItemsActivity.class);
        intent.putExtra("Items",AllItems);
        intent.putExtra("Users", AllUsers);
        settingItemsActivity.this.startActivity(intent);
    }

    private HashMap<Item, Integer> copyMap(HashMap<Item, Integer> cpMap)
    {
        HashMap<Item, Integer> items = new HashMap<Item, Integer>();
        Item tempItem;
        for (Map.Entry<Item, Integer> entry : AllItems.entrySet()) {
            tempItem=entry.getKey();
            items.put(tempItem,0);
        }
        return items;
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

}