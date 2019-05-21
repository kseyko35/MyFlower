package com.denes.myflower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import android.widget.ViewFlipper;

public class Vanilya extends AppCompatActivity {
    private Toolbar mToolbar;
    ViewFlipper flipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanilya);
        mToolbar = findViewById(R.id.toolbarPortuca);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("About Vanilla");
        flipper=findViewById(R.id.view_flipper);
        int images[]={R.drawable.vanilya6,R.drawable.vanilya1,R.drawable.vanilya2,R.drawable.vanilya3,R.drawable.vanilya4,R.drawable.vanilya5};
        for(int image:images){
            flipperImages(image);
        }
    }
    public void flipperImages(int image){
        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(image);
        flipper.addView(imageView);
        flipper.setFlipInterval(3000);
        flipper.setAutoStart(true);

        flipper.setInAnimation(this,android.R.anim.slide_in_left);
        flipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
}
