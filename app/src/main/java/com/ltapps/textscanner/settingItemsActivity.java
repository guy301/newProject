package com.ltapps.textscanner;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class settingItemsActivity extends AppCompatActivity {
    private Button go_back_b, Continue, addFriends;
    private String text;
    private HashMap<Item, Integer> AllItems = new HashMap<Item, Integer>();
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
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.ImageContainer);
        int i=0;
        for (String name : words){
            final Button yourButton = new Button(this);
            yourButton.setText(name);
            yourButton.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            yourButton.setX(-700);
            yourButton.setY(300+i*150);
            i++;
            layout.addView(yourButton );
        }
    }


    public void addButtons(String s)
    {
        processText(s);
        if (AllItems.isEmpty()) {
            return;
        }
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.ImageContainer);
        int i=0;
        for (Map.Entry<Item, Integer> entry : AllItems.entrySet()){
            final String b_text = entry.getKey().getName() + "\n" + entry.getKey().getPrice() + "\n"
                    + entry.getValue();
            final Button yourButton = new Button(this);
            yourButton.setText(b_text);
            yourButton.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            yourButton.setY(300+i*150);
            i++;
            layout.addView(yourButton );
            yourButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    showDialog(b_text, yourButton);
                    return true;
                }
            });
        }
    }

    private void showDialog(String str, final Button btn) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("input text");
        //final View view = LayoutInflater.from(this).inflate(R.layout.activity_setting_items, null);
        //final EditText edit_dialog = findViewById(R.id.edit_dialog);
        final EditText edit = new EditText(this);
        edit.setText(str);
        builder.setView(edit);
        builder.setNegativeButton("cancel",null);
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btn.setText(edit.getText().toString());
            }
        });
        builder.show();
    }

    public void processText(String s){
        final String[] words = s.split("\\s+");
        int j=0;//j=4 so we pass the titles, assuming there are 4 titles
        int words_num = words.length;
        //while (j < words_num-1-4) {//(-4) to ignore the total line assuming there are 4 columns
        int length=words.length;
        for (j=0;j<length-3;) {
//            final String item = "food" + j;
//            final String amount_s = "1";
//            final int amount_int = Integer.parseInt(amount_s);
//            final int price_int = j++;


            final String item = words[j++];
            final String amount_s = words[j++];
            final int amount_int = Integer.parseInt(amount_s);
            final String price_s = words[j++];
            final int price_int = Integer.parseInt(price_s);
            final String total_s = words[j++];
            final int total_int = Integer.parseInt(total_s);
            Item new_item = new Item(item, price_int);

            AllItems.put(new_item, amount_int);
        }
    }

    private void continueToPicking()
    {



        Intent intent = new Intent(settingItemsActivity.this, PickingItemsActivity.class);
        intent.putExtra("Items",AllItems);

        // temp, need to create real list!!!
        ArrayList<User> users=new ArrayList<User>();
        User guy=new User("Guy",copyMap(AllItems));
        User sap=new User("Sap",copyMap(AllItems));
        User jina=new User("Jina",copyMap(AllItems));
        users.add(guy);
        users.add(sap);
        users.add(jina);
        //
        intent.putExtra("Users",users);
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

}
