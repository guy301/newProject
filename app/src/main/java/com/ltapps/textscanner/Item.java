package com.ltapps.textscanner;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by Guy on 29/03/2018.
 */

public class Item implements Serializable {

    private double price;
    private String name;

    public Item(String name, double price)
    {
        this.name=name;
        this.price=price;
    }

    public void updatePrice(int price)
    {
        this.price=price;
    }

    public void updateName(String name)
    {
        this.name=name;
    }

    public String getName()
    {
        return this.name;
    }

    public double getPrice()
    {
        return  this.price;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == this)
            return true;
        if(o instanceof String)
        {
            if(this.name.equals((String)o))
                return true;
            return false;
        }
        else if(o instanceof Item)
        {
            Item itm=(Item) o;

            if(this.name.equals(itm.name))
                return true;
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int res=0;
        char c;
        int j=1;
        for(int i=0; i<this.name.length();i++)
        {
            c=this.name.charAt(i);
            res+=c*j;
            j*=10;

        }
        return res;
    }

}
