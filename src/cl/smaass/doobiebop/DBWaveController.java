package cl.smaass.doobiebop;

public interface DBWaveController {
	public void updateWave(int waveIndex, int frequency, float controlParameter);
	public void stopWave(int waveIndex);
}
