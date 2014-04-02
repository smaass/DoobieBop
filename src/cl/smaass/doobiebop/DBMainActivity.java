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

public class DBMainActivity extends Activity implements DBWaveWriter, DBWaveController {
	private final int SAMPLING_RATE = 44100;
	private List<DBChannel> channels;
	private DBPlayerThread playerThread;
	private DBView doobieView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		doobieView = new DBView(this);
		setContentView(doobieView);
		initChannels();
		doobieView.setWaveController(this);
		playerThread = new DBPlayerThread(SAMPLING_RATE, this);
		playerThread.setPriority(Thread.MAX_PRIORITY);
	}
	
	private void initChannels() {
		channels = new ArrayList<DBChannel>();
		channels.add(new DBChannel(440, SAMPLING_RATE));
	}
	
	public float getInstantSample() {
		float total = 0;
		for (DBChannel s : channels) {
			total += s.getNextValue();
		}
		return total;
	}

	@Override
	protected void onStart() {
		super.onStart();
		playerThread.start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		playerThread.end();
	}
	
	@Override
	public void updateWave(float x, float y) {
		channels.get(0).play(x, y);
	}
	
	@Override
	public void stopWave() {
		channels.get(0).stop();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
