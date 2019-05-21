package com.denes.myflower;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView mFlowersLabel;
    private TextView mPlantsLabel;
    private TextView mMyFlowersLabel;

    private ViewPager mMainPager;
    private PagerViewAdapter mPagerViewAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser=mAuth.getCurrentUser();
        if(currentuser==null) {
            sendToLogin();
        }
    }

    private void sendToLogin() {
        Intent i=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        mFlowersLabel=findViewById(R.id.Flowers_id);
        mPlantsLabel=findViewById(R.id.plants_id);
        mMyFlowersLabel=findViewById(R.id.myflower_id);
        mMainPager=findViewById(R.id.mainPager);

        mPagerViewAdapter=new PagerViewAdapter(getSupportFragmentManager());
        mMainPager.setAdapter(mPagerViewAdapter);

        mFlowersLabel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mMainPager.setCurrentItem(0);
        }
    });
        mPlantsLabel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mMainPager.setCurrentItem(1);
        }
    });
        mMyFlowersLabel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mMainPager.setCurrentItem(2);
        }
    });


        mMainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTabs(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void changeTabs(int position) {
        if(position==0) {
            mFlowersLabel.setTextColor(getColor(R.color.TextTabBright));
            mFlowersLabel.setTextSize(22);
            mPlantsLabel.setTextColor(getColor(R.color.TextTabLight));
            mPlantsLabel.setTextSize(16);
            mMyFlowersLabel.setTextColor(getColor(R.color.TextTabLight));
            mMyFlowersLabel.setTextSize(16);
        }
        if(position==1) {
            mFlowersLabel.setTextColor(getColor(R.color.TextTabLight));
            mFlowersLabel.setTextSize(16);
            mPlantsLabel.setTextColor(getColor(R.color.TextTabBright));
            mPlantsLabel.setTextSize(22);
            mMyFlowersLabel.setTextColor(getColor(R.color.TextTabLight));
            mMyFlowersLabel.setTextSize(16);
        }
        if(position==2) {
            mFlowersLabel.setTextColor(getColor(R.color.TextTabLight));
            mFlowersLabel.setTextSize(16);
            mPlantsLabel.setTextColor(getColor(R.color.TextTabLight));
            mPlantsLabel.setTextSize(16);
            mMyFlowersLabel.setTextColor(getColor(R.color.TextTabBright));
            mMyFlowersLabel.setTextSize(22);
        }
    }
}
