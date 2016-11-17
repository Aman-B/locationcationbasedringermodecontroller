package com.bewtechnologies.gtone;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Aman  on 7/21/2015.
 */

public class SettingsActivity extends AppCompatActivity {

    LinearLayout setpage;
    ArrayAdapter<String> setadapter;
    ListView lv;
   static Button save;
    RadioButton rb;
    RadioGroup rg;
    public static String RINGER_MODE=null;

    //Database

    private LocationDBHelper dbHelper;
    private SQLiteDatabase gtone;

    //string
     public String place_name;
     public String place_id;

     double slongitude;
     double slatitude;




    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);


        Intent test= getIntent();
        place_id= test.getStringExtra("com.bewtechnologies.gtone.place_id");
        place_name=test.getStringExtra("com.bewtechnologies.gtone.place_name");
        slatitude= test.getDoubleExtra("com.bewtechnologies.gtone.slatitude",0);
        slongitude=test.getDoubleExtra("com.bewtechnologies.gtone.slongitude",0);



       /* ScrollView sv = (ScrollView) findViewById(R.id.scroll);
        sv.removeAllViews();

        DrawerLayout dv= new DrawerLayout(this);
        dv.setVisibility(dv.GONE);*/

        // setpage = (LinearLayout) findViewById(R.id.settings_page);

       /* if(setpage==null) {*/
          //  View contentView = getLayoutInflater().inflate(R.layout.settings_page, null, false);
            //mDrawerLayout.addView(contentView);
        //}

        setContentView(R.layout.settings_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        rg=(RadioGroup) findViewById(R.id.rbg);
        save = (Button) findViewById(R.id.savesetting);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int selection = rg.getCheckedRadioButtonId();
                //Log.i("Got rb id ?", "Here:" + selection);

                rb=(RadioButton) findViewById(selection);
                //Log.i("Got rb ?", "Here:" + rb);
                RINGER_MODE = rb.getText().toString();

            }
        });





        save.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {


                if (place_id != null && SettingsActivity.RINGER_MODE != null)
                {
                    InsertInDb(place_id, place_name, slatitude, slongitude, RINGER_MODE,getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Settings saved", Toast.LENGTH_SHORT).show();
                }



            }
        });
        /*else
        {
            save.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i("Got 2 ringer mode?", "Here:" + RINGER_MODE);
                    Toast.makeText(getApplicationContext(),"No radio btn selected", Toast.LENGTH_SHORT).show();
                }
            });

        }
*/



    }




    //insert in DB

    public  void InsertInDb(String place_id, String place_name, double slatitude, double slongitude, String RINGER_MODE,Context context) {

        //inserting values into db
        dbHelper = new LocationDBHelper(context);

        gtone = dbHelper.getWritableDatabase();

        String val= place_id+" "+place_name+" "+slatitude+" "+slongitude+" "+ RINGER_MODE;

        Log.i("Values : ", val);

        ContentValues values= new ContentValues();
        values.put(LocationContract.LocationEntry.COLUMN_NAME_PLACE_ID,place_id);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_PLACE_NAME,place_name);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_PLACE_LAT,slatitude);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_PLACE_LONG,slongitude);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_SETTING,RINGER_MODE);

        long newrowid;

        newrowid= gtone.insert(LocationContract.LocationEntry.TABLE_NAME, null,values);

        gtone.close();
        dbHelper.close();

    }

}
