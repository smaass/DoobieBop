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

public class DBMainActivity extends Activity implements DBWaveWriter, DBChannelsController {
	private final int SAMPLING_RATE = 44100;
	private final int MAX_FINGERS = 5;
	private List<DBChannel> channels;
	private DBPlayerThread playerThread;
	private DBView doobieView;
	private int numChannels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initChannels();
		initView();
		initPlayerThread();
	}
	
	private DBEnvelope getEnvelope() {
		return new DBEnvelope() {

			@Override
			public float getADSValue(int millisSinceAttack) {
				if (millisSinceAttack <= 50)
					return millisSinceAttack / 50f;
				if (millisSinceAttack < 200)
					return 1 - (millisSinceAttack - 50) / 600f;
				return 0.75f;
			}

			@Override
			public float getReleaseValue(int millisSinceRelease) {
				if (millisSinceRelease < 300)
					return 0.75f - millisSinceRelease / 400f;
				return 0;
			}
			
		};
	}
	
	private void initChannels() {
		channels = new ArrayList<DBChannel>();
		for (int i = 0; i < MAX_FINGERS; i++) {
			DBChannel channel = new DBChannel(new DBPairWave(
													new DBSineWave(SAMPLING_RATE),
													new DBSawtoothWave(SAMPLING_RATE)),
											  getEnvelope(),
											  SAMPLING_RATE);
			channels.add(channel);
		}
		numChannels = channels.size();
	}
	
	private void initView() {
		doobieView = new DBView(this);
		setContentView(doobieView);
		doobieView.setFrequencyRange(400, 1600);
		doobieView.setWaveController(this);
	}
	
	private void initPlayerThread() {
		playerThread = new DBPlayerThread(SAMPLING_RATE, this);
		playerThread.setPriority(Thread.MAX_PRIORITY);
	}
	
	@Override
	public float getInstantSample() {
		float total = 0;
		for (DBChannel c : channels) {
			total += c.getInstantAmplitude() / numChannels;
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
	public void attack(int channelIndex, int frequency, float y) {
		if (channelIndex >= MAX_FINGERS) return;
		channels.get(channelIndex).attack(frequency, 1 - y);
	}
	
	@Override
	public void update(int channelIndex, int frequency, float y) {
		if (channelIndex >= MAX_FINGERS) return;
		channels.get(channelIndex).update(frequency, 1 - y);
	}
	
	@Override
	public void release(int channelIndex) {
		if (channelIndex >= MAX_FINGERS) return;
		channels.get(channelIndex).release();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
