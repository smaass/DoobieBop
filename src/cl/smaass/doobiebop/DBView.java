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
