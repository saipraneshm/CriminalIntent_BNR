package com.example.nearby.criminalintent.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.TimeUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.nearby.criminalintent.Model.Crime;
import com.example.nearby.criminalintent.Model.CrimeLab;
import com.example.nearby.criminalintent.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.UUID;

/**
 * Created by sai pranesh on 16-Aug-16.
 */
public class CrimeFragment extends Fragment {


    Crime mCrime;
    EditText mCrimeTitle;
    Button mCrimeDate,mCrimeTime;
    CheckBox mIsCrimeSolved;

    private static final String CRIME_ID = "crime_id" ;
    private static final String DATE_PICKER_CRIME = "crime_date_picker";
    private static final String TIME_PICKER_CRIME = "crime_time_picker";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCrime = CrimeLab.get(getActivity()).getCrime((UUID) getArguments().getSerializable(CRIME_ID));

        setRetainInstance(true);
    }

    public static Fragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID,crimeId);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        mCrimeTitle = (EditText)v.findViewById(R.id.crime_title);
        mCrimeTitle.setText(mCrime.getTitle());
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCrimeDate = (Button) v.findViewById(R.id.crime_date_button);
        mCrimeTime = (Button) v.findViewById(R.id.crime_time_button);


        updateTime();

        mCrimeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment timePickerFragment =
                        (TimePickerFragment) TimePickerFragment.newInstance(mCrime.getDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timePickerFragment.show(manager,TIME_PICKER_CRIME);
            }
        });

        mIsCrimeSolved = (CheckBox) v.findViewById(R.id.crime_solved);
        mIsCrimeSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });
        updateDate();
        mCrimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment datePickerFragment =
                        (DatePickerFragment) DatePickerFragment.newInstance(mCrime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(manager,DATE_PICKER_CRIME);
            }
        });
        mIsCrimeSolved.setChecked(mCrime.isSolved());
        return v;
    }

    private void updateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        calendar.setTime(mCrime.getDate());
        mCrimeTime.setText(String.valueOf(simpleDateFormat.format(calendar.getTime())));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }else if (requestCode == REQUEST_TIME){
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateTime();
            updateDate();
        }
    }

    private void updateDate() {
        mCrimeDate.setText(mCrime.getDate().toString());
    }
}
