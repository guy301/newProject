package com.ltapps.textscanner;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

/**
 * Created by Guy on 29/03/2018.
 */

public class User implements Serializable {
    private int id;
    private String name;
    private static int numOfUsers=0;
    protected double totalPayment;
    protected HashMap<Item,Integer> items;
    private HashMap<Integer,SharedWraper> sharedItems;
  //  private  List<Integer> shareGrups;

    public User()
    {
        this.name=null;
        numOfUsers++;
        this.id=numOfUsers;
        this.totalPayment=0;
        this.items=new HashMap<Item,Integer>();
        this.sharedItems=new HashMap<Integer,SharedWraper>();
       // this.shareGrups = new ArrayList<Integer>();
    }

    public User(String name,HashMap<Item,Integer> items)
    {
        this.items=items;
        this.name=name;
        numOfUsers++;
        this.id=numOfUsers;
        this.totalPayment=0;
        this.sharedItems=new HashMap<Integer,SharedWraper>();
       // this.shareGrups = new ArrayList<Integer>();

    }

    public HashMap<Integer,SharedWraper> getSharedItems()
    {
        return this.sharedItems;
    }
    public double getTotalPayment()
    {
        return this.totalPayment;
    }
    public HashMap<Item,Integer> getItems()
    {
        return this.items;
    }


    public void addToShared(int GroupUserId,Item itm ,double quantity)
    {
        SharedWraper wrap=isExistingGroup(GroupUserId);
       // double price=itm.getPrice();
       // this.totalPayment+=quantity*price;
        wrap.updateItem(itm,quantity);
    }
//
//    private int isExistingGroupAndItemShared(int[] gruop,Item itm)
//    {
//        int i=0;
//        int id;
//        SharedWraper sharedWraper;
//        Item tmpItm;
//        int[] tmpGroup;
//        Arrays.sort(gruop);
//        boolean flag=true;
//        for (Map.Entry<Integer,SharedWraper> entry : sharedItems.entrySet())
//        {
//            flag=true;
//            id=entry.getKey();
//            sharedWraper=entry.getValue();
//            tmpItm=sharedWraper.getItem();
//            tmpGroup=sharedWraper.getGroup();
//            if(gruop.length!=tmpGroup.length)
//                continue;
//            if(!(itm.equals(tmpItm)))
//                continue;
//            Arrays.sort(tmpGroup);
//            for(int j=0;j<tmpGroup.length;j++)
//            {
//                if(tmpGroup[j]!=gruop[j])
//                    flag=false;
//            }
//            if(flag==true)
//                return id;
//        }
//        return -1;
//    }
    public List<Item> getItemsList()
    {
        List<Item> itemsList = new ArrayList<Item>();

        for (Map.Entry<Item, Integer> entry : this.items.entrySet()) {
            itemsList.add(entry.getKey());
        }
        return itemsList;
    }



    public void updateName(String name)
    {
        this.name=name;
    }

    public void insertItem(Item itm, int amount)
    {
        int val=this.items.get(itm);
        this.items.put(itm,val+amount);
        totalPayment+=amount*itm.getPrice();

    }

    public void updateItem(Item itm, int amount)
    {
        int val=this.items.get(itm);
        this.items.put(itm,amount);
        totalPayment+=(amount-val)*itm.getPrice();
    }

    public void removeItem(Item itm)
    {
        int val=this.items.get(itm);
        this.items.put(itm,0);
        totalPayment-=val*itm.getPrice();
    }

    public String getName()
    {
        if(this.name==null)
            return Integer.toString(this.id);
        return  this.name;
    }

    public int getId()
    {
        return this.id;
    }

    public void addToshareGrups(int newGroupUser)
    {
        SharedWraper wrap=new SharedWraper(newGroupUser);
        this.sharedItems.put(newGroupUser,wrap);
    }

    public GroupUser inTheGruop(List<User> usersList,int[] gruop)
    {
        Iterator<User> iter=usersList.iterator();
        User usr;
        GroupUser gUser;
        int id;

        for (Map.Entry<Integer,SharedWraper> entry : sharedItems.entrySet()) {
            id=entry.getKey();
            gUser=(GroupUser)findUserById(id, usersList);
            if(gUser.isMyGroup(gruop))
                return  gUser;
        }
        return null;
    }

    private SharedWraper isExistingGroup(int groupUser)
    {
        SharedWraper wrap=this.sharedItems.get(groupUser);
        if(wrap==null)
        {
            wrap=new SharedWraper(groupUser);
            this.sharedItems.put(groupUser,wrap);
        }
        return wrap;
    }

    protected User findUserById(int id,List<User> users)
    {
        for(int i=0;i<users.size();i++)
        {
            if(users.get(i).getId()==id)
                return users.get(i);
        }
        return null;
    }

    protected void addToTotalPayment(double sum)
    {
        this.totalPayment+=sum;
    }

    public int getQuantityOfItem(Item itm)
    {
        return this.items.get(itm);
    }
}
