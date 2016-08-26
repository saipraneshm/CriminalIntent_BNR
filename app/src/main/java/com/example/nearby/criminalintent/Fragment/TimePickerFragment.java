package com.example.nearby.criminalintent.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nearby.criminalintent.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sai pranesh on 24-Aug-16.
 */
public class TimePickerFragment extends DialogFragment {


    TimePicker mTimePicker;
    public static final String EXTRA_DATE = "com.example.nearby.criminalintent.Fragment.DATE";
    int mHour, mMinute, mDay, mMonth, mYear;

    public static Fragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE,date);

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(args);
        return timePickerFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time_picker,null);
        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_date_time_picker);
        Date date =(Date) getArguments().getSerializable(EXTRA_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMonth = calendar.get(Calendar.MONTH);
        mYear = calendar.get(Calendar.YEAR);

        Dialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                Toast.makeText(getActivity(),
                        hourOfDay + " " + minute ,
                        Toast.LENGTH_SHORT).show();
                Calendar calendar = Calendar.getInstance();
                calendar.set(mYear,mMonth,mDay,hourOfDay,minute);
                Date date = calendar.getTime();
                sendResult(Activity.RESULT_OK, date);
            }
        },mHour,mMinute,false);
        return timePickerDialog;

    }

    void sendResult(int resultCode,Date date){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);
        getTargetFragment().
                onActivityResult(getTargetRequestCode(),resultCode,intent);

    }
}
