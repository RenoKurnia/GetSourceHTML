package com.example.renokurniarw.getsources;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by RenoKurniaRW on 10/15/2017.
 */


public class SourceGetter extends AsyncTaskLoader<String> {

    private String url_link;

    public SourceGetter(Context context, String the_link) {
        super(context);
        this.url_link = the_link;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        InputStream init;

        try {
            URL url = new URL(url_link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(20000);
            connection.setRequestMethod("GET");
            connection.connect();
            init = connection.getInputStream();
            BufferedReader buff = new BufferedReader(new InputStreamReader(init));
            StringBuilder sb = new StringBuilder();
            String line = "";
            String Builded;
            while ((line = buff.readLine()) != null) {
                sb.append(line + "\n");
            }
            buff.close();
            init.close();
            Builded = sb.toString();
            return Builded;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Undefined Protocol Errors, try fixing this issue with changing the http/https input";
    }

}