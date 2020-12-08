package com.example.wordbook;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Map;

public class MyViewModel extends ViewModel {
    ArrayList<Map<String,String>> list1 = null;
    private int i = 0;
    public int getI()
    {
        return i;
    }
    public void initI()
    {
        i=0;
    }
    public void SetViewModel(ArrayList<Map<String,String>> list)
    {
        list1 = list;
        i=1;
    }
    public ArrayList<Map<String,String>> getViewModel()
    {
        i=1;
        return list1;
    }
}
