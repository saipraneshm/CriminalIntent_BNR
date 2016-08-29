package com.example.nearby.criminalintent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nearby.criminalintent.model.Crime;
import com.example.nearby.criminalintent.model.CrimeLab;
import com.example.nearby.criminalintent.R;
import com.example.nearby.criminalintent.utils.PictureUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by sai pranesh on 16-Aug-16.
 */
public class CrimeFragment extends Fragment {


    private Crime mCrime;
    private EditText mCrimeTitle;
    private Button mCrimeDate,mCrimeTime;
    private CheckBox mIsCrimeSolved;
    private Button mSuspectButton;
    private Button mReportButton;
    private Button mCallSuspectButton;
    private UUID mCrimeId;
    private String mTelephoneNo;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private Boolean flag = false;
    private Callback mCallback;

    public interface Callback {
        void onCrimeUpdated(Crime crime);
        void onRemoveCrime(Crime crime);
    }


    private static final String CRIME_ID = "crime_id" ;
    private static final String DATE_PICKER_CRIME = "crime_date_picker";
    private static final String TIME_PICKER_CRIME = "crime_time_picker";
    private static final String CRIME_PHOTO_VIEW = "crime_photo_view";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (Callback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public void updateCrime(){
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallback.onCrimeUpdated(mCrime);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* if(savedInstanceState != null){
            mCrimeId = (UUID) savedInstanceState.getSerializable(CRIME_ID);
            mCrime = CrimeLab.get(getActivity())
                        .getCrime(mCrimeId);
        }*/

        mCrime = CrimeLab.
                get(getActivity()).
                getCrime((UUID) getArguments().
                        getSerializable(CRIME_ID));
        mCrimeId = mCrime.getId();
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        setHasOptionsMenu(true);
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
    public View onCreateView(LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        mCallSuspectButton = (Button) v.findViewById(R.id.crime_call_suspect);
        mCrimeTitle = (EditText)v.findViewById(R.id.crime_title);
        mCrimeTitle.setText(mCrime.getTitle());
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
                updateCrime();
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
                        (TimePickerFragment) TimePickerFragment.newInstance(mCrime.getId());
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timePickerFragment.show(manager,TIME_PICKER_CRIME);
            }
        });

        mIsCrimeSolved = (CheckBox) v.findViewById(R.id.crime_solved);
        mIsCrimeSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
                updateCrime();
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


        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);


        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendReport = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setChooserTitle(getString(R.string.send_report))
                        .createChooserIntent();
                startActivity(sendReport);
                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                startActivity(intent);*/
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //Just to test whether the PackageManager disables the button or not.
        //pickContact.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });


        if(mCrime.getSuspect() != null){
            mSuspectButton.setText(getString(R.string.current_suspect,mCrime.getSuspect()));
            mCallSuspectButton.setText(getString(R.string.call_suspect,mCrime.getSuspect()));
            mCallSuspectButton.setVisibility(View.VISIBLE);
        }


        mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTelephoneNo != null){
                    Uri callSuspectUri = Uri.parse("tel:" + mTelephoneNo);
                    Intent callSuspect = new Intent(Intent.ACTION_DIAL,callSuspectUri);
                    startActivity(callSuspect);
                }
            }
        });
        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null){
            mSuspectButton.setEnabled(false);
            mCallSuspectButton.setVisibility(View.GONE);
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);

        ViewTreeObserver observer = mPhotoView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
               flag = true;
            }
        });
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mPhotoFile != null && mPhotoFile.exists()) {
                    FragmentManager fm = getFragmentManager();
                    ImageViewDialogFragment imageViewDialogFragment =
                            (ImageViewDialogFragment)
                                    ImageViewDialogFragment.newInstance(mCrime.getId());
                    imageViewDialogFragment.show(fm, CRIME_PHOTO_VIEW);
                }
            }
        });

        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null
                && cameraIntent.resolveActivity(packageManager) != null;


            mPhotoButton.setEnabled(canTakePhoto);

        if(canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(cameraIntent,REQUEST_PHOTO);
            }
        });

        if(flag){
            int destHeight = mPhotoView.getMaxHeight();
            int destWidth  = mPhotoView.getMaxWidth();
            updatePhotoView(destWidth,destHeight);
        }


        return v;
    }


     private void updatePhotoView(int width,int height){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageBitmap(null);
        }else{
            mPhotoView.setImageBitmap
                    (PictureUtils.
                            getScaledBitmap
                                    (mPhotoFile.getPath(),width,height));
        }
    }

    private String getCrimeReport(){
        String solvedString = null;
        if(mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else{
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat
                            .format(dateFormat, mCrime.getDate())
                            .toString();

        String suspect = mCrime.getSuspect();
        if( suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else{
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report,
                        mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    private void updateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        calendar.setTime(mCrime.getDate());
        mCrimeTime.setText(String.valueOf(simpleDateFormat.format(calendar.getTime())));
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            Log.d("CrimeFragment",date.toString() + ": Crime Date");
            mCrime.setDate(date);
            updateCrime();
            updateDate();
        }else if (requestCode == REQUEST_TIME){
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_CRIME_ID);
            Log.d("CrimeFragment",date.toString());
            mCrime.setDate(date);
            updateCrime();
            updateTime();
            updateDate();
        }else if(requestCode == REQUEST_CONTACT && data != null){

            Uri contactUri = data.getData();
            String[] queryString = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts._ID
            };
            Cursor c = getActivity().getContentResolver()
                       .query(contactUri,queryString,null,null,null);

            try{
                if(c != null && c.getCount() == 0){
                    return;
                }

                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(getString(R.string.current_suspect,suspect));

                Cursor getPhoneNumber = getActivity().getContentResolver()
                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                                ,new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ? ",
                                new String[]{c.getString(1)},null);
                if(getPhoneNumber != null && getPhoneNumber.getCount() != 0){
                    getPhoneNumber.moveToFirst();
                    mTelephoneNo = getPhoneNumber.getString(0);
                    mCallSuspectButton.setText(getString(R.string.call_suspect,mCrime.getSuspect()));
                    mCallSuspectButton.setVisibility(View.VISIBLE);
                }
                updateCrime();
            }finally {
                c.close();
            }
        }else if (requestCode == REQUEST_PHOTO){

            if(flag){
                int destHeight = mPhotoView.getMaxHeight();
                int destWidth  = mPhotoView.getMaxWidth();
                updatePhotoView(destWidth,destHeight);
                updateCrime();
            }

           // updatePhotoView();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(R.id.menu_item_remove_crime):
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                crimeLab.removeCrime(mCrime);
                mCallback.onRemoveCrime(mCrime);
                //getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CRIME_ID,mCrimeId);
    }

    private void updateDate() {
        mCrimeDate.setText(mCrime.getDate().toString());
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }
}
