package com.scsa.android.selfManagement.toDoList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDatabase {
    private static TaskDatabase database;
    public static String DATABASE_NAME = "todo.db";
    public static String TABLE_TASK = "TASK";
    public static int DATABASE_VERSION = 1;

    private static final String TAG = "Database";

    private Context context;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    private TaskDatabase(Context context){
        this.context = context;
    }

    public static TaskDatabase getInstance(Context context){
        if(database == null){
            database = new TaskDatabase(context);
        }
        return database;
    }

    public Cursor rawQuery(String SQL){

        Cursor c = null;
        try{
            c = db.rawQuery(SQL,null);
        } catch (Exception e){
            Log.i("Exception_msg","Exception - db.rawQuery");
        }

        return c;
    }

    public boolean execSQL(String SQL){
        try{
            Log.d("Exception_msg", "SQL : " + SQL);
            db.execSQL(SQL);
        } catch (Exception e){
            Log.i("Exception_msg","Exception - execSQL");
            return false;
        }
        return true;
    }

    public boolean open(){

        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    public void close(){
        db.close();
        database = null;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name,factory,version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            String DROP_SQL = "drop table if exists " +TABLE_TASK;

            try {
                db.execSQL(DROP_SQL);

            } catch (Exception ex){
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            String CREATE_SQL = "create table " + TABLE_TASK +
                    "("
                    + " _id integer NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  TODO TEXT DEFAULT '', DESCT TEXT DEFAULT '',  "
                    + "  PRIOR integer DEFAULT 0, TIME TEXT DEFAULT 'click here'"
                    + ")";
            try{
                db.execSQL(CREATE_SQL);
            } catch (Exception ex){
                Log.e(TAG,"Exception in CREATE_SQL", ex);
            }

            String CREATE_INDEX_SQL = "create index " + TABLE_TASK + "_IDX ON " + TABLE_TASK + "("
                    + "_id"
                    + ")";
            try{
                db.execSQL(CREATE_INDEX_SQL);
            } catch (Exception ex){
                Log.e(TAG, "Exception in CREATE_INDEX_SQL",ex);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}












