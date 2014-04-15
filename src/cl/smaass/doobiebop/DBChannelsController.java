package cl.smaass.doobiebop;

public interface DBChannelsController {
	public void attack(int channelIndex, int frequency, float controlParameter);
	public void update(int channelIndex, int frequency, float controlParameter);
	public void release(int channelIndex);
}
