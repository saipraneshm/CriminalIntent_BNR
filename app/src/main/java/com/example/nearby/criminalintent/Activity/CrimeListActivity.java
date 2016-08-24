package com.example.nearby.criminalintent.Activity;

import android.support.v4.app.Fragment;

import com.example.nearby.criminalintent.Fragment.CrimeListFragment;

/**
 * Created by sai pranesh on 23-Aug-16.
 */
public class CrimeListActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
