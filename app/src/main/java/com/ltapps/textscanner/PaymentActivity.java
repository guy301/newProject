package com.ltapps.textscanner;


        import android.content.DialogInterface;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.InputType;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import org.w3c.dom.Text;

        import java.text.DecimalFormat;
        import java.text.NumberFormat;
        import java.text.ParsePosition;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private double tip=1;
    private List<User> users;
    private Map<Button,User> buttons;
    private  HashMap<Item,Integer> remainMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        users = (ArrayList<User>) getIntent().getSerializableExtra("Users");
        remainMap= (HashMap<Item,Integer>)getIntent().getSerializableExtra("remainItems");
        buttons=new HashMap<Button, User>();
        addUserButtons();
    }


    private void addUserButtons()
    {
        LinearLayout layout = (LinearLayout) findViewById(R.id.usersNames);
        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);

        String name;
        User usr;
        double payment;
        int j=0;
        for (int i=0;i<users.size();i++) {
            usr=users.get(i);
            if(usr instanceof GroupUser)
                continue;
            name=usr.getName();
            payment=usr.getTotalPayment()*tip;
            Button btn = new Button(this);
            btn.setText(name + ": " + new DecimalFormat("##.##").format(payment)+"$");
            // btn.setTextColor(255);
            layout.addView(btn);
            buttons.put(btn,usr);
            userOnclik(btn,usr);
            j++;
        }
    }

    private void updateUsers()
    {
        String name;
        User usr;
        double payment;
        Button btn;
        for (Map.Entry<Button, User> entry : buttons.entrySet())
        {
            btn=entry.getKey();
            usr=entry.getValue();
            name=usr.getName();
            payment=usr.getTotalPayment()*tip;
            btn.setText(name + ": " + new DecimalFormat("##.##").format(payment)+"$");
        }
    }


    public void setTip(final View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eter tip percentage");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s=input.getText().toString();
                int tmp=0;
                if(s!=null && s!="" && !s.isEmpty() && isNumeric(s) ) {
                    tmp = Integer.parseInt(s);
                    tip = 1 + (double) tmp / 100;
                    updateUsers();
                    dialog.cancel();
                }
            }
        });
        final  AlertDialog a = builder.create();
        Button btn=(Button) findViewById(R.id.Tip);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.show();
            }
        });
    }
    public void userOnclik(Button btn,User user)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Items");
        String items=getUserItems(user);
        String sharedItems=getUserSharedItems(user);
        if(sharedItems!=null)
        {
            items+="Shared Items: \n"+sharedItems;
        }
        final TextView text = new TextView(this);
        text.setText(items);
        builder.setView(text);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final  AlertDialog a = builder.create();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.show();
            }
        });
    }

    private String getUserItems(User usr)
    {
        String str=null;
        HashMap<Item,Integer> items=usr.getItems();
        int quantity;
        String name;
        double price;
        String tmp;
        int len=getLongestItemName(items)+3;
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            quantity = entry.getValue();

            if(quantity!=0)
            {
                price=entry.getKey().getPrice();
                name=entry.getKey().getName();
                tmp=new DecimalFormat("##.##").format(price);
                tmp+=name;
                if(str==null)
                {
                    str="   "+name+" x "+quantity;
                    for(int i=tmp.length();i<len;i++)
                        str+=" ";
                    str+=price+"$"+"\n";
                }

                else
                {
                    str+="   "+name+" x "+quantity;
                    for(int i=tmp.length();i<len;i++)
                        str+=" ";
                    str+=price+"$"+"\n";
                }

            }
        }
        if(str==null)
            str="None";
        return str;
    }

    private String getUserSharedItems(User usr)
    {
        String str=null;
        HashMap<Integer,SharedWraper>  UserSharedWrap=usr.getSharedItems();
        double quantity;
        String name;
        double price;
        String tmp;
        SharedWraper sharedWraper;
        HashMap<Item,Double> items;
        Item itm;
        int[] group;
        int gUserId;
        GroupUser gUser;
        User friendUser;
        for (Map.Entry<Integer,SharedWraper> entry : UserSharedWrap.entrySet()) {
            sharedWraper=entry.getValue();
            items=sharedWraper.getItems();
            for(Map.Entry<Item,Double> entry1:items.entrySet() ) {
                quantity = entry1.getValue();

                if (quantity != 0) {
                    gUserId=entry.getKey();
                    gUser=(GroupUser)findUserById(gUserId);
                    group = gUser.getMembers();
                    itm = entry1.getKey();
                    price = itm.getPrice();
                    name = itm.getName();
                    tmp = new DecimalFormat("##.##").format(price);
                    tmp += name;
                    if (str == null) {
                        str = "   " + name + " x " + new DecimalFormat("##.##").format(quantity) + "   ";
                        str += price + "$" + "     shared with: ";
                        for (int i = 0; i < group.length; i++) {
                            friendUser = findUserById(group[i]);
                            if (friendUser.getName() == usr.getName())
                                continue;
                            str += friendUser.getName();
                            if (i != (group.length - 1))
                                str += ",";
                        }
                    } else {
                        str += "   " + name + " x " + new DecimalFormat("##.##").format(quantity) + "   ";
                        str += price + "$" + " shared with: ";
                        for (int i = 0; i < group.length; i++) {
                            friendUser = findUserById(group[i]);
                            if (friendUser.getName() == usr.getName())
                                continue;
                            str += friendUser.getName() + ", ";
                            if (i != (group.length - 1))
                                str += ",";
                        }
                    }
                    str += "\n";

                }
            }
        }
        return str;
    }


    private User findUserById(int id)
    {
        for(int i=0;i<users.size();i++)
        {
            if(users.get(i).getId()==id)
                return users.get(i);
        }
        return null;
    }

    private int getLongestItemName(HashMap<Item,Integer> items)
    {
        int len=0;
        int quantity=0;
        String name;
        double price=0;
        String tmp;
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            quantity = entry.getValue();
            if(quantity!=0)
            {

                name=entry.getKey().getName();
                price=entry.getKey().getPrice();
                tmp=new DecimalFormat("##.##").format(price);
                tmp+=name;
                if(tmp!=null)
                {
                    if(tmp.length()>len)
                        len=tmp.length();
                }
            }
        }
        return len;
    }
    public void remainItems(View view)
    {
        String str=getRemainItemsAsString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remain Items");
        final TextView text = new TextView(this);
        text.setText(str);
        builder.setView(text);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final  AlertDialog a = builder.create();
        ((Button)view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.show();
            }
        });
    }


    private String getRemainItemsAsString()
    {
        String str="None";
        boolean flag=true;
        Item itm;
        String name;
        int quantity;
        for (Map.Entry<Item, Integer> entry : remainMap.entrySet()) {
            quantity=entry.getValue();
            if(quantity==0)
                continue;
            itm=entry.getKey();
            name=itm.getName();
            if(flag)
            {
                flag=false;
                str=name+" x "+quantity+"\n";
            }
            else
            {
                str+=name+" x "+quantity+"\n";
            }
        }
        return str;
    }

    public static boolean isNumeric(String str)
    {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }
}



