package com.bewtechnologies.gtone;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman  on 7/23/2015.
 */
public class SavedPlaces extends AppCompatActivity
{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<PlaceDetails> places;

    //for db
    private SQLiteDatabase gtone;
    private LocationDBHelper dbHelper;


    //save deleted place's name
    String del_place;
    usersetting delplace;
    String dringermode;
    String dplace_id;
    double dlatitude;
    double dlongitude;
    int dpos;

    String[] myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.my_recycler_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        Toast.makeText(SavedPlaces.this, "Swipe cards to delete!", Toast.LENGTH_SHORT).show();


        // specify an adapter (see also next example)


        initializeData(getApplicationContext());
        mAdapter = new CardAdapter(places);
        mRecyclerView.setAdapter(mAdapter);

       delplace = new usersetting();

        //Snackbar using https://github.com/nispok/snackbar

       final ActionClickListener actionListener = new ActionClickListener() {
            @Override
            public void onActionClicked(Snackbar snackbar) {
                //undoes the delete
                SettingsActivity save = new SettingsActivity();
                save.InsertInDb(dplace_id,del_place,dlatitude,dlongitude,dringermode,getApplicationContext());
                places.add(dpos,new PlaceDetails(del_place,dringermode));
                mAdapter.notifyDataSetChanged();
            }
        };

       // activity where it is displayed

       /* // Dismisses the Snackbar being shown, if any, and displays the new one
        SnackbarManager.show(
                Snackbar.with(getApplicationContext())
                        .text("Single-line snackbar"),SavedPlaces.this);*/



        //Swipe listener using https://github.com/brnunes/SwipeableRecyclerView

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    Snackbar.with(getApplicationContext()) // context
                                            .text("Place deleted.")
                                            .actionLabel("Undo")
                                            .actionColor(Color.RED)
                                            .actionListener(actionListener)// text to display
                                            .show(SavedPlaces.this);

                                    //save deleted item's details
                                    dpos=position;
                                    del_place =places.get(position).place_name;
                                    dringermode= places.get(position).place_ringermode;
                                    SavedelPlaceDetails(getApplicationContext(),del_place);
                                    System.out.println("Deleted place : "+ del_place);
                                    delplace.deleteplace(del_place, getApplicationContext());

                                    places.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Snackbar.with(getApplicationContext()) // context
                                            .text("Place deleted.")
                                            .actionLabel("Undo")
                                            .actionColor(Color.RED)
                                            .actionListener(actionListener)// text to display
                                            .show(SavedPlaces.this);

                                    //save deleted item's details
                                    dpos=position;
                                    del_place =places.get(position).place_name;
                                    dringermode= places.get(position).place_ringermode;
                                    SavedelPlaceDetails(getApplicationContext(),del_place);
                                    System.out.println("Deleted place : "+ del_place);
                                    delplace.deleteplace(del_place, getApplicationContext());

                                    places.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);



    }

    private void SavedelPlaceDetails(Context context, String del_place) {

        //Getting data from db
        dbHelper = new LocationDBHelper(context);
        gtone= dbHelper.getReadableDatabase();

        if(!(gtone.isDbLockedByCurrentThread())) {

            Cursor c = gtone.rawQuery("Select * from location where placename='" + del_place+"'", null);

            try {


                int i = 0;
                if (c != null && c.getCount() > 0)

                {
                    c.moveToFirst();
                    do {
                        dplace_id = c.getString(1);
                        dlatitude = c.getDouble(3);
                        dlongitude = c.getDouble(4);
                        dringermode = c.getString(5);
                    } while (c.moveToNext());


                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if ((c != null) && (gtone != null)) {
                    gtone.close();
                    c.close();
                }
            }
        }
    }


    private void initializeData(Context context){

        places= new ArrayList<>();
        //Getting data from db
        dbHelper = new LocationDBHelper(context);
        gtone= dbHelper.getReadableDatabase();

        if(!(gtone.isDbLockedByCurrentThread())) {

            Cursor c = gtone.rawQuery("Select placename,setting from location", null);

            try {


                int i = 0;
                if (c != null && c.getCount() > 0)

                {
                    c.moveToFirst();
                    do
                    {
                        places.add(new PlaceDetails(c.getString(0),c.getString(1)));

                    } while (c.moveToNext());
                }

            }

            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if((c!=null)&&(gtone!=null))
                {
                    gtone.close();
                    c.close();
                }
            }
        }






    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.savedplaces_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch(item.getItemId())
        {
            case R.id.add:
                Intent i = new Intent(SavedPlaces.this,MainActivity.class);
                startActivity(i);
                break;

            default:
                Toast.makeText(getApplicationContext(),"No actions here!",Toast.LENGTH_SHORT);

        }


        return super.onOptionsItemSelected(item);
    }
}
