package com.example.nearby.criminalintent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.nearby.criminalintent.fragment.CrimeFragment;
import com.example.nearby.criminalintent.model.Crime;
import com.example.nearby.criminalintent.model.CrimeLab;
import com.example.nearby.criminalintent.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by sai pranesh on 23-Aug-16.
 */
public class CrimePagerActivity extends AppCompatActivity {


    ViewPager mViewPager;
    List<Crime> mCrimes;
    UUID crimeID;

    public static final String EXTRA_CRIME_ID = "crime_id_extra";


    public static Intent newIntent(Context context, UUID crimeID){
        Intent intent = new Intent(context,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeID);
        return intent;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        mCrimes = CrimeLab.get(this).getCrimes();
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        crimeID = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for(int i = 0; i < mCrimes.size() ; i ++){
            if(mCrimes.get(i).getId().equals(crimeID)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
