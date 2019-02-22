package com.themaskedbit.tempcontact.view;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.themaskedbit.tempcontact.R;
import com.themaskedbit.tempcontact.presenter.TempPresenterImpl;
import com.themaskedbit.tempcontact.presenter.CallLogPresenterImpl;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TempPresenterImpl tempPresenter;
    private CallLogPresenterImpl callLogPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        TempFragment tempFragment = new TempFragment();
        tempPresenter = new TempPresenterImpl(tempFragment);
        CallLogFragment callLogFragment = new CallLogFragment();
        callLogPresenter = new CallLogPresenterImpl(callLogFragment);
        adapter.addFragment(tempFragment, "Temporary Contact");
        adapter.addFragment(callLogFragment, "Call Logs");
        viewPager.setAdapter(adapter);
    }

    public void loadContact(View v) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);

    }
}
