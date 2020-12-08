package com.example.wordbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MyViewModel viewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DBHelper data;
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        final EditText editText = view.findViewById(R.id.wb_search);
        String s = null;
        data = new DBHelper(getActivity());
        Button button = view.findViewById(R.id.search1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText.getText().toString();
                viewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
                viewModel.SetViewModel(SearchUseSql(s));
                Button book = (Button) getActivity().findViewById(R.id.rb_word);
                book.performClick();
            }
        });
        return view;
    }
    private ArrayList<Map<String, String>> SearchUseSql(String strWordSearch){
        SQLiteDatabase db=data.getReadableDatabase();
        ArrayList<Map<String, String>> result = new ArrayList<>();
        String sql="select * from words where name like ? order by name desc";
        Cursor cursor=db.rawQuery(sql,new String[]{"%"+strWordSearch+"%"});
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(Words.Word._ID, String.valueOf(cursor.getInt(0)));
            map.put(Words.Word.COLUMN_NAME_WORD, cursor.getString(1));
            map.put(Words.Word.COLUMN_NAME_TRANSLATE, cursor.getString(2));
            map.put(Words.Word.COLUMN_NAME_EXAMPLE, cursor.getString(3));
            result.add(map);
        }
        return  result;
    }
}