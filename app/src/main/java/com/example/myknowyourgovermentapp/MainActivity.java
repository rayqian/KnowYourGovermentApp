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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recyclerView = findViewById(R.id.recycler);
        myAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        officialList.clear();
        //reading json file in onCreate
        //readJSONData();
        //add dummy data
//        Official a = new Official("president", "Obama", "Dom");
//        Official b = new Official("vice president", "Baiden", "Dom");
//        officialList.add(a);
//        officialList.add(b);

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
        } else {
            setLocation();
        }

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
//        String pre_url = "http://www.marketwatch.com/investing/stock/";
//        String url = pre_url + stock.getStockSymbol();
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse(url));
//        startActivity(i);
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

    //accept result from SearchStockRunnable
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
        }

    }

    public void downloadFailed() {
        officialList.clear();
    }

//    public void handleResultShow(){
//        Toast.makeText(this, "handle result received " + officialList.size() + " officials", Toast.LENGTH_SHORT).show();
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Make a selection");
//
//        //create the String[] to display the search result to user
//        final CharSequence[] sArray = new CharSequence[searchResult.size()];
//        for (int i = 0; i < searchResult.size(); i++){
//            sArray[i] = searchResult.get(i).getSymbolwithName();
//        }
//        builder.setItems(sArray, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //add the selection stock to main page
//                validateAdd(searchResult.get(which));
//                updatePrice();
//            }
//        });
//
//        builder.setNegativeButton("Nevermind", null);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull
            String[] permissions, @NonNull
                    int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                setLocation();
                return;
            }
        }
        ((TextView) findViewById(R.id.location)).setText("No permission");

    }

    @SuppressLint("MissingPermission")
    private void setLocation() {
        //best provider: gps, network or passive
        String bestProvider = locationManager.getBestProvider(criteria, true);
//        ((TextView) findViewById(R.id.location)).setText(bestProvider);

        Location currentLocation = null;
        if (bestProvider != null) {
            currentLocation = locationManager.getLastKnownLocation(bestProvider);
        }
        if (currentLocation != null) {
            Geocoder geocoder = new Geocoder(this);
            try{
                List<Address> adrs = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                StringBuilder sb = new StringBuilder();
                Address adr = adrs.get(0);
                sb.append(adr.getLocality()).append(", ").append(adr.getAdminArea()).append(" ").append(adr.getPostalCode());
                ((TextView) findViewById(R.id.location)).setText(sb.toString());
            }catch (IOException e){
                e.printStackTrace();
            }
        } else {
            ((TextView) findViewById(R.id.location)).setText("Location Unavailable");
        }




    }
}