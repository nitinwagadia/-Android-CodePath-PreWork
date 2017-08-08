package com.a2do.list.a2do;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.a2do.list.a2do.database.DatabaseHelper;
import com.a2do.list.a2do.models.ToDoItem;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    final static String fileName = "Items.txt";
    final static String intent_edit_item = "EDIT_ITEM";
    final static String intent_edit_item_position = "ITEM_POSITION";
    final static int REQUEST_CODE = 1;
    final static int RESULT_CODE = 2;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    EditText newItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateListItems();
        newItemText = (EditText) findViewById(R.id.etNewItem);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                WriteItemsToFile();
                itemsAdapter.notifyDataSetChanged();
                return false;
            }
        });

        DatabaseHelper database = DatabaseHelper.getInstance(this);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent launchEditActivty = new Intent(MainActivity.this, EditActivity.class);
                launchEditActivty.putExtra(intent_edit_item, items.get(position));
                launchEditActivty.putExtra(intent_edit_item_position, position);
                startActivityForResult(launchEditActivty, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == resultCode && data != null) {
            String editedItem = data.getStringExtra(MainActivity.intent_edit_item);
            int position = data.getIntExtra(MainActivity.intent_edit_item_position, 0);
            items.set(position, editedItem);
            WriteItemsToFile();
            itemsAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Yaass", Toast.LENGTH_LONG).show();

        }
    }

    private void readItemsFromFile() {
        File file = new File(getApplicationContext().getFilesDir(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            items = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteItemsToFile() {
        File file = new File(getApplicationContext().getFilesDir(), fileName);
        try {
            FileUtils.writeLines(file, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateListItems() {
        readItemsFromFile();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
    }

    public void OnAddItems(View view) {
        String newItem = newItemText.getText().toString();
        if (newItem.isEmpty()) {
            Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseHelper db = DatabaseHelper.getInstance(this);
            Date d = new Date();
            db.addTodoItem(new ToDoItem("TASK NOTES", "PENDING", "low", newItem, d));
            items.add(newItem);
            newItemText.setText("");
            WriteItemsToFile();
            itemsAdapter.notifyDataSetChanged();
        }


    }
}
