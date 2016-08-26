package com.example.nearby.criminalintent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.nearby.criminalintent.model.Crime;
import com.example.nearby.criminalintent.R;
import com.example.nearby.criminalintent.model.CrimeLab;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sai pranesh on 23-Aug-16.
 */
public class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.CrimeHolder>{


    private List<Crime> mCrimes;
    private Context mContext;
    private onCrimeClickListener mOnCrimeClickListener;

    public CrimeAdapter(Context context, List<Crime> crimes){
        mCrimes = crimes;
        mContext = context;
    }

    public void setCrimes(List<Crime> crimes) {
        mCrimes = crimes;
    }

    @Override
    public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_crime,parent, false);
        CrimeHolder crimeHolder = new CrimeHolder(view);
        return crimeHolder;

    }

    @Override
    public void onBindViewHolder(CrimeHolder holder, int position) {
        Crime crime = mCrimes.get(position);
        holder.bindCrime(crime);
    }

    @Override
    public int getItemCount() {
        return mCrimes.size();
    }

    public class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        CheckBox mCheckBox;
        TextView mCrimeTitle;
        TextView mCrimeDate;
        Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.crimeIsSolved);
            mCrimeDate = (TextView) itemView.findViewById(R.id.crimeDate);
            mCrimeTitle = (TextView) itemView.findViewById(R.id.crimeTitle);
            mCheckBox.setOnCheckedChangeListener(this);
        }

        void bindCrime(Crime crime){
            mCrime = crime;
            mCrimeTitle.setText(mCrime.getTitle());
            mCrimeDate.setText(new Date().toString());
            mCheckBox.setChecked(mCrime.isSolved());

        }

        public void setCrimes(List<Crime> crimes){

        }
        @Override
        public void onClick(View view) {
            mOnCrimeClickListener.onCrimeSelected(mCrime,getAdapterPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            mCrimes.get(getAdapterPosition()).setSolved(b);
            CrimeLab.get(mContext).updateCrime(mCrimes.get(getAdapterPosition()));
        }
    }

    public interface onCrimeClickListener{
        void onCrimeSelected(Crime crime, int position);
    }

    public void setOnCrimeClickListener(onCrimeClickListener onCrimeClickListener) {
        mOnCrimeClickListener = onCrimeClickListener;
    }
}
