package com.example.nearby.criminalintent.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.nearby.criminalintent.activity.CrimePagerActivity;
import com.example.nearby.criminalintent.adapter.CrimeAdapter;
import com.example.nearby.criminalintent.model.Crime;
import com.example.nearby.criminalintent.model.CrimeLab;
import com.example.nearby.criminalintent.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeListFragment extends Fragment {


    CrimeAdapter mCrimeAdapter;
    public static final String CRIME_ID = "crime_id";
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private Button mAddCrimeBtn;
    private LinearLayout llFragmentListCrime;

    Crime mCrime;
    int position;

    RecyclerView mRecyclerView;
    public CrimeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_crime_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.crimeList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAddCrimeBtn = (Button) view.findViewById(R.id.addCrime);
        llFragmentListCrime = (LinearLayout) view.findViewById(R.id.llFragmentListCrime);
        mAddCrimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCrime();
            }
        });




        updateUI();

        return view;
    }


    public void updateUI(){

        List<Crime> crimes = CrimeLab.get(getActivity()).getCrimes();

        if(crimes.size() == 0){
            llFragmentListCrime.setVisibility(View.VISIBLE);
        }else{
            llFragmentListCrime.setVisibility(View.GONE);
        }

        if(mCrimeAdapter == null){
            mCrimeAdapter = new CrimeAdapter(getActivity(),crimes);
            mCrimeAdapter.setOnCrimeClickListener(new CrimeAdapter.onCrimeClickListener() {
                @Override
                public void onCrimeSelected(Crime crime, int position) {
                    mCrime = crime;
                    CrimeListFragment.this.position = position;
                    Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                    startActivity(intent);

                }
            });

            mRecyclerView.setAdapter(mCrimeAdapter);
        }else{
           // if(mCrime != null && position >= 0)
                mCrimeAdapter.setCrimes(crimes);
                mCrimeAdapter.notifyDataSetChanged();
        }

        updateSubtitle();

    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

        MenuItem subtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if(!mSubtitleVisible){
            subtitle.setTitle(R.string.show_subtitle);
        }else{
            subtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                addCrime();
                return true;
            case R.id.menu_item_show_subtitle:
                 mSubtitleVisible = !mSubtitleVisible;
                 getActivity().invalidateOptionsMenu();
                 updateSubtitle();
                 return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    private void addCrime() {
        Crime crime = new Crime();
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        crimeLab.addCrime(crime);
        Intent intent
                = CrimePagerActivity.
                newIntent(getActivity(),crime.getId());
        startActivity(intent);
    }

    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int count = crimeLab.getCrimes().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural,count,count);

        if(!mSubtitleVisible){
            subtitle = "Criminal Intent";
        }

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setTitle(subtitle);
    }
}
