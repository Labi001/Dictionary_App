package com.labinot.dictionary.dataBaseHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "eng_dictionary.db";
    private static final String TABLE_NAME = "words";
    public static final String EN_WORD = "en_word";
    public static final String EN_DEFINITION = "en_definition";
    public static final String EXAMPLE = "example";
    public static final String SYNONYMS = "synonyms";
    public static final String ANTONYMS = "antonyms";
    private ProgressInterface progressInterface;
    private String perqindja;

    private String DB_PATH = null;
    private SQLiteDatabase myDatabase;


    public DataBaseHelper(@Nullable Context context,ProgressInterface progressInterface) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";

        if(progressInterface != null)
            this.progressInterface = progressInterface;
    }

    public void createDataBase() throws IOException{
        
        boolean DbisCreated = CheckDatabase();

        if(!DbisCreated){

            this.getReadableDatabase();

            try {

                copyDataBase();

            }catch (IOException e){

                throw new Error("Error copying database !"+e.getMessage());
            }

        }

        
    }

    @SuppressLint("SuspiciousIndentation")
    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String OutfileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(OutfileName);

        byte[] buffer = new byte[1024];
        int length;
        long total = 0;

        int fileSize = myInput.available();

        while ((length = myInput.read(buffer))>0){

            total = total + length;
            perqindja = String.valueOf((total * 100)/fileSize);

            if(progressInterface != null)
            progressInterface.onProgressUpdate(perqindja);

            myOutput.write(buffer,0,length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public boolean CheckDatabase() {

        SQLiteDatabase checkDB= null;

        try {

            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);

        }catch (SQLException i){

        }

        if(checkDB != null)
            checkDB.close();

        return checkDB!=null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {

            this.getReadableDatabase();
            context.deleteDatabase(DB_NAME);
            copyDataBase();

        }catch (IOException i){

            i.printStackTrace();
        }

    }

    public void openDatabase() throws SQLException {

        String path = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);

    }


    @Override
    public synchronized void close() {
        super.close();

        if(myDatabase != null)
            myDatabase.close();

        super.close();
    }

    public Cursor getMeaning(String text) {

        return myDatabase.rawQuery("SELECT " + EN_DEFINITION + "," + EXAMPLE + "," + SYNONYMS + "," + ANTONYMS + " FROM " + TABLE_NAME + " WHERE " + EN_WORD + "==UPPER('" + text + "')", null);
    }

    public Cursor getSuggestion(String text) {

        return myDatabase.rawQuery("SELECT _id" + ", " + EN_WORD + " FROM " + TABLE_NAME + " WHERE " + EN_WORD + " LIKE '" + text + "%' LIMIT 40", null);

    }

    public Cursor getHistory() {

        return myDatabase.rawQuery("select distinct word, en_definition from history h join words w on h.word==w.en_word order by h._id desc", null);
    }

    public void insertHistory(String words) {

        myDatabase.execSQL("INSERT INTO history(word) VALUES(UPPER('" + words + "'))");

    }

    public void deleteHistory() {

        myDatabase.execSQL("DELETE FROM history");
    }
}
