package com.example.nearby.criminalintent.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nearby.criminalintent.R;
import com.example.nearby.criminalintent.model.Crime;
import com.example.nearby.criminalintent.model.CrimeLab;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by sai pranesh on 24-Aug-16.
 */
public class TimePickerFragment extends DialogFragment {


    TimePicker mTimePicker;
    public static final String EXTRA_CRIME_ID = "com.example.nearby.criminalintent.Fragment.CRIME_ID";
    int mHour, mMinute, mDay, mMonth, mYear;
    UUID mCrimeId;

    public static Fragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID,crimeId);

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(args);
        return timePickerFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time_picker,null);
        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_date_time_picker);
        mCrimeId =(UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        Log.d("TimePicker",CrimeLab.get(getActivity()).getCrime(mCrimeId).getDate() + " ");
        Date date = CrimeLab.get(getActivity()).getCrime(mCrimeId).getDate();
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
        intent.putExtra(EXTRA_CRIME_ID,date);
        getTargetFragment().
                onActivityResult(getTargetRequestCode(),resultCode,intent);

    }
}
