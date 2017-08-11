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

import com.a2do.list.a2do.R;
import com.a2do.list.a2do.database.DatabaseHelper;
import com.a2do.list.a2do.models.ItemType;
import com.a2do.list.a2do.models.ToDoItem;

import java.util.Date;

public class EditDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TITLE = "title";
    Spinner mPrioritySpinner, mStatusSpinner;
    DatePicker mDatePicker;
    Button mOkButton, mCancelButton;
    private String mTitle, mTaskName, mTaskNotes, mPriority, mStatus, mDate;
    EditDialogListener mListener;
    ItemType mItemType;

    private EditText mTaskEditText, mTaskNotesEditText;
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
        mListener = (EditDialogListener)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
            mTaskNotes = getArguments().getString(DatabaseHelper.PROP_TASK_NOTES,"");
            mTaskName  = getArguments().getString(DatabaseHelper.PROP_ACTIVITY_NAME,"");
            mDate      = getArguments().getString(DatabaseHelper.PROP_DUE_DATE,"");
            mPriority  = getArguments().getString(DatabaseHelper.PROP_STATUS,"");
            mStatus    = getArguments().getString(DatabaseHelper.PROP_ACTIVITY_NUMBER,"");
            mItemType  = ItemType.valueOf(getArguments().getString(DatabaseHelper.UTIL_ITEM_TYPE,ItemType.ITEM_TYPE_NEW.name()));
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
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        getDialog().setTitle(mTitle);

        if(mItemType == ItemType.ITEM_TYPE_EXIST)
        {
            mTaskNotesEditText.setText(mTaskNotes);
            mTaskEditText.setText(mTaskName);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.okButton) {
            Toast.makeText(getContext(), mStatus + " OK " + mPriority, Toast.LENGTH_SHORT).show();
            mListener.onFinishDialog(new ToDoItem());
            dismiss();

        } else if (v.getId() == R.id.cancelButton) {
            dismiss();
        }

    }

    public void dismiss()
    {
        if (getDialog().isShowing() && getDialog() != null) {
            getDialog().dismiss();
        }
    }
    public interface EditDialogListener {
        void onFinishDialog(ToDoItem item);
    }
}
