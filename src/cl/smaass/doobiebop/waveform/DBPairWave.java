package cl.smaass.doobiebop.waveform;

public class DBPairWave implements DBWaveform {
	
	private DBWaveform wave1, wave2;
	private float p;
	
	public DBPairWave(DBWaveform wave1, DBWaveform wave2) {
		this.wave1 = wave1;
		this.wave2 = wave2;
	}

	@Override
	public float getInstantAmplitude() {
		return wave1.getInstantAmplitude() * p + wave2.getInstantAmplitude() * (1 - p);
	}

	@Override
	public void setFrequency(int frequency) {
		wave1.setFrequency(frequency);
		wave2.setFrequency(frequency);
	}
	
	/**
	 * Sets the factor to be used for mixing the waves.
	 * The mixture will be: wave1 * factor + wave2 * (1 - factor)
	 * 
	 * @param factor a float in [0, 1]
	 */
	@Override
	public void setControlFactor(float factor) {
		this.p = factor;
	}
	
}
