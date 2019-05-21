package com.denes.myflower;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.denes.myflower.Fragment.FlowersFragment;
import com.denes.myflower.Fragment.MyFlowersFragment;
import com.denes.myflower.Fragment.PlantsFragment;

/**
 * Created by seyfi on 6.3.2018.
 */

class PagerViewAdapter extends FragmentPagerAdapter{

    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position){
            case 0:
                 FlowersFragment flowersFragment=new FlowersFragment();
                 return flowersFragment;
            case 1:
                 PlantsFragment plantsFragment =new PlantsFragment();
                 return plantsFragment;
            case 2:
                 MyFlowersFragment myFlowersFragment=new MyFlowersFragment();
                 return myFlowersFragment;
                 default:
                     return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
