package com.example.mainproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentActivity extends AppCompatActivity  {
    private TextView text_content;
    private ImageView iContent;
    private int category = 0, position = 0;
    private SharedPreferences def_pref;
    private int [] array_content = {R.string.content_1, R.string.content_2, R.string.content_3, R.string.content_4, R.string.content_5, R.string.content_6};
    private int [] array_catalog = {R.string.catalog_1, R.string.catalog_2, R.string.catalog_3, R.string.catalog_4, R.string.catalog_5};
    private int [] array_img = {R.drawable.cat_icon_1, R.drawable.cat_icon_2, R.drawable.cat_icon_1, R.drawable.cat_icon_2, R.drawable.cat_icon_1, R.drawable.cat_icon_2};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        def_pref = PreferenceManager.getDefaultSharedPreferences(this);
        text_content = findViewById(R.id.textMainContent);
        iContent = findViewById(R.id.imageContent);
        String text = def_pref.getString("main_text_size", "Средний");
        if(text != null) {
            switch (text) {
                case "Крупный":
                    text_content.setTextSize(24);
                    break;
                case "Средний":
                    text_content.setTextSize(18);
                    break;
                case "Мелкий":
                    text_content.setTextSize(14);
                    break;
                default:
                    text_content.setTextSize(18);
            }
        }

        reciveIntent();
    }

    private void reciveIntent(){
        Intent i = getIntent();
        if( i != null){
            category = i.getIntExtra("category", 0);
            position = i.getIntExtra("position", 0);
        }
        switch (category){
            case 0:
                if(position == 0){
                    Intent intent = new Intent(ContentActivity.this, PhotoCatalog.class );
                    startActivity(intent);
                }
                else if(position == 5){
                    Intent intent = new Intent(ContentActivity.this, CameraActivity.class );
                    startActivity(intent);
                }else {
                    text_content.setText(array_content[position]);
                    iContent.setImageResource(array_img[position]);
                }
                break;
            case 1:
                text_content.setText(array_catalog[position]);
                iContent.setImageResource(array_img[position]);
                break;
            default:

                break;


        }
    }
}