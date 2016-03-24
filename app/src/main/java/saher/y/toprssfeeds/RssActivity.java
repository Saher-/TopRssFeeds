package saher.y.toprssfeeds;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;

import saher.y.toprssfeeds.Fragments.BusinessFragment;
import saher.y.toprssfeeds.Fragments.EconomyFragment;
import saher.y.toprssfeeds.Fragments.EducationFragment;
import saher.y.toprssfeeds.Fragments.EntertainmentFragment;
import saher.y.toprssfeeds.Fragments.GamingFragment;
import saher.y.toprssfeeds.Fragments.NewsFragment;
import saher.y.toprssfeeds.Fragments.ScienceFragment;
import saher.y.toprssfeeds.Fragments.SportsFragment;
import saher.y.toprssfeeds.Fragments.TechnologyFragment;

public class RssActivity extends AppCompatActivity {

    private TabLayout toolbar;
    private PagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        toolbar = (TabLayout) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.toolbar_viewpager);
        viewPager.setOffscreenPageLimit(8);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.toolbar_tabs);
        tabLayout.setupWithViewPager(viewPager);
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


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewsFragment(), "Top-News");
        adapter.addFragment(new SportsFragment(), "Sports");
        adapter.addFragment(new ScienceFragment(), "Science");
        adapter.addFragment(new TechnologyFragment(), "Technology");
        adapter.addFragment(new EducationFragment(), "Education");
        adapter.addFragment(new BusinessFragment(), "Business");
        adapter.addFragment(new EconomyFragment(), "Economy");
        adapter.addFragment(new EntertainmentFragment(), "Entertainment");
        adapter.addFragment(new GamingFragment(), "Gaming");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rss, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}