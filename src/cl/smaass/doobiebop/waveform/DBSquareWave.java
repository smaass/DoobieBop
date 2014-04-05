package cl.smaass.doobiebop.waveform;

public class DBSquareWave extends DBAbstractSimpleWave implements DBWaveform {

	public DBSquareWave(int samplingRate) {
		super(samplingRate);
	}

	@Override
	public float getValue(float phase) {
		int p = ((int) (phase / Math.PI)) & 1;
		if (p == 0) return 1;
		else return -1;
	}

}
