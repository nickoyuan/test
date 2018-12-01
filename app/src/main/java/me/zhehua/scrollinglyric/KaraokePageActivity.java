package me.zhehua.scrollinglyric;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;


public class KaraokePageActivity extends AppCompatActivity implements FragmentOriginal.OnFragmentInteractionListener, FragmentMyVersion.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ImageView Back = (ImageView) findViewById(R.id.back);

        Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {startActivity(new Intent(KaraokePageActivity.this, MainActivity.class));
            }
        });




        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.instruction_1)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.instruction_2)));

      //  tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);



        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    //    @Override
      //  public void onFragmentInteraction (Uri uri){

        //}
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
