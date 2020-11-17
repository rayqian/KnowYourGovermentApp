package com.example.myknowyourgovermentapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


//API_KEY = AIzaSyBadkTYHFTAZFronpXiIIDvnVYlisGzXcM

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{

    private RecyclerView recyclerView;
    private OfficialAdapter myAdapter;
    private final List<Official> officialList = new ArrayList<>();

    private LocationManager locationManager;
    private Criteria criteria;
    private static int MY_LOCATION_REQUEST_CODE_ID = 111;

    private static final String TAG = "from MainActivity";

    public static String loc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        myAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        officialList.clear();

        //location code
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    MY_LOCATION_REQUEST_CODE_ID);
        }
        else{
            Address cur_add = setLocation();
            StringBuilder sb = new StringBuilder();
            String address;
            if(cur_add.getPostalCode() == null){
                sb.append(cur_add.getLocality()).append(", ").append(cur_add.getAdminArea()).append(" ");
                address = cur_add.getLocality();;
            }
            else{
                sb.append(cur_add.getLocality()).append(", ").append(cur_add.getAdminArea()).append(" ").append(cur_add.getPostalCode());
                address = cur_add.getPostalCode();;
            }
            ((TextView)findViewById(R.id.location)).setText(sb.toString());

            //loading data
            UpdateOfficialRunnable uor = new UpdateOfficialRunnable(this, address);
            new Thread(uor).start();

    }


    }

    @Override
    protected void onPause() {
        super.onPause();
//        writeJSONData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                handleSearchLocate();
                return true;
            case R.id.menu_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Official official = officialList.get(pos);

        Toast.makeText(this, "toast from onClick method for " + official.getName(), Toast.LENGTH_SHORT ).show();
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra(Official.class.getName(), official);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bye, Thanks for using my app!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    public void handleSearchLocate(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = new EditText(this);

        et.setGravity(Gravity.CENTER_HORIZONTAL);

        builder.setView(et);

        builder.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String user_input = et.getText().toString();
                handleLocateSearch(user_input);
            }
        });
        builder.setTitle("Enter a City, State or a Zip Code:");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void handleLocateSearch(String input){
        if(input.isEmpty()){
            return;
        }

        //create new thread to handle locate search
        UpdateOfficialRunnable uor = new UpdateOfficialRunnable(this, input);
        new Thread(uor).start();
    }

    //accept location result from SearchStockRunnable
    public void acceptLocResult(String location){

        //if no result from runnable
        if(location == null || location.equals("")){
            ((TextView) findViewById(R.id.location)).setText("no location info");
        }

        //if there are results from runnable
        else{
            loc = location;
            ((TextView) findViewById(R.id.location)).setText(location);
        }
    }

    //accept officials result from UpdateOfficialRunnable
    public void acceptResult(ArrayList<Official> officials){
        officialList.clear();
        //if no result from runnable
        if(officials.size() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Officials Data for the location is not found.");
            builder.setTitle("Data Not Found");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        //if there are results from runnable
        else{
            officialList.addAll(officials);
            //the official list should show up automatically
            //handleResultShow();
            myAdapter.notifyDataSetChanged();
        }
        Toast.makeText(this, "result received " + officials.size() + " officials", Toast.LENGTH_SHORT).show();

    }

    public void downloadFailed() {
        Toast.makeText(this, "no result received", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull
            String[] permissions, @NonNull
                    int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {

                Address cur_add = setLocation();
                StringBuilder sb = new StringBuilder();
                String address;
                if(cur_add.getPostalCode() == null){
                    sb.append(cur_add.getLocality()).append(", ").append(cur_add.getAdminArea()).append(" ");
                    address = cur_add.getLocality();;
                }
                else{
                    sb.append(cur_add.getLocality()).append(", ").append(cur_add.getAdminArea()).append(" ").append(cur_add.getPostalCode());
                    address = cur_add.getPostalCode();;
                }
                ((TextView)findViewById(R.id.location)).setText(sb.toString());

                //loading data
                UpdateOfficialRunnable uor = new UpdateOfficialRunnable(this, address);
                new Thread(uor).start();
                return;
            }
        }
        ((TextView) findViewById(R.id.location)).setText("No permission");

    }

    @SuppressLint("MissingPermission")
    private Address setLocation() {
        //best provider: gps, network or passive
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location currentLocation = null;
        if (bestProvider != null) {
            currentLocation = locationManager.getLastKnownLocation(bestProvider);
        }
        if (currentLocation != null) {
            Geocoder geocoder = new Geocoder(this);
            try{
                List<Address> adrs = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                Address adr = adrs.get(0);
                return adr;
            }catch (IOException e){
                e.printStackTrace();
            }
        } else {
            ((TextView) findViewById(R.id.location)).setText("Location Unavailable");
        }
        return null;
    }

}