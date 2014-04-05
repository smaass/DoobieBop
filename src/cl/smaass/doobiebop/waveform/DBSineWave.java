package cl.smaass.doobiebop.waveform;

public class DBSineWave extends DBAbstractSimpleWave implements DBWaveform {

	public DBSineWave(int samplingRate) {
		super(samplingRate);
	}

	@Override
	public float getValue(float phase) {
		return (float) Math.sin(phase);
	}

}
