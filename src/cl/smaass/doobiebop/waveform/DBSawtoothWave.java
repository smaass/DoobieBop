package cl.smaass.doobiebop.waveform;

public class DBSawtoothWave extends DBAbstractSimpleWave implements DBWaveform {

	public DBSawtoothWave(int samplingRate) {
		super(samplingRate);
	}

	@Override
	public float getValue(float phase) {
		float t = (float) (phase / TWOPI);
		return (float) (t - Math.floor(t));
	}

}
