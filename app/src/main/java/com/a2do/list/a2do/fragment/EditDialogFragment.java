package com.a2do.list.a2do.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.a2do.list.a2do.MainActivity;
import com.a2do.list.a2do.R;
import com.a2do.list.a2do.database.DatabaseHelper;
import com.a2do.list.a2do.models.ItemType;
import com.a2do.list.a2do.models.ToDoItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.y;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.media.CamcorderProfile.get;
import static com.a2do.list.a2do.R.id.datePicker;

public class EditDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TITLE = "title";
    Spinner mPrioritySpinner, mStatusSpinner;
    DatePicker mDatePicker;
    Button mOkButton, mCancelButton;
    EditDialogListener mListener;
    ItemType mItemType;
    private String mTitle, mTaskName, mTaskNotes, mPriority, mStatus, mDate;
    private EditText mTaskEditText, mTaskNotesEditText;
    private  int m_item_id;

    public EditDialogFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditDialogFragment newInstance(String title) {
        EditDialogFragment fragment = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (EditDialogListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
            mTaskNotes = getArguments().getString(DatabaseHelper.PROP_TASK_NOTES, "");
            mTaskName = getArguments().getString(DatabaseHelper.PROP_ACTIVITY_NAME, "");
            mDate = getArguments().getString(DatabaseHelper.PROP_DUE_DATE, "");
            mPriority = getArguments().getString(DatabaseHelper.PROP_PRIORITY, "");
            mStatus = getArguments().getString(DatabaseHelper.PROP_STATUS, "");
            m_item_id = getArguments().getInt(DatabaseHelper.PROP_ACTIVITY_NUMBER, 0);
            mItemType = ItemType.valueOf(getArguments().getString(DatabaseHelper.UTIL_ITEM_TYPE, ItemType.ITEM_TYPE_NEW.name()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTaskEditText = (EditText) view.findViewById(R.id.taskNameEditText);
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        mTaskNotesEditText = (EditText) view.findViewById(R.id.taskNotesEditText);
        mPrioritySpinner = (Spinner) view.findViewById(R.id.prioritySpinner);
        mStatusSpinner = (Spinner) view.findViewById(R.id.statusSpinner);
        mOkButton = (Button) view.findViewById(R.id.okButton);
        mCancelButton = (Button) view.findViewById(R.id.cancelButton);
        mOkButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mStatusSpinner.setSelection(0);
        mPrioritySpinner.setSelection(0);

        mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mStatus = mStatusSpinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPriority = mPrioritySpinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getDialog().setTitle(mTitle);

        if (mItemType == ItemType.ITEM_TYPE_EXIST) {

            mTaskNotesEditText.setText(mTaskNotes);
            mTaskEditText.setText(mTaskName);

            ArrayList<String> array = new ArrayList<String>(Arrays.asList(getContext().getResources().getStringArray(R.array.priority_list)));
            mPrioritySpinner.setSelection(array.contains(mPriority) ? array.indexOf(mPriority) : 0);

            array = new ArrayList<String>(Arrays.asList(getContext().getResources().getStringArray(R.array.status_list)));
            mStatusSpinner.setSelection(array.contains(mStatus) ? array.indexOf(mStatus) : 0 );

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(mDate));
                int year = cal.get(Calendar.YEAR);
                int month =cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DATE);
                mDatePicker.updateDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.okButton) {
            Toast.makeText(getContext(), mStatus + " OK " + mPriority, Toast.LENGTH_SHORT).show();
            mTaskName = mTaskEditText.getText().toString();
            mTaskNotes = mTaskNotesEditText.getText().toString();

            int day = mDatePicker.getDayOfMonth();
            int month = mDatePicker.getMonth();
            int year = mDatePicker.getYear();

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            if (!mTaskName.isEmpty() && !mTaskNotes.isEmpty() && calendar!=null  &&
                    !mPriority.isEmpty() && !mStatus.isEmpty()) {

                ToDoItem item = new ToDoItem();
                item.set_dueDate(calendar.getTime());
                item.set_task_notes(mTaskNotes);
                item.set_activity_name(mTaskName);
                item.set_status(mStatus);
                item.set_priority(mPriority);
                if(mItemType == ItemType.ITEM_TYPE_EXIST) {
                    item.set_item_type(mItemType);
                    item.set_id(m_item_id);
                } else {
                    item.set_item_type(ItemType.ITEM_TYPE_NEW);
                }
                mListener.onFinishDialog(item);
                dismiss();
            }
            else {
                Toast.makeText(getContext(), "Some fields are empty", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.cancelButton) {
            dismiss();
        }

    }

    public void dismiss() {
        if (getDialog().isShowing() && getDialog() != null) {
            getDialog().dismiss();
        }
    }

    public interface EditDialogListener {
        void onFinishDialog(ToDoItem item);
    }
}
