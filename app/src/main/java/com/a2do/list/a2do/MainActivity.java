package com.a2do.list.a2do;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.a2do.list.a2do.Adapters.ListAdapter;
import com.a2do.list.a2do.database.DatabaseHelper;
import com.a2do.list.a2do.fragment.EditDialogFragment;
import com.a2do.list.a2do.models.ItemType;
import com.a2do.list.a2do.models.ToDoItem;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements EditDialogFragment.EditDialogListener{
    final static String intent_edit_item = "EDIT_ITEM";
    final static String intent_edit_item_position = "ITEM_POSITION";
    final static int REQUEST_CODE = 1;
    final static int RESULT_CODE = 2;
    final static int NEW_ITEM_ID = -1;
    DatabaseHelper database;
    ArrayList<ToDoItem> items;
    ListAdapter itemsAdapter;
    ListView lvItems;
    EditText newItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = DatabaseHelper.getInstance(this);
        populateListItems();

        newItemText = (EditText) findViewById(R.id.etNewItem);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseHelper database = DatabaseHelper.getInstance(MainActivity.this);
                int result = database.deleteTodoItem(items.get(position));
                if(result > 0) {
                    items.remove(position);
                    itemsAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DisplayDialogFragment("Edit To-do item", items.get(position));
                /*Intent launchEditActivty = new Intent(MainActivity.this, EditActivity.class);
                launchEditActivty.putExtra(intent_edit_item, items.get(position).get_activity_name());
                launchEditActivty.putExtra(intent_edit_item_position, position);
                startActivityForResult(launchEditActivty, REQUEST_CODE);*/
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == resultCode && data != null) {
            String editedItem = data.getStringExtra(MainActivity.intent_edit_item);
            int position = data.getIntExtra(MainActivity.intent_edit_item_position, 0);
            ToDoItem item = items.get(position);
            item.set_activity_name(editedItem);
            items.set(position, item);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    private void populateListItems() {
        items = new ArrayList<ToDoItem>();
        database.getTodoItems(items);
        itemsAdapter = new ListAdapter(this, items);
    }

    public void OnAddItems(View view) {
        long result = 0;
        String newItem = newItemText.getText().toString();
        if (newItem.isEmpty()) {
            Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseHelper db = DatabaseHelper.getInstance(this);
            Date d = new Date();
            ToDoItem item = new ToDoItem(NEW_ITEM_ID, "TASK NOTES", "PENDING", "low", newItem, d, ItemType.ITEM_TYPE_NEW);
            if ((result = db.addorUpdateTodoItem(item)) != -1) {
                item.set_id((int) result);
                items.add(item);
            }
            newItemText.setText("");
            itemsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_item:
                DisplayDialogFragment("Add new To-do task ");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void DisplayDialogFragment(String title) {
        FragmentManager fm = getSupportFragmentManager();
        EditDialogFragment editNameDialogFragment = EditDialogFragment.newInstance(title);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    private void DisplayDialogFragment(String title, ToDoItem item) {
        FragmentManager fm = getSupportFragmentManager();
        EditDialogFragment editNameDialogFragment = EditDialogFragment.newInstance(title);
        Bundle bundle = editNameDialogFragment.getArguments();
        bundle.putString(DatabaseHelper.PROP_TASK_NOTES,item.get_task_notes());
        bundle.putString(DatabaseHelper.PROP_ACTIVITY_NAME,item.get_activity_name());
        bundle.putString(DatabaseHelper.PROP_DUE_DATE,item.get_dueDate().toString());
        bundle.putString(DatabaseHelper.PROP_STATUS,item.get_status());
        bundle.putString(DatabaseHelper.PROP_ACTIVITY_NUMBER,String.valueOf(item.get_id()));
        bundle.putString(DatabaseHelper.UTIL_ITEM_TYPE,String.valueOf(ItemType.ITEM_TYPE_EXIST));
        editNameDialogFragment.setArguments(bundle);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void onFinishDialog(ToDoItem item) {
        Toast.makeText(this,"Yes",Toast.LENGTH_LONG).show();
    }
}
