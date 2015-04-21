package com.app.oliviadodge.bridgecrossings;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;


import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by oliviadodge on 2/1/2015.
 */
public class BridgeCrossingPagerActivity extends ActionBarActivity implements BridgeCrossingFragment.Callbacks{

    private ViewPager mViewPager;
    private ArrayList<BridgeCrossing> mCrossings;
    public static final String TAG = "BridgeCrossingPagerActivity";

    public void onCrossingUpdated(BridgeCrossing crossing){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_screen_slide);
        mViewPager = (ViewPager) findViewById(R.id.pager);

//        mViewPager = new ViewPager(this);
//        mViewPager.setId(R.id.viewPager);
//
//        setContentView(mViewPager);

        mCrossings = BridgeCrossingsLab.get(this).getCrossings();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                BridgeCrossing crossing = mCrossings.get(position);
                return BridgeCrossingFragment.newInstance(crossing.getCrossingId());
            }

            @Override
            public int getCount() {
                return mCrossings.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            public void onPageScrollStateChanged(int state){}

            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels){}

            public void onPageSelected(int pos){
                BridgeCrossing crossing = mCrossings.get(pos);
                if (crossing.getTitle() != null){
                    setTitle(crossing.getTitle());

                }
            }
        });

        UUID crossingId = (UUID)getIntent().getSerializableExtra(BridgeCrossingFragment.EXTRA_CROSSING_ID);
        for (int i = 0; i < mCrossings.size(); i++) {
            if (mCrossings.get(i).getCrossingId().equals(crossingId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }
}

