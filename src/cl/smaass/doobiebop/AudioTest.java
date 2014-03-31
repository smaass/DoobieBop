package cl.smaass.doobiebop;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioTest {
	private AudioTrack track;
	private short[] buffer = new short[1024];
	 
	public AudioTest(int sampleRate) {
		int minSize =AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT );        
		track = new AudioTrack( AudioManager.STREAM_MUSIC, sampleRate, 
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, 
				minSize, AudioTrack.MODE_STREAM);
		track.play();        
	}	   
	 
	public void writeSamples(float[] samples) {	
		fillBuffer( samples );
		track.write( buffer, 0, samples.length );
	}
	
	private void fillBuffer( float[] samples ) {
		if( buffer.length < samples.length )
			buffer = new short[samples.length];
	 
		for( int i = 0; i < samples.length; i++ )
			buffer[i] = (short)(samples[i] * Short.MAX_VALUE);;
	}	
}
