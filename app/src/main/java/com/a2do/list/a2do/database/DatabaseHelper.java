package com.a2do.list.a2do.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.a2do.list.a2do.models.ItemType;
import com.a2do.list.a2do.models.ToDoItem;

import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.version;

/**
 * Created by Nitin on 8/7/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "app_db" ;
    private static final String DATABASE_TO_DO_TABLE_NAME = "TO_DO";
    public static final String PROP_ACTIVITY_NAME="activity";
    public static final String PROP_TASK_NOTES="task_notes";
    public static final String PROP_DUE_DATE="due_date";
    public static final String PROP_PRIORITY ="priority";
    public static final String PROP_STATUS="status";
    public static final String PROP_ACTIVITY_NUMBER="id";
    public static final String UTIL_ITEM_TYPE="item_type";

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

    public void getTodoItems(ArrayList<ToDoItem> items) {

        SQLiteDatabase readableDatabase = getReadableDatabase();
        String todoItemQuery = String.format("SELECT * FROM %s ", DATABASE_TO_DO_TABLE_NAME );
        readableDatabase.beginTransaction();
        Cursor cursor = readableDatabase.rawQuery(todoItemQuery, null);
            try {
                while(cursor.moveToNext())
                {
                    ToDoItem item = new ToDoItem();
                    item.set_id(cursor.getInt(cursor.getColumnIndex(PROP_ACTIVITY_NUMBER)));
                    item.set_activity_name(cursor.getString(cursor.getColumnIndex(PROP_ACTIVITY_NAME)));
                    item.set_item_type(ItemType.ITEM_TYPE_EXIST);
                    item.set_priority(cursor.getString(cursor.getColumnIndex(PROP_PRIORITY)));
                    item.set_status(cursor.getString(cursor.getColumnIndex(PROP_STATUS)));
                    item.set_task_notes(cursor.getString(cursor.getColumnIndex(PROP_TASK_NOTES)));
                    item.set_dueDate(new Date());
                    items.add(item);
                }
                readableDatabase.setTransactionSuccessful();
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                    readableDatabase.endTransaction();
                }
            }

    }

    public int deleteTodoItem(ToDoItem item) {
        int result = -1;

        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            result = writableDatabase.delete(DATABASE_TO_DO_TABLE_NAME,
                    PROP_ACTIVITY_NUMBER+"= ?",
                    new String[]{String.valueOf(item.get_id())});
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            writableDatabase.endTransaction();
        }

        return result;
    }

    public long addorUpdateTodoItem(ToDoItem item)
    {
        long result=0;
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROP_ACTIVITY_NAME, item.get_activity_name());
        values.put(PROP_PRIORITY, item.get_priority());
        values.put(PROP_STATUS,item.get_status());
        values.put(PROP_TASK_NOTES,item.get_task_notes());
        values.put(PROP_DUE_DATE,item.get_dueDate().toString());
        database.beginTransaction();
        try {
            if(item.get_item_type() == ItemType.ITEM_TYPE_NEW) {
                 result = database.insertOrThrow(DATABASE_TO_DO_TABLE_NAME,null,values);
            }
            else {
                 result = database.update(DATABASE_TO_DO_TABLE_NAME,values,PROP_ACTIVITY_NUMBER+ "=?",new String[]{String.valueOf(item.get_id())});
            }

            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("ERR", "ERR WHILE AADING");
            e.printStackTrace();
        }
        finally {
            database.endTransaction();
        }

        return result;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TO_DO_TABLE_NAME);
            onCreate(db);
        }
    }
}
