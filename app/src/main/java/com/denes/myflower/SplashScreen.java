package com.denes.myflower;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private TextView text;
    private static int Splash_Time_out=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        text=findViewById(R.id.textView5);
        text.setText("Çiçeklerin oImadığı yerde,\n insanlar yaşayamaz.\n NapoIeon");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeintent=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(homeintent);
                finish();
            }
        },Splash_Time_out);
    }
}
