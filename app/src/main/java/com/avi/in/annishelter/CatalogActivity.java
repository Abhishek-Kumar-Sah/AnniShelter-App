package com.avi.in.annishelter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avi.in.annishelter.data.PetContract.PetEntry;

import com.avi.in.annishelter.data.PetDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    PetCursorAdapter petCursorAdapter;
    final int LODER_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);




        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        ListView petListView = findViewById(R.id.Pet_ListView);

        View emptyView = findViewById(R.id.empty_view);

        petListView.setEmptyView(emptyView);

        petCursorAdapter = new PetCursorAdapter(this,null);
        petListView.setAdapter(petCursorAdapter);

        getLoaderManager().initLoader(LODER_ID,null,this);


        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Uri uri = ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);
                Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);
                intent.setData(uri);
                startActivity(intent);
                Log.v("Passing uri ____","uri : " + uri.toString());
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertData();

                return true;

            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:

                if (petCursorAdapter.getCount() > 0)
                    showDeleteConfirmationDialog();
                else
                    Toast.makeText(this, "Nothing To Delete!", Toast.LENGTH_SHORT).show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertData(){

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME,"Toto");
        values.put(PetEntry.COLUMN_PET_BREED,"Terrier");
        values.put(PetEntry.COLUMN_PET_WEIGHT,7);
        values.put(PetEntry.COLUMN_PET_GENDER,PetEntry.GENDER_MALE);


        Uri newRowUri = getContentResolver().insert(PetEntry.CONTENT_URI,values);

        Log.v("CatalogActivity","New Uri ID : "+newRowUri);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {BaseColumns._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_WEIGHT,
                PetEntry.COLUMN_PET_GENDER};

        //Set uri
        Uri uri = PetEntry.CONTENT_URI;


        return new CursorLoader(this,
                uri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        petCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        petCursorAdapter.swapCursor(null);
    }



    private void showDeleteConfirmationDialog() {

        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_pets);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // User clicked the "Delete" button, so delete the pet.
                deleteAllPets();
            }
        });


        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pets in the database.
     */
    private void deleteAllPets() {


        int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);

            Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");

    }

}
