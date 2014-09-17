package edu.ntust.cs.idsl.nomissing.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.adapter.TabsPagerAdapter;
import edu.ntust.cs.idsl.nomissing.constant.NavItem;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.MAIN";
    
    private ActionBar actionBar;
    private ViewPager viewPager;
    private TabsPagerAdapter tabsPagerAdapter;
    private boolean doubleBackToExitPressedOnce = false;
    
    public static void startAction(Context context) {
        context.startActivity(getAction(context));
    }
    
    public static Intent getAction(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(ACTION);   
        return intent;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setViewPager();
        setActionBar();    
    }

    private void setActionBar() {
        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
        
        for (NavItem tab : NavItem.getNavItems()) {
            actionBar.addTab(actionBar.newTab().setIcon(tab.getIcon()).setTabListener(this));
        }
    }
    
    private void setViewPager() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tabsPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings:
            SettingsActivity.startAction(this);
            return true;

        case R.id.action_about:
            AboutActivity.startAction(this);
            return true;
            
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        ToastMaker.toast(this, getString(R.string.click_back_again_to_exit));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }    
    
}
