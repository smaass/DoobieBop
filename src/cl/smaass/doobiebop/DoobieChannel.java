package cl.smaass.doobiebop;

import android.util.Log;


public class DoobieChannel {
	private int sampleRate;
	private float frequency;
	private float increment;
	private float angle;
	private Wave outputWave;
	private boolean isOn;
	private float x, y;
	
	public interface Wave {
		public float getValue(DoobieChannel channel);
	}
	
	public DoobieChannel(float frequency, int sampleRate) {
		this.sampleRate = sampleRate;
		this.frequency = frequency;
		this.increment = (float)(2*Math.PI) * frequency / sampleRate;
		this.angle = 0;
		this.isOn = false;
		setWave(new Wave() {

			@Override
			public float getValue(DoobieChannel channel) {
				float t = (float) (channel.angle/(2*Math.PI));
				return (float) (Math.sin(channel.angle)*(1-channel.y) + (t - Math.floor(t))*channel.y);
			}
			
		});
	}
	
	public float getNextValue() {
		if (!isOn) return 0;
		float value = outputWave.getValue(this);
		this.angle +=  this.increment;
		if (angle > 2*Math.PI) angle -= 2*Math.PI;
		return value;
	}
	
	public void setWave(Wave newWave) {
		this.outputWave = newWave;
	}
	
	public void play(final float x, final float y) {
		this.frequency = 196 + x*1000;
		this.increment = (float) (2*Math.PI) * frequency/sampleRate;
		this.x = x;
		this.y = y;
		this.isOn = true;
		Log.e("Doobie", "x: " + x + "; y: " + y + "; inc: " + increment);
	}
	
	public void stop() {
		this.isOn = false;
	}
}
