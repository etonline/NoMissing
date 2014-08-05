package edu.ntust.cs.idsl.nomissing.activity;

import java.util.Calendar;

import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.R.layout;
import edu.ntust.cs.idsl.nomissing.fragment.CalendarFragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SetEventActivity extends Activity {

	public static final String TAG = SetEventActivity.class.getSimpleName();
	
	private Calendar calendar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_event);
		
		int month = getIntent().getIntExtra("month", 1);
		int day = getIntent().getIntExtra("day", 1);
		
		calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
	}
}
