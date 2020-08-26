package com.example.home_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import java.net.*;
import java.io.*;

import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




import java.net.URL;
public class MainActivity extends AppCompatActivity {
      private Button btnon;
      private Switch swit;
      public double responseCode;
     private Button btnoff;
     public int counter=0;
     public boolean swtichCheck=false;
    public String url= "http://192.168.0.108/led1on";
    public String urloff= "http://192.168.0.108/led1off";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnon=(Button)findViewById(R.id.wifionn);
        btnoff=(Button)findViewById(R.id.wifioff);
        swit = (Switch)findViewById(R.id.sw);

        swit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View z)
            {
                swtichCheck=swit.isChecked();
                if(swtichCheck)
                {
                    Toast.makeText(getApplicationContext(), "MOBILE DATA MODE NOT AVAILABLE FOR NOW ", Toast.LENGTH_LONG).show();
                    btnon.setEnabled(false);
                    btnoff.setEnabled(false);


                }
                else
                {
                    Toast.makeText(getApplicationContext(), "WIFI MODE", Toast.LENGTH_LONG).show();
                    btnon.setEnabled(true);
                    btnoff.setEnabled(true);

                }

            }
        });
        btnon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                if (swtichCheck==false && isNetworkConnected()) {
                    try {
                        //GetText();
                        OkHttpHandler okHttpHandler = new OkHttpHandler();
                        okHttpHandler.execute(url);
                        okHttpHandler.getStatus();
                        if (responseCode == 200) {
                            Toast.makeText(getApplicationContext(), "ON", Toast.LENGTH_LONG).show();
                            counter = 0;
                            btnoff.setEnabled(true);
                            btnon.setEnabled(false);
                        } else if (counter > 3) {
                            Toast.makeText(getApplicationContext(), (int) responseCode + "Server Not Running", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), (int) responseCode + "TRY AGAIN", Toast.LENGTH_SHORT).show();
                            counter++;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "TURN ON MOBILE DATA/WIFI", Toast.LENGTH_LONG).show();
                }
            }
        });

btnoff.setOnClickListener(new View.OnClickListener(){
  public void onClick(View v) {
      if (swtichCheck==false && isNetworkConnected()) {
          OkHttpHandler okHttpHandler = new OkHttpHandler();
          okHttpHandler.execute(urloff);
          if (responseCode == 200 ) {
              Toast.makeText(getApplicationContext(), "OFF", Toast.LENGTH_LONG).show();
              counter = 0;
              btnon.setEnabled(true);
              btnoff.setEnabled(false);

          } else if (counter > 3) {
              Toast.makeText(getApplicationContext(), (int) responseCode + "Server Not Running/CHECK INTERNET", Toast.LENGTH_LONG).show();

          } else {
              Toast.makeText(getApplicationContext(), (int) responseCode + "TRY AGAIN", Toast.LENGTH_SHORT).show();
              counter++;
          }


      /*Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse(url));
      startActivity(i);*/
      }
      else if(swtichCheck==true || swtichCheck==false && !isNetworkConnected()) {
              Toast.makeText(getApplicationContext(), "TURN ON MOBILE/WIFI ", Toast.LENGTH_LONG).show();

      }
      else
      {
          Toast.makeText(getApplicationContext(), "SWITCH TO WIFI ", Toast.LENGTH_LONG).show();
      }
  }
});
    }




    public  void  GetText()
    {




    }


    public class OkHttpHandler extends AsyncTask<String,Void,String>{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String...params) {

            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
       responseCode=response.code();
               return response.body().string();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

           // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }






}
