package com.example.wordbook;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordbookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordbookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DBHelper data;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WordbookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordbookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordbookFragment newInstance(String param1, String param2) {
        WordbookFragment fragment = new WordbookFragment();
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
        MyViewModel viewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        ArrayList<Map<String,String>> item;
        if(viewModel.getI()==0)
            item = getAll();
        else
            item = viewModel.getViewModel();
        View wbview = inflater.inflate(R.layout.fragment_wordbook,container,false);
        ListView wordlist = (ListView)wbview.findViewById(R.id.word_list);
        registerForContextMenu(wordlist);
        data = new DBHelper(getActivity());
        setWordsListView(wbview,item);
        setHasOptionsMenu(true);
        return wbview;

    }
    private ArrayList<Map<String, String>> getAll(){
        data = new DBHelper(getActivity());
        SQLiteDatabase db=data.getReadableDatabase();
        ArrayList<Map<String, String>> result = new ArrayList<>();
        String[] words = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD,
                Words.Word.COLUMN_NAME_TRANSLATE,
                Words.Word.COLUMN_NAME_EXAMPLE
        };
        String px =
                Words.Word.COLUMN_NAME_WORD + " DESC";
        Cursor cursor = db.query(Words.Word.TABLE_NAME, words, null, null, null, null, px);
        while (cursor.moveToNext()) {//将Cursor对象转换为list 显示在listView
            Map<String, String> map = new HashMap<>();
            map.put(Words.Word._ID, String.valueOf(cursor.getInt(0)));
            map.put(Words.Word.COLUMN_NAME_WORD, cursor.getString(1));
            map.put(Words.Word.COLUMN_NAME_TRANSLATE, cursor.getString(2));
            map.put(Words.Word.COLUMN_NAME_EXAMPLE, cursor.getString(3));
            result.add(map);
        }
        return result;
    }
    private void setWordsListView(View v,ArrayList<Map<String,String>> items)
    {

        SimpleAdapter adapter = new SimpleAdapter(getActivity(),items,R.layout.word_item,
                new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD, Words.Word.COLUMN_NAME_TRANSLATE, Words.Word.COLUMN_NAME_EXAMPLE},
                new int[]{R.id.word_id,R.id.word_name,R.id.word_translate,R.id.word_example});
        ListView list = (ListView)v.findViewById(R.id.word_list);
        list.setAdapter(adapter);
    }
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add,menu);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.change,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.wordadd:
                InsertDialog();
                break;
            case R.id.word_zero:
                MyViewModel myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
                myViewModel.initI();
                Button book = (Button) getActivity().findViewById(R.id.rb_word);
                book.performClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
            TextView textId=null;
            TextView textWord=null;
            TextView textTranslate=null;
            TextView textExample=null;

            AdapterView.AdapterContextMenuInfo info=null;
            View itemView=null;

            info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            itemView = info.targetView;
            switch (item.getItemId())
            {
                case R.id.worddelete:
                    textId =(TextView)itemView.findViewById(R.id.word_id);
                    if(textId!=null){
                        String strId=textId.getText().toString();
                        DeleteDialog(strId);
                    }
                    break;
                case R.id.wordchange:
                    textId =(TextView)itemView.findViewById(R.id.word_id);
                    textWord =(TextView)itemView.findViewById(R.id.word_name);
                    textTranslate =(TextView)itemView.findViewById(R.id.word_translate);
                    textExample =(TextView)itemView.findViewById(R.id.word_example);
                    if(textId!=null && textWord!=null && textTranslate!=null && textExample!=null) {
                        String strId = textId.getText().toString();
                        String strWord = textWord.getText().toString();
                        String strTranslate = textTranslate.getText().toString();
                        String strExample = textExample.getText().toString();
                        UpdateDialog(strId, strWord, strTranslate, strExample);
                    }
                    break;
            }
            return super.onContextItemSelected(item);
    }
    private void DeleteDialog(final String strId){
        new android.app.AlertDialog.Builder(getActivity()).setTitle("删除备忘录").setMessage("确认删除").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteUseSql(strId);
                setWordsListView(getView(),getAll());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }
    private void InsertDialog() {
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.adddialog, null);
        new android.app.AlertDialog.Builder(getActivity())
                .setTitle("添加新备忘录")//标题
                .setView(linearLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord=((EditText)linearLayout.findViewById(R.id.add_word)).getText().toString();
                        String strTranslate=((EditText)linearLayout.findViewById(R.id.add_translate)).getText().toString();
                        String strExample=((EditText)linearLayout.findViewById(R.id.add_example)).getText().toString();
                        InsertUserSql(strWord, strTranslate, strExample);
                        ArrayList<Map<String, String>> items=getAll();
                        setWordsListView(getView(),items);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框


    }
    private void UpdateDialog(final String strId, final String strWord, final String strTranslate, final String strExample) {
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.updatedialog, null);
        ((EditText)linearLayout.findViewById(R.id.update_word)).setText(strWord);
        ((EditText)linearLayout.findViewById(R.id.update_translate)).setText(strTranslate);
        ((EditText)linearLayout.findViewById(R.id.update_example)).setText(strExample);
        new android.app.AlertDialog.Builder(getActivity())
                .setTitle("修改备忘录")//标题
                .setView(linearLayout)//设置视图
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String NewWord = ((EditText) linearLayout.findViewById(R.id.update_word)).getText().toString();
                        String NewTranslate = ((EditText) linearLayout.findViewById(R.id.update_translate)).getText().toString();
                        String NewExample = ((EditText) linearLayout.findViewById(R.id.update_example)).getText().toString();
                        UpdateUseSql(strId, NewWord, NewTranslate, NewExample);
                        setWordsListView(getView(),getAll());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();


    }

    private void InsertUserSql(String strWord, String strTranslate, String strExample){
        String sql="insert into  words(name,translate,example) values(?,?,?)";

        SQLiteDatabase db = data.getWritableDatabase();
        db.execSQL(sql,new String[]{strWord,strTranslate,strExample});
    }
    //使用Sql语句更新单词
    private void UpdateUseSql(String strId,String strWord, String strTranslate, String strExample) {
        SQLiteDatabase db =data.getReadableDatabase();
        String sql="update words set name=?,translate=?,example=? where _id=?";
        db.execSQL(sql, new String[]{strWord, strTranslate, strExample,strId});
    }
    private void DeleteUseSql(String strId) {
        String sql="delete from words where _id='"+strId+"'";
        SQLiteDatabase db = data.getReadableDatabase();
        db.execSQL(sql);
    }

}