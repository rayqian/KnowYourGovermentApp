package com.example.myknowyourgovermentapp;

import android.net.Uri;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Switch;

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

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(location != null){
                    mainActivity.acceptLocResult(location);
                }
                if (officialList != null){
                    mainActivity.acceptResult(officialList);
                }
            }
        });

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

            JSONObject jObjMain = new JSONObject(s);
            JSONArray offices_info = jObjMain.getJSONArray("offices");
            JSONArray officials_info = jObjMain.getJSONArray("officials");

            //loop through the officials_info, create official obj for each entry, put them into the arraylist in order
            for(int i = 0; i < officials_info.length(); i++){
                JSONObject jOfficial =  (JSONObject) officials_info.get(i);
                Official temp = new Official();

                temp.setName(jOfficial.getString("name"));
                temp.setParty(jOfficial.getString("party"));
                if(jOfficial.has("photoUrl")){
                    temp.setPhoto_url(jOfficial.getString("photoUrl"));
                }
                if(jOfficial.has("phones")){
                    temp.setPhone_number(jOfficial.getJSONArray("phones").get(0).toString());
                }
                if(jOfficial.has("urls")){
                    temp.setWebsite(jOfficial.getJSONArray("urls").get(0).toString());
                }


                StringBuilder address = new StringBuilder();
                if(jOfficial.has("address")){
                    JSONObject jAddress = (JSONObject) jOfficial.getJSONArray("address").get(0);
                    if(jAddress.has("line1")){
                        address.append(jAddress.get("line1")).append(" ");
                    }
                    if(jAddress.has("city")){
                        address.append(jAddress.get("city")).append(", ");
                    }
                    if(jAddress.has("state")){
                        address.append(jAddress.get("state")).append(" ");
                    }
                    if(jAddress.has("zip")){
                        address.append(jAddress.get("zip"));
                    }
                    temp.setOffice_address(address.toString());
                }

                if(jOfficial.has("channels")){
                    JSONArray jChannels = jOfficial.getJSONArray("channels");
                    for(int j = 0; j < jChannels.length(); j++){
                        JSONObject ch = (JSONObject) jChannels.get(j);
                        if(ch.get("type").toString().equals("Facebook")){
                            temp.setFacebook(ch.get("id").toString());
                        }
                        if(ch.get("type").toString().equals("Twitter")){
                            temp.setTwitter(ch.get("id").toString());
                        }
                        if(ch.get("type").toString().equals("YouTube")){
                            temp.setYoutube(ch.get("id").toString());
                        }
                    }
                }
                OList.add(temp);
            }
            //loop through the offices_info, for each entry, check the officialIndices field and loop through it,by get the object from arraylist by the index and put the office name to the object
            for(int i = 0; i < offices_info.length(); i++){
                JSONObject office = (JSONObject) offices_info.get(i);
                JSONArray indices = office.getJSONArray("officialIndices");
                String office_name = office.getString("name");
                for(int j = 0; j < indices.length(); j++){
                    int outter_index = (int)indices.get(j);
                    OList.get(outter_index).setOffice(office_name);
                }
            }
            return OList;
        }
        catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
