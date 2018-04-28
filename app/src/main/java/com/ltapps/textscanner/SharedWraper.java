package com.ltapps.textscanner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Guy on 05/04/2018.
 */

public class SharedWraper implements Serializable {
    HashMap<Item,Double> items;
    private int gruop;

    public SharedWraper(int gruop)
    {
        this.gruop=gruop;
        this.items=new HashMap<Item,Double>();
    }

    public void updateItem(Item itm,double quantity)
    {
//        double oldQuantity=0;
//        if(this.items.get(itm)!=null)
//            oldQuantity =this.items.get(itm);
//        this.items.put(itm,oldQuantity+quantity);
        this.items.put(itm,quantity);
    }
    public HashMap<Item,Double> getItems()
    {
        return this.items;
    }

}
