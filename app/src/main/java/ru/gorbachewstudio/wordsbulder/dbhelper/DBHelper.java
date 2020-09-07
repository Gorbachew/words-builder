package ru.gorbachewstudio.wordsbulder.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "wordsDb";
    public static final String TABLE_WORDS = "words";

    public static final String KEY_ID = "_id";
    public static final String KEY_PARENT_WORD = "parent_word";
    public static final String KEY_WORD = "word";
    public static final String KEY_STATE = "state";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("create table %s(%s integer primary key,%s text,%s text,%s integer)", TABLE_WORDS, KEY_ID, KEY_PARENT_WORD, KEY_WORD, KEY_STATE));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
