package com.jack.carebaby.ui;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jack.carebaby.R;
import com.jack.carebaby.base.BaseFragment;

import java.util.ArrayList;

public class SystemFragment extends BaseFragment implements OnTabSelectListener {

    //private SystemFragment mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "成长计划","日常","居家",  "医疗",  "更多>>"
    };
    private MyPagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_system, null);
        //获取组件

        //初始化页面
        initFragment();

        ViewPager vp=v.findViewById(R.id.system_viewpage);

        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        vp.setAdapter(mAdapter);

        /** tabbar设置 */
        SlidingTabLayout tabLayout_1 =v.findViewById(R.id.system_tablayout);
;
        tabLayout_1.setViewPager(vp);

        vp.setCurrentItem(0);

        tabLayout_1.showDot(0);


        return v;
    }

    public void initFragment(){
        mFragments.add(new SystemPlanFragment());
        mFragments.add(new SystemDailyFragment());

        mFragments.add(new SystemLiveFragment());

        mFragments.add(new SystemHospitalFragment());

        mFragments.add(new SystemMoreFragment());

    }


    //Tablayout设置函数
    @Override
    public void onTabSelect(int position) {
        //Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onTabReselect(int position) {
        //Toast.makeText(mContext, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
