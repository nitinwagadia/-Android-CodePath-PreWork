package com.a2do.list.a2do;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    EditText editedTextView;
    Button submitButton;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editedTextView = (EditText) findViewById(R.id.item_edit_text);
        submitButton = (Button) findViewById(R.id.sumbit_button);
        Intent intent = getIntent();
        String itemName = intent.getStringExtra(MainActivity.intent_edit_item);
        position = intent.getIntExtra(MainActivity.intent_edit_item_position,0);
        editedTextView.setText(itemName);
    }

    public void Submit(View view) {

        String editedItem = editedTextView.getText().toString();
        if(!editedItem.isEmpty()) {
            Intent returnResultIntent = new Intent(EditActivity.this, MainActivity.class);
            returnResultIntent.putExtra(MainActivity.intent_edit_item,editedItem);
            returnResultIntent.putExtra(MainActivity.intent_edit_item_position,position);
            setResult(MainActivity.RESULT_CODE,returnResultIntent);
            finish();
        }
        else
        {
            Toast.makeText(this,"Item name cannot be Empty",Toast.LENGTH_LONG).show();
        }
    }
}
