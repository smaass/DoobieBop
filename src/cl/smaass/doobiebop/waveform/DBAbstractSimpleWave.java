package cl.smaass.doobiebop.waveform;

public abstract class DBAbstractSimpleWave implements DBWaveform {
	
	protected final double TWOPI = 2 * Math.PI;
	private float frequency, phase, stepFactor;
	private float amplitude; /* must be between 0 and 1 */

	public DBAbstractSimpleWave(int samplingRate) {
		phase = 0;
		frequency = 0;
		amplitude = 1;
		stepFactor = (float) (2*Math.PI / samplingRate);
	}
	
	@Override
	public float getInstantAmplitude() {
		float retVal = getValue(phase);
		phase += stepFactor * frequency;
		if (phase > TWOPI) phase -= TWOPI;
		return retVal * amplitude;
	}

	@Override
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	@Override
	public void setControlFactor(float amplitude) {
		this.amplitude = amplitude;
	}
	
	abstract public float getValue(float phase);
}