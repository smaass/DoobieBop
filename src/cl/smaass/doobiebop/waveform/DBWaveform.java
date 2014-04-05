package cl.smaass.doobiebop.waveform;

public interface DBWaveform {

	/**
	*	Returns the normalized instant amplitude of the wave at the current phase.
	*
	*	The wave must update its phase after this method is called. This method will
	*	be called a number of times per second given by the sampling rate.
	**/
	public float getInstantAmplitude();
	
	/**
	 * Sets the frequency of the wave
	 * @param frequency
	 */
	public void setFrequency(int frequency);
	
	/**
	 * Sets a control factor in [0,1] which will have an effect depending on each wave.
	 * 
	 * @param factor
	 */
	public void setControlFactor(float factor);
	
}
