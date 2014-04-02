package cl.smaass.doobiebop;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

public class DBView extends View {
	
	private DBWaveController controller;

	public DBView(Context context) {
		super(context);
	}
	
	public void setWaveController(DBWaveController controller) {
		this.controller = controller;
		setViewListeners();
	}
	
	private void setViewListeners() {
		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				int action = e.getAction();
				float normalizedX = e.getX() / getWidth();
				float normalizedY = 1 - (e.getY() / getHeight());
				
				if (action == MotionEvent.ACTION_DOWN)
					controller.updateWave(normalizedX, normalizedY);
				else if (action == MotionEvent.ACTION_MOVE)
					controller.updateWave(normalizedX, normalizedY);
				else if (action == MotionEvent.ACTION_UP)
					controller.stopWave();
				
				return true;
			}
			
		});
	}
	
}
