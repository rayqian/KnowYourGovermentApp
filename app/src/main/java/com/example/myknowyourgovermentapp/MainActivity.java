package com.example.myknowyourgovermentapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{

    private RecyclerView recyclerView;
    private OfficialAdapter myAdapter;
    private final List<Official> officialList = new ArrayList<>();

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
        Official a = new Official("president", "Obama", "Dom");
        Official b = new Official("vice president", "Baiden", "Dom");
        officialList.add(a);
        officialList.add(b);

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
//                handleStockSearch(user_input);

            }
        });

        builder.setTitle("Enter a City, State or a Zip Code:");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}