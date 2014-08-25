package edu.ntust.cs.idsl.nomissing.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract.Calendars;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.adapter.NavDrawerListAdapter;
import edu.ntust.cs.idsl.nomissing.constant.NavDrawerItem;
import edu.ntust.cs.idsl.nomissing.dao.calendar.CalendarProviderDaoFactory;
import edu.ntust.cs.idsl.nomissing.fragment.CalendarFragment;
import edu.ntust.cs.idsl.nomissing.fragment.ChimeFragment;
import edu.ntust.cs.idsl.nomissing.fragment.HomeFragment;
import edu.ntust.cs.idsl.nomissing.fragment.SettingsFragment;
import edu.ntust.cs.idsl.nomissing.fragment.WeatherFragment;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Calendar;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint("NewApi") 
public class MainActivity extends FragmentActivity implements OnItemClickListener {

	private NoMissingApp app;
	
	private DrawerLayout drawerLayout;
	private LinearLayout drawerNavigation;
	private ListView drawerMenu;
	private ActionBarDrawerToggle drawerToggle;

	private CharSequence drawerTitle;
	private CharSequence appTitle;

	private List<NavDrawerItem> navDrawerItems;
	
	private boolean doubleBackToExitPressedOnce = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		appTitle = drawerTitle = getTitle();
		app = (NoMissingApp)getApplicationContext();
		
		setActionBar();
		setNavigationDrawer();
		
		if (savedInstanceState == null) {
			displayView(0);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if (app.getSettings().getCalendarID() == -1)
			openSettingCalendarDialog();
	}

	private void setActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);			
	}
	
	private void setNavigationDrawer() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			@Override
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(appTitle);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(drawerTitle);
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		
		navDrawerItems = NavDrawerItem.getNavDrawerItems();
		drawerNavigation = (LinearLayout) findViewById(R.id.drawerNavigation);
		drawerMenu = (ListView) findViewById(R.id.drawerMenu);
		drawerMenu.setAdapter(new NavDrawerListAdapter(getApplicationContext(), navDrawerItems));
		drawerMenu.setOnItemClickListener(this);
	}

	@Override
	public void setTitle(CharSequence title) {
		appTitle = title;
		getActionBar().setTitle(appTitle);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerNavigation);
		menu.findItem(R.id.action_about).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_about:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		displayView(position);	
	}	
	
	private void displayView(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new CalendarFragment();
			break;
		case 2:
			fragment = new ChimeFragment();
			break;
		case 3:
			fragment = new WeatherFragment();	
			break;
		case 4:
			fragment = new SettingsFragment();	
			break;			
		case 5:
			finish();
			
			break;				
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

			drawerMenu.setItemChecked(position, true);
			drawerMenu.setSelection(position);
			setTitle(navDrawerItems.get(position).getTitle());
			drawerLayout.closeDrawer(drawerNavigation);
		} else {
			Log.e("MainActivity", "Error in creating fragment");
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
	
	private void openSettingCalendarDialog() {
		final List<Calendar> calendars = 
				CalendarProviderDaoFactory.createCalendarDao(this).findByAccessLevel(Calendars.CAL_ACCESS_OWNER);
	
		new AlertDialog.Builder(this)
		.setTitle(R.string.dialog_set_calendar)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setItems(Calendar.getNameOfCalendars(calendars).toArray(new String[calendars.size()]), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				app.getSettings().setCalendarID(calendars.get(which).getId());
			}
		}).show();
	}
	
}

