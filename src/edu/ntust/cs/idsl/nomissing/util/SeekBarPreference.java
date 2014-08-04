package edu.ntust.cs.idsl.nomissing.util;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import edu.ntust.cs.idsl.nomissing.R;

public class SeekBarPreference extends DialogPreference implements OnSeekBarChangeListener {

	private SeekBar mSeekBar;
	private TextView mTextView;
	
	private int mMin;
	private int mMax;
	private int mValue;
	private int mInterval;

	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
		mTextView = (TextView) view.findViewById(R.id.textView);
		mSeekBar.setOnSeekBarChangeListener(this);
		
		mSeekBar.setMax(mInterval);
		mSeekBar.setProgress(mValue - mMin);
		mTextView.setText(String.valueOf(mValue));
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			mValue = mSeekBar.getProgress() + mMin;
			this.getOnPreferenceChangeListener().onPreferenceChange(this, mValue);		
		} 
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		mTextView.setText(String.valueOf(progress + mMin));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
	
	public void init(int min, int max, int progress) {
		this.mMin = min;
		this.mMax = max;
		this.mValue = progress;
		this.mInterval = max - min;
	}

}
