package cl.smaass.doobiebop;

public interface DBEnvelope {
	public float getADSValue(int millisSinceAttack);
	public float getReleaseValue(int millisSinceRelease);
}
