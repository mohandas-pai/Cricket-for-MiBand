package com.example.hp.cricketformiband;

import android.app.NotificationManager;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    int matchId;

    Button btn;

    String finalscore,scorecard;

    int flag = 0;

    RadioButton rb1,rb5,rb10;

    int mytime;

    EditText eMatchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eMatchId = (EditText)findViewById(R.id.matchID);
        btn = (Button)findViewById(R.id.button);
        rb1 = (RadioButton)findViewById(R.id.radioButton);
        rb5 = (RadioButton)findViewById(R.id.radioButton2);
        rb10 = (RadioButton)findViewById(R.id.radioButton3);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    matchId = Integer.parseInt(eMatchId.getText().toString());

                    if(rb1.isChecked()){
                        mytime = 60000;
                    }

                    if(rb5.isChecked()){
                        mytime = 300000;
                    }

                    if(rb10.isChecked()){
                        mytime = 600000;
                    }

                    flag = 1;

                }catch (Exception e){
                }

                doing();

            }
        });

    }



    public void doing()
    {

        if(flag == 1) {
            String url = "http://cricscore-api.appspot.com/csa?id=" + matchId;

            MyClass my = new MyClass();
            my.execute(url);

            String jsonstring = null;
            try {
                jsonstring = my.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            try {

                Log.d("String", "*****************" + jsonstring);

                jsonstring = jsonstring.substring(jsonstring.indexOf("["), jsonstring.length());

                // JSONObject object=new JSONObject(jsonstring);            //JSONArray array=object.optJSONArray("");
                JSONArray array = new JSONArray(jsonstring);

                // JSONArray array=new JSONArray(jsonstring);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);

                    String matchname = "---> \t" + jsonObject.optString("t1").toString() + " VS " + jsonObject.optString("t1").toString();

                    scorecard = (jsonObject.optString("de").toString());
                    finalscore = (jsonObject.optString("si").toString());

                    //String[] mystring = jsonObject.optString("de").toString().split(",");

                    //player1 = (mystring[1]);
                    //player2 = (mystring[2]);

                    //Toast.makeText(MainActivity.this, "Sending Notification", Toast.LENGTH_LONG).show();
                    showNotification(scorecard, finalscore);
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block            e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(MainActivity.this,"No MatchID set",Toast.LENGTH_LONG).show();
        }

    }

    private void showNotification(String scorecard, String finalscore) {
        int NOTIFICATION_ID = 45;
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText(scorecard+" "+finalscore);
        notificationManager.notify(NOTIFICATION_ID,mBuilder.build());

    }

    @Override
    protected void onPause(){
        super.onPause();

        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                doing();
                handler.postDelayed(this, mytime);
            }
        };
        handler.post(run);

//        final android.os.Handler handle = new android.os.Handler();
//        new CountDownTimer(80000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                doing();
//            }
//
//            @Override
//            public void onFinish() {
//                start();
//            }
//        }.start();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
