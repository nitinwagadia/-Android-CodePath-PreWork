package com.a2do.list.a2do.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.a2do.list.a2do.models.ToDoItem;

import static android.R.attr.version;

/**
 * Created by Nitin on 8/7/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app_db" ;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TO_DO_TABLE_NAME = "TO_DO";
    private static final String PROP_ACTIVITY_NAME="activity";
    private static final String PROP_TASK_NOTES="task_notes";
    private static final String PROP_DUE_DATE="due_date";
    private static final String PROP_PRIORITY ="priority";
    private static final String PROP_STATUS="status";
    private static final String PROP_ACTIVITY_NUMBER="number";

    private static DatabaseHelper m_Instance;

    public static synchronized DatabaseHelper getInstance(Context context) {

        if(m_Instance == null) {
            m_Instance = new DatabaseHelper(context.getApplicationContext());
        }

        return m_Instance;
    }


    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TO_DO_TABLE = "Create TABLE " + DATABASE_TO_DO_TABLE_NAME + "("+ PROP_ACTIVITY_NUMBER +" INTEGER PRIMARY KEY, " +
                PROP_ACTIVITY_NAME + " TEXT, " +
                PROP_TASK_NOTES + " TEXT, " +
                PROP_DUE_DATE + " DATE, " +
                PROP_PRIORITY + " TEXT, " +
                PROP_STATUS + " TEXT )";

        db.execSQL(CREATE_TO_DO_TABLE);
    }

    public void addTodoItem(ToDoItem item)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROP_ACTIVITY_NAME, item.get_activity_name());
        values.put(PROP_PRIORITY, item.get_priority());
        values.put(PROP_STATUS,item.get_status());
        values.put(PROP_TASK_NOTES,item.get_task_notes());
        values.put(PROP_DUE_DATE,item.get_dueDate().toString());
        database.beginTransaction();
        try {
            database.insertOrThrow(DATABASE_TO_DO_TABLE_NAME,null,values);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("ERR", "ERR WHILE AADING");
            e.printStackTrace();
        }
        finally {
            database.endTransaction();
        }




    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TO_DO_TABLE_NAME);
            onCreate(db);
        }
    }
}
