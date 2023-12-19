package com.example.mainproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;


import com.example.mainproject.settings.settingsActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mainproject.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private ListView list;
    private String[] array;
    private ArrayAdapter<String> adapter;
    private Toolbar toolbar;
    private int index_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ////
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /////

        list = findViewById(R.id.listView);
        array = getResources().getStringArray(R.array.main_arr);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(array)));
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent = new Intent(MainActivity.this, ContentActivity.class );
               intent.putExtra("category", index_category);
               intent.putExtra("position", position);
               startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        toolbar.setTitle(R.string.Main);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent i = new Intent(MainActivity.this, settingsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.nav_main) {
            array = getResources().getStringArray(R.array.main_arr);
            adapter.clear();
            adapter.addAll(array);
            adapter.notifyDataSetChanged();
            toolbar.setTitle(R.string.Main);
            index_category = 0;

        }else if(id == R.id.nav_catalog){
            array = getResources().getStringArray(R.array.catalog_arr);
            adapter.clear();
            adapter.addAll(array);
            adapter.notifyDataSetChanged();
            toolbar.setTitle(R.string.Catalog);
            index_category = 1;

        }else if(id == R.id.nav_camera){

            Intent intent = new Intent(MainActivity.this, CameraActivity.class );
            startActivity(intent);

        }else if(id == R.id.nav_json){
            Intent intent = new Intent(MainActivity.this, JsonActivity.class );
            startActivity(intent);

        }else {
            Toast.makeText(this, "err "+id, Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}