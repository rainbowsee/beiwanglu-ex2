package com.example.wordbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fm = null;
    private FragmentTransaction transaction = null;
    private FragmentTransaction transaction1 = null;
    private HomeFragment hf = null;
    private WordbookFragment wf = null;
    private SearchFragment sf = null;
    DBHelper data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button rb_home = (Button) findViewById(R.id.rb_home);
        final Button rb_word = (Button) findViewById(R.id.rb_word);
        final Button rb_search = (Button) findViewById(R.id.rb_search);
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentManager fm1 = getSupportFragmentManager();

        transaction = fm.beginTransaction();
        data = new DBHelper(this);
        hf = new HomeFragment();
        transaction.commit();
        transaction.replace(R.id.fl_container,hf);
        rb_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hf = new HomeFragment();
                wf = new WordbookFragment();
                sf = new SearchFragment();
                transaction1 = fm1.beginTransaction();
                transaction1.replace(R.id.fl_container,hf);
                transaction1.hide(wf).hide(sf).show(hf);
                transaction1.commit();
            }
        });
        rb_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hf = new HomeFragment();
                wf = new WordbookFragment();
                sf = new SearchFragment();
                transaction1 = fm1.beginTransaction();
                transaction1.replace(R.id.fl_container,wf);
                transaction1.hide(hf).hide(sf).show(wf);
                transaction1.commit();
            }
        });
        rb_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hf = new HomeFragment();
                wf = new WordbookFragment();
                sf = new SearchFragment();
                transaction1 = fm1.beginTransaction();
                transaction1.replace(R.id.fl_container,sf);
                transaction1.hide(wf).hide(hf).show(sf);
                transaction1.commit();
            }
        });
    }

}