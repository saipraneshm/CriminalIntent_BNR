package com.example.nearby.criminalintent.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.nearby.criminalintent.R;
import com.example.nearby.criminalintent.fragment.CrimeFragment;
import com.example.nearby.criminalintent.fragment.CrimeListFragment;
import com.example.nearby.criminalintent.model.Crime;
import com.example.nearby.criminalintent.model.CrimeLab;

/**
 * Created by sai pranesh on 23-Aug-16.
 */
public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.Callback{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_master_detail;
    }

    @Override
    public void OnCrimeSelected(Crime crime) {
        if(findViewById(R.id.detail_fragment_view)== null){
            Intent crimePagerActivity = CrimePagerActivity.newIntent(this,crime.getId());
            startActivity(crimePagerActivity);
        }else{
            FragmentManager fm = getSupportFragmentManager();
            CrimeFragment crimeFragment = (CrimeFragment) CrimeFragment.newInstance(crime.getId());
            fm.beginTransaction()
                    .replace(R.id.detail_fragment_view,crimeFragment).commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment crimeListFragment = (CrimeListFragment) getSupportFragmentManager().
                                findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUI();
    }

    @Override
    public void onRemoveCrime(Crime crime) {
        CrimeListFragment crimeListFragment = (CrimeListFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUI();
        FragmentManager fm = getSupportFragmentManager();
        if(CrimeLab.get(this).getCrimes().size() > 0){
            Crime firstCrime = CrimeLab.get(this).getCrimes().get(0);
            CrimeFragment crimeFragment = (CrimeFragment) CrimeFragment.newInstance(firstCrime.getId());
            fm.beginTransaction()
                    .replace(R.id.detail_fragment_view,crimeFragment).commit();
        }else{
            CrimeFragment crimeFragment = (CrimeFragment) fm.findFragmentById(R.id.detail_fragment_view);
            fm.beginTransaction()
                    .remove(crimeFragment).commit();
        }


    }
}
