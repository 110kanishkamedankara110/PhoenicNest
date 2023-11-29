package com.phoenix.phoenixNest.util;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

public class ListWithListener<T> extends LinkedList<T>{

    ListListener listener;

    public  ListWithListener(ListListener l){
        listener=l;
    }

    @Override
    public boolean add(T item){
       Boolean res=super.add(item);
        listener.onAdd(item);
        return res;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        Boolean res= super.remove(o);
        listener.onRemove();
        return res;
    }

}
