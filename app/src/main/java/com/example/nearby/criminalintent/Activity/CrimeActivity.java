package com.example.nearby.criminalintent.activity;

import android.support.v4.app.Fragment;

import com.example.nearby.criminalintent.fragment.CrimeFragment;
import com.example.nearby.criminalintent.fragment.CrimeListFragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        UUID crimeID = (UUID) getIntent().getSerializableExtra(CrimeListFragment.CRIME_ID);
        return CrimeFragment.newInstance(crimeID);
    }
}
