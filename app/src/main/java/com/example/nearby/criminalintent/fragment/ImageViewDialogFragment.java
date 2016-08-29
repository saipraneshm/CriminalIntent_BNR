package com.example.nearby.criminalintent.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.ImageView;


import com.example.nearby.criminalintent.R;
import com.example.nearby.criminalintent.model.Crime;
import com.example.nearby.criminalintent.model.CrimeLab;

import java.io.File;
import java.util.UUID;

/**
 * Created by sai pranesh on 28-Aug-16.
 */
public class ImageViewDialogFragment extends DialogFragment {


    private UUID mCrimeId;
    private static final String EXTRA_CRIME_ID = "crimeId";

    public static Fragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID,crimeId);

        ImageViewDialogFragment fragment = new ImageViewDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mCrimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        Crime crime = CrimeLab.get(getActivity()).getCrime(mCrimeId);
        ImageView imgView = new ImageView(getActivity());

        File photoFile  = CrimeLab.get(getActivity()).getPhotoFile(crime);
        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getPath());
        imgView.setImageBitmap(bitmap);

        Dialog imageDialog = new AlertDialog.Builder(getActivity())
                                    .setView(imgView)
                                    .setTitle(R.string.crime_image_title)
                                    .setPositiveButton(android.R.string.ok, null)
                                    .create();

        return imageDialog;

    }

}
