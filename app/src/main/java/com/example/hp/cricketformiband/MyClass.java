package com.example.hp.cricketformiband;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hp on 04-03-2019.
 */

public class MyClass extends AsyncTask<String ,Void,String> {

    String finalString;

    @Override
    protected String doInBackground(String... params) {

        try {
            URL scoreurl = new URL(params[0]);
            URLConnection connection = scoreurl.openConnection();

            BufferedReader bf = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));

            String tmp = bf.readLine();

            while (tmp != null) {
                finalString = finalString + tmp;
                tmp = bf.readLine();
            }
        } catch (Exception e) {
            e.getMessage();
        }


        return finalString;
    }
}
