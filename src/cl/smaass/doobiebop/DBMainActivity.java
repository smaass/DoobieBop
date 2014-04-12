package cl.smaass.doobiebop;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import cl.smaass.doobiebop.waveform.DBPairWave;
import cl.smaass.doobiebop.waveform.DBSawtoothWave;
import cl.smaass.doobiebop.waveform.DBSineWave;
import cl.smaass.doobiebop.waveform.DBWaveform;

public class DBMainActivity extends Activity implements DBWaveWriter, DBWaveController {
	private final int SAMPLING_RATE = 44100;
	private List<DBWaveform> waves;
	private DBPlayerThread playerThread;
	private DBView doobieView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initWaves();
		initView();
		initPlayerThread();
	}
	
	private void initWaves() {
		waves = new ArrayList<DBWaveform>();
		waves.add(new DBPairWave(new DBSineWave(SAMPLING_RATE), new DBSawtoothWave(SAMPLING_RATE)));
	}
	
	private void initView() {
		doobieView = new DBView(this);
		setContentView(doobieView);
		doobieView.setFrequencyRange(100, 1000);
		doobieView.setWaveController(this);
	}
	
	private void initPlayerThread() {
		playerThread = new DBPlayerThread(SAMPLING_RATE, this);
		playerThread.setPriority(Thread.MAX_PRIORITY);
	}
	
	@Override
	public float getInstantSample() {
		float total = 0;
		for (DBWaveform s : waves) {
			total += s.getInstantAmplitude();
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
	public void updateWave(int frequency, float y) {
		DBWaveform pairWave = waves.get(0);
		pairWave.setFrequency(frequency);
		pairWave.setControlFactor(1 - y);
	}
	
	@Override
	public void stopWave() {
		waves.get(0).setFrequency(0);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
