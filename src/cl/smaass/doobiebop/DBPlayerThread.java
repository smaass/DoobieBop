package cl.smaass.doobiebop;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class DBPlayerThread extends Thread {
	
	private int samplingRate;
	private AudioTrack audioTrack;
	private DBWaveWriter waveWriter;
	private short[] buffer = new short[1024];
	
	public DBPlayerThread(int samplingRate, DBWaveWriter waveWriter) {
		this.samplingRate = samplingRate;
		this.waveWriter = waveWriter;
		initAudioTrack();
	}
	
	public void initAudioTrack() {
		int minSize = AudioTrack.getMinBufferSize(samplingRate, AudioFormat.CHANNEL_OUT_MONO,
				  									AudioFormat.ENCODING_PCM_16BIT );
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, samplingRate, 
								AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, 
								minSize, AudioTrack.MODE_STREAM);
		audioTrack.play();    
	}
	
	public void run() {
		float samples[] = new float[1024];
		
		while(true) {
			for (int i = 0; i < samples.length; i++) {
				samples[i] = waveWriter.getInstantSample();
			}
			writeSamples(samples);
		}        	
	}
	
	public void end() {
		audioTrack.stop();
		audioTrack.release();
	}
	
	public void writeSamples(float[] samples) {	
		fillBuffer(samples);
		audioTrack.write(buffer, 0, samples.length);
	}
	
	private void fillBuffer(float[] samples) {
		if (buffer.length < samples.length)
			buffer = new short[samples.length];
	 
		for (int i = 0; i < samples.length; i++)
			buffer[i] = (short)(samples[i] * Short.MAX_VALUE);
	}
}
