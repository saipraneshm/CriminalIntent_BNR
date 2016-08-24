package com.example.nearby.criminalintent.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nearby.criminalintent.Activity.CrimeActivity;
import com.example.nearby.criminalintent.Activity.CrimePagerActivity;
import com.example.nearby.criminalintent.Adapter.CrimeAdapter;
import com.example.nearby.criminalintent.Model.Crime;
import com.example.nearby.criminalintent.Model.CrimeLab;
import com.example.nearby.criminalintent.R;

import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeListFragment extends Fragment {


    CrimeAdapter mCrimeAdapter;
    public static final String CRIME_ID = "crime_id";

    Crime mCrime;
    int position;

    RecyclerView mRecyclerView;
    public CrimeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_crime_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.crimeList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }


    public void updateUI(){

        List<Crime> crimes = CrimeLab.get(getActivity()).getCrimes();

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
            if(mCrime != null && position >= 0)
                mCrimeAdapter.notifyDataSetChanged();
        }



    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
