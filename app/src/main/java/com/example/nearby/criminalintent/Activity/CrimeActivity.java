package com.example.nearby.criminalintent.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.example.nearby.criminalintent.Fragment.CrimeFragment;
import com.example.nearby.criminalintent.Fragment.CrimeListFragment;
import com.example.nearby.criminalintent.R;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        UUID crimeID = (UUID) getIntent().getSerializableExtra(CrimeListFragment.CRIME_ID);
        return CrimeFragment.newInstance(crimeID);
    }
}
