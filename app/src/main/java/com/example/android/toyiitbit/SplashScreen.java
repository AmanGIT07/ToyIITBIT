package com.example.android.toyiitbit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    Intent intent;
    String vName="1.0.0",vCode="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        vName = packageInfo.versionName;
        vCode =String.valueOf(packageInfo.versionCode);

        new accessNetwork().execute();



    }

    public class accessNetwork extends AsyncTask<Void,Void,String>
    {

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("https://studytutorial.in/post.php");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("versionName",vName);
                jsonObject.put("versionCode",vCode);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(12000);//millisecond
                httpURLConnection.setConnectTimeout(12000);//millisecond
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter;
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                bufferedWriter.write(getPostDataString(jsonObject));

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String line = "";

                    while((line = bufferedReader.readLine()) != null)
                    {
                        stringBuffer.append(line);
                        break;
                    }
                    bufferedReader.close();
                    return stringBuffer.toString();
                }
                else
                    return  new String("error:"+responseCode);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            //waiting 1500 milliseconds more
            new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
            }
          },1500);
        }
    }

  public String getPostDataString(JSONObject params) throws Exception
  {
      StringBuilder result = new StringBuilder();
      boolean first = true;
      Iterator<String>itr = params.keys();

      while(itr.hasNext())
      {
          String key = itr.next();
          Object value = params.get(key);

          if(first)
              first = false;
          else
              result.append("&");

          result.append(URLEncoder.encode(key,"UTF-8"));
          result.append("=");
          result.append(URLEncoder.encode(value.toString(),"UTF-8"));
      }
      return result.toString();
  }

}
