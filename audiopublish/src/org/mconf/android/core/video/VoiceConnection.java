package org.mconf.android.core.video;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flazr.rtmp.RtmpDecoder;
import com.flazr.rtmp.RtmpEncoder;
import com.flazr.rtmp.client.ClientHandshakeHandler;
import com.flazr.rtmp.client.ClientOptions;
import com.flazr.rtmp.message.Audio;

public abstract class VoiceConnection extends RtmpConnection {

	private static final Logger log = LoggerFactory.getLogger(VoiceConnection.class);
	private String publishName;
	private String playName;
	@SuppressWarnings("unused")
	private String codec;
	private int playStreamId = -1;
	private int publishStreamId = -1;

	public VoiceConnection(ClientOptions options, BigBlueButtonClient context) {
		super(options, context);
	}
	
	@Override
	protected ChannelPipelineFactory pipelineFactory() {
		return new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
		        final ChannelPipeline pipeline = Channels.pipeline();
		        pipeline.addLast("handshaker", new ClientHandshakeHandler(options));
		        pipeline.addLast("decoder", new RtmpDecoder());
		        pipeline.addLast("encoder", new RtmpEncoder());
		        pipeline.addLast("handler", VoiceConnection.this);
		        return pipeline;
			}
		};
	}
	
	abstract protected void onAudio(Audio audio);

}