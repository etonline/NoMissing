package edu.ntust.cs.idsl.nomissing.adapter;

import edu.ntust.cs.idsl.nomissing.fragment.CalendarFragment;
import edu.ntust.cs.idsl.nomissing.fragment.ChimeFragment;
import edu.ntust.cs.idsl.nomissing.fragment.AgendaFragment;
import edu.ntust.cs.idsl.nomissing.fragment.WeatherFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        Fragment fragment = null;
        
        switch (index) {
        case 0:
            fragment = new AgendaFragment();
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
        }
 
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

}
