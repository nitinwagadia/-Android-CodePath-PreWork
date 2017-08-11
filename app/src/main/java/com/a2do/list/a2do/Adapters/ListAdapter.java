package com.a2do.list.a2do.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a2do.list.a2do.R;
import com.a2do.list.a2do.models.ToDoItem;

import java.util.ArrayList;

/**
 * Created by Nitin on 8/9/2017.
 */


public class ListAdapter extends ArrayAdapter<ToDoItem> {

    public ListAdapter(Context context, ArrayList<ToDoItem> items) {
        super(context, R.layout.list_item, items);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewholder = null;

        if (convertView == null) {
            viewholder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewholder.itemNameTextView = (TextView) convertView.findViewById(R.id.itemName);
            viewholder.priorityTextView = (TextView) convertView.findViewById(R.id.priority);
            viewholder.listItemView = (LinearLayout) convertView.findViewById(R.id.listItemView);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        ToDoItem item = getItem(position);
        viewholder.itemNameTextView.setText(item.get_activity_name());
        viewholder.priorityTextView.setText(item.get_priority());
        if (item.get_status().equalsIgnoreCase("low"))
            viewholder.priorityTextView.setBackgroundResource(android.R.color.holo_green_light);
        else if (item.get_status().equalsIgnoreCase("medium"))
            viewholder.priorityTextView.setBackgroundResource(android.R.color.holo_blue_bright);
        else
            viewholder.priorityTextView.setBackgroundResource(android.R.color.holo_red_light);

        /*viewholder.listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        return convertView;
    }

    private static class ViewHolder {
        TextView priorityTextView;
        TextView itemNameTextView;
        LinearLayout listItemView;
    }


}
