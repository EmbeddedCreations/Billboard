package com.example.billboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayBillboards extends AppCompatActivity {

    //Accessing the username from login
    String userId = Login.username;

    public static String current_ID,current_billboard,user_name;
    public static int billboards_count[];
    public static HashMap<Integer,String[]> billboardList;
    String address = "http://172.20.10.4/fetch_billboards.php?user="+userId;
    InputStream is = null;
    String line,result;
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    String id[],trip[],trip_date[],name[];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billboards_list);



        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        getData();
        List<BillboardData> BillboardCards = new ArrayList<>();
        for(int i =0;i<id.length;i++){
            String[] Billboard = trip[i].split("->");
//            billboards_count[i] = Billboard.length;
//            billboardList.put(i,Billboard);
            for(int j =0;j<Billboard.length;j++){
                BillboardCards.add(new BillboardData(id[i],Billboard[j],name[i]));
            }

        }
        recyclerView = findViewById(R.id.recyclerView);
        cardAdapter = new CardAdapter(BillboardCards);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cardAdapter);
    }
    private void getData(){
        try{
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());
            //Read it Into a String
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine())!= null){
                sb.append(line+"/n");
            }
            is.close();
            result = sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            JSONArray js = new JSONArray(result);
            JSONObject jo = null;
            id = new String[js.length()];
            trip = new String[js.length()];
            trip_date = new String[js.length()];
            name = new String[js.length()];
            for(int i =0;i< js.length();i++){
                jo = js.getJSONObject(i);
                if(jo.getString("Trip_Completed").equals("0")){
                    id[i] = jo.getString("id");
                    trip[i] = jo.getString("Trip");
                    trip_date[i] = jo.getString("Trip_date");
                    name[i] = jo.getString("Name");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){

        }
    }
}
