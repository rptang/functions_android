package com.project.rptang.android.weixin_fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

import com.project.rptang.android.R;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{

    private ViewPager mViewPager;
    private List<android.support.v4.app.Fragment> mFragmentList;

    private List<TabButton> mTabButtonList = new ArrayList<TabButton>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
        pagerAdapter();
        initEvent();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main,menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add){
            mTabButtonList.get(mViewPager.getCurrentItem()).addMessageNumber(1);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mTabButtonList.add((TabButton)findViewById(R.id.tab_first));
        mTabButtonList.add((TabButton)findViewById(R.id.tab_second));
        mTabButtonList.add((TabButton)findViewById(R.id.tab_third));
//        mTabButtonList.add((TabButton)findViewById(R.id.tab_fourth));

        mTabButtonList.get(0).setAlpha(1.0f);

    }

    private void pagerAdapter() {
        mFragmentList = new ArrayList<android.support.v4.app.Fragment>();
        for (int i=0;i<3;i++){
            mFragmentList.add(new Fragment(i));
        }
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),mFragmentList);
        mViewPager.setAdapter(adapter);
    }

    private void initEvent() {
        for(int i = 0; i < mTabButtonList.size(); i++){
            mTabButtonList.get(i).setOnClickListener(this);
            mTabButtonList.get(i).setTag(i);
        }
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {

        int number = (Integer) v.getTag();
        changeAlpha(number);
        mViewPager.setCurrentItem(number,false);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffsetPixels != 0){
            mTabButtonList.get(position).setAlpha(1 - positionOffset);
            mTabButtonList.get(position+1).setAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void changeAlpha(int number){
        for (TabButton btn:mTabButtonList){
            btn.setAlpha(0f);
        }
        mTabButtonList.get(number).setAlpha(1.0f);
    }
}
