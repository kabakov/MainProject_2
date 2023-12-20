package com.example.mainproject;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainproject.databinding.ActivityPhotoCatalogSectionBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PhotoCatalogSection extends AppCompatActivity {

    private String key = "";
    private JSONArray jsonArray;

    private Handler sectionHandler = new Handler();
    private ProgressDialog progressDialog;
    private ActivityPhotoCatalogSectionBinding binding;
    private ListView listView;

    private HashMap<String, String> resultdata;
    private List<HashMap<String, String>> listDate;
    private SimpleAdapter adapter;

    private String err;
    private  TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_catalog_section);

        Bundle bundle = getIntent().getExtras();
        key = bundle.getString("key");

        binding = ActivityPhotoCatalogSectionBinding.inflate(getLayoutInflater());
        listView = findViewById(R.id.userList);
        textView = findViewById(R.id.textView2);

        binding.textView2.setText(bundle.getString("title"));
        setContentView(binding.getRoot());
        initListData();

        new PhotoCatalogSection.fetchData().start();

        binding.userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentarticle = listDate.get(position).get("First line");
                Intent intent = new Intent(PhotoCatalogSection.this, CameraActivity.class );
                intent.putExtra("art", currentarticle);
                startActivity(intent);
            }
        });

    }

    private void initListData() {

        listDate = new ArrayList<>();
        adapter = new SimpleAdapter(this, listDate, R.layout.list_item,
                new String[] {"First line", "Second line"}, new int[] {R.id.text1, R.id.text2});
        binding.userList.setAdapter(adapter);

    }

    public class fetchData extends Thread {
        String data = "";

        @Override
        public void run() {
            sectionHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(PhotoCatalogSection.this);
                    progressDialog.setMessage("Загрузка данных");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                URL url = new URL("https://online.csdb.ru/module/api/api.php?type=GetPhotoNomen&catalog="+key);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while((line = bufferedReader.readLine()) != null){
                    data = data + line;
                }

                jsonArray = new JSONArray();

                if(!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    Iterator x = jsonObject.keys();

                    while (x.hasNext()){
                        String key = (String) x.next();
                        jsonArray.put(jsonObject.get(key));
                    }
                    listDate.clear();
                    for (int i=0; i < jsonArray.length(); i++) {
                        try {
                            if(i==0) continue;
                            resultdata = new HashMap<>();
                            resultdata.put("First line", "" + jsonArray.getJSONArray(i).getString(0));
                            resultdata.put("Second line", "" + jsonArray.getJSONArray(i).getString(1) + " ["+ jsonArray.getJSONArray(i).getString(2) +"]");
                            listDate.add(resultdata);
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
            sectionHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    adapter.notifyDataSetChanged();

                }
            });
        }
    }
}