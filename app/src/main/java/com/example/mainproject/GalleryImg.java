package com.example.mainproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class GalleryImg extends AppCompatActivity {

    String path = "http://kabakovisg.temp.swtest.ru/android/ShowImg.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_img);

        TextView contentView = findViewById(R.id.content);
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        Button btnFetch = findViewById(R.id.downloadBtn);
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentView.setText("Загрузка...");
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            String content = getContent(path);
                            webView.post(new Runnable() {
                                public void run() {
                                    webView.loadDataWithBaseURL(path,content, "text/html", "UTF-8", path);
                                    Toast.makeText(getApplicationContext(), "Данные загружены", Toast.LENGTH_SHORT).show();
                                }
                            });
                            contentView.post(new Runnable() {
                                public void run() {
                                    contentView.setText(content);
                                }
                            });
                        }
                        catch (IOException ex){
                            contentView.post(new Runnable() {
                                public void run() {
                                    contentView.setText("Ошибка: " + ex.getMessage());
                                    Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
    private String getContent(String path) throws IOException {
        BufferedReader reader=null;
        InputStream stream = null;
        HttpURLConnection connection = null;
        try {
            URL url=new URL(path);
            connection =(HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            stream = connection.getInputStream();
            reader= new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf=new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return(buf.toString());
        }
        finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}