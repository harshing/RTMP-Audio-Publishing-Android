package org.mconf.android.core.video;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.SystemClock;
import android.util.Log;

import com.flazr.rtmp.client.ClientOptions;
import com.flazr.rtmp.message.Audio;

public class VoiceOverRtmp implements VoiceInterface {
	
	private static final Logger log = LoggerFactory.getLogger(VoiceOverRtmp.class);

	private BbbVoiceConnection connection;
	private RtmpAudioPlayer audioPlayer = new RtmpAudioPlayer();
	private AudioPublish micBufferReader = new AudioPublish();
	protected boolean onCall = false;
	private Object args []={"Test1","18.9750/72.8258","AB","1","Female","info",""};
	//private Object args[]={"Test1"};
	public VoiceOverRtmp(BigBlueButtonClient bbb, ClientOptions options) {
				
		/*ClientOptions options=new ClientOptions();
		options.setHost("10.129.200.81");
		options.setAppName("HariPanTest3");
		//options.setAppName("PanTest");
		options.setStreamName("Test1");
		options.setArgs(args);
		options.publishLive();*/
		options.setReaderToPublish(micBufferReader);
		connection = new BbbVoiceConnection(bbb, options) {
			@Override
			protected void onAudio(Audio audio) {
				audioPlayer.onAudio(audio);
			}
			
			@Override
			protected void onConnectedSuccessfully() {
				onCall = true;
			}
			
			@Override
			public void channelDisconnected(ChannelHandlerContext ctx,
					ChannelStateEvent e) throws Exception {
				super.channelDisconnected(ctx, e);
					log.debug("\n\nvoice disconnected, stopping VoiceOverRtmp\n\n");
					onCall = false;
					audioPlayer.stop();
					
			}
		};
		
	}

	@Override
	public int start() {
		connection.start();
		
		int cont = 10;
		while (!onCall && cont > 0) {
			SystemClock.sleep(500);
			cont--;
		}
		
		if (cont == 0) {
			stop();
			return E_TIMEOUT;
		} 
		else 
		{
			audioPlayer.start();
			int res;
			if(sendFirstAudioPacket()) {
				micBufferReader.start();
				//Log.e("",""+Thread.currentThread());
				res=E_OK;
			}
			else {
				stop();
				//Log.e("",""+Thread.currentThread());
				res=E_TIMEOUT;
			}
			return res;
		}
	}
	
	private boolean sendFirstAudioPacket()
	{
		//for some reason - and we dont know why yet - after the reception of the first audio packet
		//the connection needs to wait 101 ms to then normally starts the audio dispatching
		//so..we are firing the first audio packet with a 101ms delay...
		//this first audio packet is in the audio buffer of the micBufferReader
		// ( you can check in the constructor of the AudioPublish class )
		
		// The voice connection waits for a 'createStream' server command to initialize the publisher
		// It means that by the time fireNext is called 'connection.publisher' may be null
		// So we have to wait until the publisher is initialized
		
		int attemptsLeft = 10;
		while(connection.publisher == null && attemptsLeft > 0) {
			SystemClock.sleep(500);
			attemptsLeft--;
		}
		
		if(attemptsLeft == 0) {
			/* Failed to initialize the publisher */
			return false;
		}
		else {
			connection.publisher.fireNext(connection.publisher.channel, 101);
			return true;
		}
	}

	@Override
	public void stop() {	
		connection.stop();	
	}

	@Override
	public boolean isOnCall() {
		return onCall;
	}

	@Override
	public boolean isMuted() {
		return micBufferReader.isMuted();
	}

	@Override
	public void muteCall(boolean mute) {
		if(mute)
			micBufferReader.mute();
		else
			micBufferReader.unmute();
		
	}

	@Override
	public int getSpeaker() {
		return 0;
	}

	@Override
	public void setSpeaker(int mode) {
		// TODO Auto-generated method stub
		
	}

}