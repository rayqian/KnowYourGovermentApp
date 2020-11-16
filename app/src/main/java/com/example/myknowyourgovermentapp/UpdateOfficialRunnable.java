package com.example.myknowyourgovermentapp;

import android.net.Uri;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UpdateOfficialRunnable implements Runnable {

    private static final String TAG = "UpdateOfficialRunnable";
    private MainActivity mainActivity;
    private String postal_code;
    private static final String DATA_URL = "https://www.googleapis.com/civicinfo/v2/representatives?";
    private static final String API_Key = "key=AIzaSyBadkTYHFTAZFronpXiIIDvnVYlisGzXcM";
    private static final String ADDR_URL = "&address=";

    UpdateOfficialRunnable(MainActivity ma, String input){
        this.mainActivity = ma;
        this.postal_code = input;
    }

    @Override
    public void run() {
        Uri dataUri = Uri.parse(DATA_URL + API_Key + ADDR_URL + postal_code);
        String formatedURL = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(formatedURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                handleResults(null);
                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "run: " + sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        handleResults(sb.toString());
    }

    private void handleResults(String s) {
        if (s == null) {
            Log.d(TAG, "handleResults: Failure in data download");
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.downloadFailed();
                }
            });
            return;
        }

        final String location = parseJSONloc(s);
        final ArrayList<Official> officialList = parseJSONofficial(s);

//        mainActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (officialList != null)
//                    mainActivity.acceptResult(officialList);
//            }
//        });
    }

    private String parseJSONloc(String s){
        String res = "";
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONObject normalized_info = jObjMain.getJSONObject("normalizedInput");
            res = normalized_info.get("city").toString() + ", " + normalized_info.get("state").toString() + " " + normalized_info.get("zip").toString();
        } catch (Exception e) {
            Log.d(TAG, "parseJSON location: " + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    private ArrayList<Official> parseJSONofficial(String s) {
        ArrayList<Official> OList = new ArrayList<>();
        try {
//            JSONArray jObjMain = new JSONArray(s);
            JSONObject jObjMain = new JSONObject(s);
            JSONObject offices_info = jObjMain.getJSONObject("offices");
            JSONObject officials_info = jObjMain.getJSONObject("officials");



//            for (int i = 0; i < jObjMain.length(); i++) {
//                JSONObject jStock = (JSONObject) jObjMain.get(i);
//                String symbol = jStock.getString("symbol");
//                String name = jStock.getString("name");
//                if(symbol.indexOf(key) != -1 || name.indexOf(key) != -1){
//                    stockList.add(new Stock(symbol,name));
//                }
//            }
            return OList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
