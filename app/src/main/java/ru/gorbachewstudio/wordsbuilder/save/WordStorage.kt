package ru.gorbachewstudio.wordsbuilder.save

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import ru.gorbachewstudio.wordsbuilder.R
import ru.gorbachewstudio.wordsbuilder.word.Word


class WordStorage(activity: Activity){

    private var dbHelper: DBHelper = DBHelper(activity)
    private var _activity: Activity = activity

    private var allWords = arrayListOf<Array<String>>()

    @SuppressLint("Recycle")
    fun checkFullnessDB(): Boolean {
        val database = dbHelper.writableDatabase
        val cursor = database.query(DBHelper.TABLE_WORDS, null, null, null, null, null, null)
        return cursor.count > 0
    }

    fun createWords(){
        Log.i("mySQLite", "Filling word_database")
        val database = dbHelper.writableDatabase
        val contentValues = ContentValues()

        connectArrays()

        val parentWords = _activity.resources.getStringArray(R.array.parent_words)

        allWords.forEachIndexed { index, elem ->
            run {
                elem.forEach {
                    contentValues.put(DBHelper.KEY_PARENT_WORD, parentWords[index])
                    contentValues.put(DBHelper.KEY_WORD, it)
                    contentValues.put(DBHelper.KEY_STATE, 0)
                    database.insert(DBHelper.TABLE_WORDS, null, contentValues)
                }
            }
        }
    }

    @SuppressLint("Recycle")
    fun getWords(parent_word: String): ArrayList<Word> {
        val database = dbHelper.writableDatabase
        val wordsArr: ArrayList<Word> = ArrayList()
        val cursor = database.query(DBHelper.TABLE_WORDS, null, String.format("%s = '%s'", DBHelper.KEY_PARENT_WORD, parent_word) , null, null, null, null)
        if(cursor.moveToFirst()){
            val idIndex = cursor.getColumnIndex(DBHelper.KEY_ID)
            val parentWordIndex = cursor.getColumnIndex(DBHelper.KEY_PARENT_WORD)
            val wordIndex = cursor.getColumnIndex(DBHelper.KEY_WORD)
            val state = cursor.getColumnIndex(DBHelper.KEY_STATE)
            do {
                val word = Word()
                word.id = cursor.getInt(idIndex)
                word.parent_word = cursor.getString(parentWordIndex)
                word.word = cursor.getString(wordIndex)
                word.state = cursor.getInt(state)
                wordsArr.add(word)
            }while (cursor.moveToNext())
        } else{
            Log.d("mLog", "db empty")
        }
        cursor.close()
        return wordsArr
    }

    fun resetDb(){
        val database = dbHelper.writableDatabase
        database.delete(DBHelper.TABLE_WORDS, null, null)
        Toast.makeText(_activity, _activity.resources.getString(R.string.resetDb),Toast.LENGTH_LONG).show()
    }

    fun openWord(id: Int){
        val dbHelper = DBHelper(_activity)
        val database = dbHelper.writableDatabase
        val cv = ContentValues()
        cv.put(DBHelper.KEY_STATE, 1)
        database.update(
            DBHelper.TABLE_WORDS, cv,
            java.lang.String.format("%s = %s", DBHelper.KEY_ID, id),
            null
        )
        dbHelper.close()
    }

    fun getWordCount(parent_word: String): String{
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val result = DatabaseUtils.queryNumEntries(db, DBHelper.TABLE_WORDS, String.format("%s = '%s'", DBHelper.KEY_PARENT_WORD, parent_word))
        db.close()
        return result.toString()
    }

    fun getOpenWordCount(parent_word: String): String{
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val result = DatabaseUtils.queryNumEntries(db, DBHelper.TABLE_WORDS, String.format("%s = '%s' AND state = 1", DBHelper.KEY_PARENT_WORD, parent_word))
        db.close()
        return result.toString()
    }


    private fun connectArrays(){
        allWords.add(_activity.resources.getStringArray(R.array.tractor))
        allWords.add(_activity.resources.getStringArray(R.array.registration))
        allWords.add(_activity.resources.getStringArray(R.array.keyboard))

        allWords.add(_activity.resources.getStringArray(R.array.eucalyptus))
        allWords.add(_activity.resources.getStringArray(R.array.hangGlider))
        allWords.add(_activity.resources.getStringArray(R.array.democracy))
    }

}