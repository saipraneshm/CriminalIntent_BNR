package com.example.nearby.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.nearby.criminalintent.model.Crime;

import java.util.Date;
import java.util.UUID;


/**
 * Created by sai pranesh on 25-Aug-16.
 */
public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime(){
        String uuidString = getString(getColumnIndex(CrimeDBSchema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDBSchema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDBSchema.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeDBSchema.CrimeTable.Cols.SOLVED));
        String contact = getString(getColumnIndex(CrimeDBSchema.CrimeTable.Cols.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(contact);

        return crime;
    }
}
