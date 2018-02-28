package com.client.rokarr;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.client.rokarr.activity.DriveActivity;
import com.client.rokarr.fragments.FourFragment;
import com.client.rokarr.fragments.OneFragment;
import com.client.rokarr.fragments.ThreeFragment;
import com.client.rokarr.fragments.TwoFragment;

import java.util.ArrayList;
import java.util.List;

    public class MainActivity extends AppCompatActivity {
        private Toolbar toolbar;
        private TabLayout tabLayout;
        private ViewPager viewPager;
        private ImageView logout, home, drive;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            logout = (ImageView) findViewById(R.id.logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    logoutUser();
                }
            });
            home = (ImageView) findViewById(R.id.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MainActivity1.class);
                    startActivity(intent);
                    finish();
                }
            });
            drive = (ImageView) findViewById(R.id.sync);
            drive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DriveActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Sales");
        adapter.addFragment(new TwoFragment(), "Purchase");
        adapter.addFragment(new ThreeFragment(),"Saved Data");
        adapter.addFragment(new FourFragment(),"Daily Sheet");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

        private void logoutUser() {

            // Launching the login activity
            Intent intent = new Intent(MainActivity.this, MainActivity1.class);
            intent.putExtra("logout",true);
            startActivity(intent);
            finish();
        }

}
