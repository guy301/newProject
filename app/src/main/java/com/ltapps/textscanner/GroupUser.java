package com.ltapps.textscanner;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupUser  extends User{
    private int[] members;
    private int size;
    private List<User> users;

    public GroupUser(List<User> users, int[] group,HashMap<Item,Integer> items)
    {
        super("group_user",items);
        this.members=group;
        this.size=group.length;
        this.users=users;
        updateMymember(users);
    }

    @Override
    public void updateItem(Item itm, int amount)
    {
        User user;
        int val=this.items.get(itm);
        double sum=((double)itm.getPrice()*(amount-val))/this.size;
        for(int i=0;i<this.size;i++)
        {

            user=findUserById(members[i],users);
            user.addToShared(this.getId(),itm ,((double)amount)/size);
           user.addToTotalPayment(sum);
        }
        this.items.put(itm,amount);


        totalPayment+=(amount-val)*itm.getPrice();
    }
    private void updateMymember(List<User> users)
    {
        for(int i=0;i<users.size();i++)
            users.get(i).addToshareGrups(this.getId());
    }

    public boolean isMember(int id)
    {
        for(int i=0; i<this.members.length ; i++)
        {
            if(this.members[i]==id)
                return true;
        }
        return false;
    }


    public int[] getMembers(){
        return this.members;
    }

    public int getNumOfMembers()
    {
        return this.members.length;
    }

    // return random user from the group. the return user id != id
    public int getRandomMemberId(int id)
    {
        if(this.members[0] == id)
          return this.members[1];
        return this.members[0];
    }
    public boolean isMyGroup(int[] group1)
    {
        if(group1.length!=members.length)
            return false;
        int[] tmp=new int[group1.length];
        for(int i=0;i<tmp.length;i++) // initialize tmp
            tmp[i]=0;
        boolean flag=false;
        for(int i=0;i<group1.length;i++)
        {
            flag=false;
            for(int j=0; j<members.length;j++)
            {
                if((tmp[j]==0) && (group1[i]==members[j]))
                {
                    flag=true;
                    tmp[j]=1;
                    break;
                }
            }
            if(flag==false)
                return false;

        }
        return true;
    }
}
