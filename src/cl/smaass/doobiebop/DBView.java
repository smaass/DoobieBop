package cl.smaass.doobiebop;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

public class DBView extends View {
	
	private DBWaveController controller;
	private int lowFr;
	private double exponentFactor;

	public DBView(Context context) {
		super(context);
	}
	
	public void setWaveController(DBWaveController controller) {
		this.controller = controller;
		setViewListeners();
	}
	
	public void setFrequencyRange(int low, int high) {
		assert low < high;
		this.lowFr = low;
		exponentFactor = Math.log(high/low);
	}
	
	private int getFrequency(float normalizedX) {
		return (int) (Math.exp(normalizedX * exponentFactor) * lowFr);
	}
	
	private void setViewListeners() {
		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				int action = e.getAction();
				int freq = getFrequency(e.getX() / getWidth());
				float normalizedY = 1 - (e.getY() / getHeight());
				
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						controller.updateWave(freq, normalizedY);
						break;
					case MotionEvent.ACTION_MOVE:
						controller.updateWave(freq, normalizedY);
						break;
					case MotionEvent.ACTION_UP:
						controller.stopWave();
				}
				
				return true;
			}
			
		});
	}
	
}
