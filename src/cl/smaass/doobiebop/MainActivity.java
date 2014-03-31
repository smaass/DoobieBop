package cl.smaass.doobiebop;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

public class MainActivity extends Activity {
	private final int SAMPLERATE = 44100;
	private List<DoobieChannel> channels;
	private Thread playerThread;
	private DoobieView doobieView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		doobieView = new DoobieView(this);
		setContentView(doobieView);
		initChannels();
		playerThread = new Thread(new Runnable() {
			public void run() {
				AudioTest device = new AudioTest(SAMPLERATE);
				float samples[] = new float[1024];
				
				while(true) {
					for( int i = 0; i < samples.length; i++ ) {
						samples[i] = getInstantSample();
					}
					device.writeSamples( samples );
				}        	
			}
		});
		playerThread.setPriority(Thread.MAX_PRIORITY);
	}
	
	private void initChannels() {
		channels = new ArrayList<DoobieChannel>();
		channels.add(new DoobieChannel(440, SAMPLERATE));
	}
	
	public float getInstantSample() {
		float total = 0;
		for (DoobieChannel s : channels) {
			total += s.getNextValue();
		}
		return total;
	}

	@Override
	protected void onStart() {
		super.onStart();
		setViewListeners(doobieView);
		playerThread.start();
	}
	
	private void setViewListeners(final View view) {
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent e) {
				int action = e.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					playSound(e.getX()/view.getWidth(), 1-(e.getY()/view.getHeight()));
				}
				else if (action == MotionEvent.ACTION_MOVE) {
					playSound(e.getX()/view.getWidth(), 1-(e.getY()/view.getHeight()));
				}
				else if (action == MotionEvent.ACTION_UP) {
					stopSound();
				}
				return true;
			}
			
		});
	}
	
	public void playSound(float x, float y) {
		channels.get(0).play(x, y);
	}
	
	public void stopSound() {
		channels.get(0).stop();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
