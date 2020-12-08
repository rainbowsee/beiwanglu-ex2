package com.example.wordbook;

import android.provider.BaseColumns;

public class Words {
    public Words(){}
    public static abstract class Word implements BaseColumns
    {
        public static final String TABLE_NAME="words";
        public static final String COLUMN_NAME_WORD="name";
        public static final String COLUMN_NAME_TRANSLATE="translate";
        public static final String COLUMN_NAME_EXAMPLE="example";
    }
}
