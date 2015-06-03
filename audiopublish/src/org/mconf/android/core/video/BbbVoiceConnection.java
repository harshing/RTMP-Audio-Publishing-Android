package org.mconf.android.core.video;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flazr.rtmp.client.ClientOptions;
import com.flazr.rtmp.message.Audio;

public class BbbVoiceConnection extends VoiceConnection {
	private static final Logger log = LoggerFactory.getLogger(BbbVoiceConnection.class);

	public BbbVoiceConnection(BigBlueButtonClient context, ClientOptions options) {
		super(options, context);
	}
	
	public void setLoop(boolean loop) {
		options.setLoop(loop ? Integer.MAX_VALUE : 0);
	}

	public boolean start() {
		return connect();
	}

	public void stop() {
		disconnect();
	}
	
	@Override
	protected void onAudio(Audio audio) {
		log.debug("received audio package: {}", audio.getHeader().getTime());
	}
}