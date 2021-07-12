package com.avi.in.annishelter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.avi.in.annishelter.data.PetContract.PetEntry;
import androidx.annotation.Nullable;

public class PetDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shelter.db";
    private static final int DATABASE_VERSION = 1;

    public PetDbHelper( Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String  CREATE_PETS_TABLE = "CREATE TABLE " + PetContract.PetEntry.TABLE_NAME
                + " (" + PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetEntry.COLUMN_PET_BREED + " TEXT, "
                + PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL,"
                + PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String DELETE_TABLE = "DROP TABLE IF EXISTS " + PetEntry.TABLE_NAME +";";

        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }
}
