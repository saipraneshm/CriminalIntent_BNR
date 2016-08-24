package com.example.nearby.criminalintent.Fragment;



import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.example.nearby.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by sai pranesh on 24-Aug-16.
 */
public class DatePickerFragment extends DialogFragment {

    private DatePicker mDatePicker;
    private Date mDate;
    public static final String EXTRA_DATE = "com.example.nearby.criminalintent.Fragment.date";

    public static Fragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE,date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(args);
        return datePickerFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null);
        mDatePicker = (DatePicker)v.findViewById(R.id.dialog_date_date_picker);
        mDate = (Date) getArguments().getSerializable(EXTRA_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year,month,date, null);

        Dialog dialog = new AlertDialog.Builder(getActivity())
                            .setView(v)
                            .setTitle(R.string.date_picker)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int year = mDatePicker.getYear();
                                    int month = mDatePicker.getMonth();
                                    int day = mDatePicker.getDayOfMonth();
                                    Date date = new GregorianCalendar(year,month,day).getTime();
                                    sendResult(Activity.RESULT_OK,date);
                                }
                            })
                            .create();
        return dialog;

    }

    public void sendResult(int resultCode, Date date ){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
