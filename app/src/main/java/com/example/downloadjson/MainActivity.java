package com.example.downloadjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ololo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadJSON task = new DownloadJSON();
        task.execute("https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22");
    }
    private static class DownloadJSON extends AsyncTask<String,Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader =  new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();
                while (line !=null){
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
                return  stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection !=null){
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TAG, "onPostExecute: " + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray  jsonArray = jsonObject.getJSONArray("weather");
                JSONObject weather = jsonArray.getJSONObject(0);
                String id = weather.getString("id");
                String main = weather.getString("main");
                String description = weather.getString("description");
                String icon = weather.getString("icon");
                Log.i(TAG, "onPostExecute: id " + id);
                Log.i(TAG, "onPostExecute: main " + main);
                Log.i(TAG, "onPostExecute: description " + description);
                Log.i(TAG, "onPostExecute: icon " + icon);
//                JSONObject main =jsonObject.getJSONObject("main");
//                String temp = main.getString("temp");
//                String pressure = main.getString("pressure");
//                String humidity = main.getString("humidity");
//                Log.i(TAG, "onPostExecute: temp " + temp);
//                Log.i(TAG, "onPostExecute: pressure " + pressure);
//                Log.i(TAG, "onPostExecute: humidity " + humidity);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
