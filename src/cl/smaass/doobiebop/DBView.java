package cl.smaass.doobiebop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DBView extends RelativeLayout {
	
	private Button buttonUp, buttonDown;
	private DBWaveController controller;
	private int lowFr, highFr, startFr;
	private double exponentFactor;

	public DBView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.doobieview, this, true);
		setButtons();
	}
	
	private void setButtons() {
		buttonUp = (Button) findViewById(R.id.buttonUp);
		buttonDown = (Button) findViewById(R.id.buttonDown);
		
		buttonUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int frRatio = highFr / lowFr;
				startFr = startFr * frRatio;
				showFrequencyRange(startFr, startFr * frRatio);
			}
		});
		
		buttonDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int frRatio = highFr / lowFr;
				startFr = startFr / frRatio;
				showFrequencyRange(startFr, startFr * frRatio);
			}
		});
	}
	
	private void showFrequencyRange(int start, int end) {
		Toast.makeText(getContext(), start + " - " + end + " Hz", Toast.LENGTH_SHORT).show();
	}
	
	public void setWaveController(DBWaveController controller) {
		this.controller = controller;
		setViewListeners();
	}
	
	public void setFrequencyRange(int low, int high) {
		assert low < high;
		this.lowFr = low;
		this.startFr = low;
		this.highFr = high;
		exponentFactor = Math.log(high/low);
	}
	
	private int getFrequency(float normalizedX) {
		return (int) (Math.exp(normalizedX * exponentFactor) * startFr);
	}
	
	private void setViewListeners() {
		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				int action = e.getActionMasked();
				int index = e.getActionIndex();
				int id = e.getPointerId(index);
				int freq = getFrequency(e.getX(index) / getWidth());
				float normalizedY = 1 - (e.getY(index) / getHeight());
				
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						controller.updateWave(id, freq, normalizedY);
						return true;
					case MotionEvent.ACTION_MOVE:
						for (int i=0; i<e.getPointerCount(); i++) {
							id = e.getPointerId(i);
							freq = getFrequency(e.getX(i) / getWidth());
							normalizedY = 1 - (e.getY(i) / getHeight());
							controller.updateWave(id, freq, normalizedY);
						}
						return true;
					case MotionEvent.ACTION_POINTER_DOWN:
						controller.updateWave(id, freq, normalizedY);
						return true;
					case MotionEvent.ACTION_POINTER_UP:
						controller.stopWave(id);
						return true;
					case MotionEvent.ACTION_UP:
						controller.stopWave(id);
				}
				
				return true;
			}
			
		});
	}
	
}
