package com.example.mainproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mainproject.databinding.ActivityMainBinding;
import com.example.mainproject.databinding.ActivityPhotoCatalogBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class PhotoCatalog extends AppCompatActivity {

    ActivityPhotoCatalogBinding binding;
    ArrayList<String> snameList;
    ArrayAdapter<String> listAdapter;
    JSONArray jsonArray;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    String err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotoCatalogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initListData();

        binding.fetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new fetchData().start();
            }
        });
    }

    private void initListData() {

        snameList = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,snameList);
        binding.userList.setAdapter(listAdapter);
    }

    class fetchData extends Thread {

        String data = "";
        @Override
        public void run() {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(PhotoCatalog.this);
                    progressDialog.setMessage("Загрузка данных");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                URL url = new URL("https://online.csdb.ru/module/api/api.php?type=GetPhotoCatalog");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while((line = bufferedReader.readLine()) != null){
                    data = data + line;
                }

                jsonArray = new JSONArray();
                snameList.clear();
                if(!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    Iterator x = jsonObject.keys();

                    while (x.hasNext()){
                        String key = (String) x.next();
                        jsonArray.put(jsonObject.get(key));
                    }

                    for (int i=0; i < jsonArray.length(); i++) {
                        try {
                            if(i==0) continue;
                            snameList.add(jsonArray.getJSONArray(i).getString(0));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }


                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                        listAdapter.notifyDataSetChanged();
                }
            });

        }
    }
}