package cl.smaass.doobiebop;

import cl.smaass.doobiebop.waveform.DBWaveform;

public class DBChannel {
	private DBWaveform waveform;
	private DBEnvelope envelope;
	private volatile boolean released;
	private volatile int lastEventTime;
	private int kSamplingRate;
	
	public DBChannel(DBWaveform waveform, DBEnvelope envelope, int samplingRate) {
		this.waveform = waveform;
		this.envelope = envelope;
		this.kSamplingRate = samplingRate / 1000;
	}
	
	public float getInstantAmplitude() {
		lastEventTime += 1;
		if (!released) {
			return waveform.getInstantAmplitude() * envelope.getADSValue(lastEventTime / kSamplingRate);
		}
		else {
			return waveform.getInstantAmplitude() * envelope.getReleaseValue(lastEventTime / kSamplingRate);
		}
	}
	
	public void attack(int frequency, float control) {
		lastEventTime = 0;
		released = false;
		update(frequency, control);
	}
	
	public void update(int frequency, float control) {
		waveform.setFrequency(frequency);
		waveform.setControlFactor(control);
	}
	
	public void release() {
		lastEventTime = 0;
		released = true;
	}
}
