package com.ltapps.textscanner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guy on 05/04/2018.
 */

public class SharedWraper implements Serializable {
    private double quantity;
    private Item item;
    private int[] gruop;

    public SharedWraper(double quantity,Item itm,int[] gruop)
    {
        this.gruop=gruop;
        this.quantity=quantity;
        this.item=itm;
    }

    public int[] getGroup()
    {
        return this.gruop;
    }

    public Item getItem()
    {
        return this.item;
    }

    public double getQuantity()
    {
        return  this.quantity;
    }

    public void addToQuantity(double sum)
    {
        this.quantity+=sum;
    }

    public void updateQuantity(double sum)
    {
        this.quantity=sum;
    }


}
