package org.mconf.android.core.video;

import android.preference.ListPreference;
import android.preference.Preference;
import android.telephony.TelephonyManager;

class CodecBase implements Preference.OnPreferenceChangeListener {
	protected String CODEC_NAME;
	protected String CODEC_USER_NAME;
	protected int CODEC_NUMBER;
	protected int CODEC_SAMPLE_RATE=16000;		// default for most narrow band codecs
	protected int CODEC_FRAME_SIZE=320;		// default for most narrow band codecs
	protected String CODEC_DESCRIPTION;
	protected String CODEC_DEFAULT_SETTING = "never";

	private boolean loaded = false,failed = false;
	private boolean enabled = false;
	private boolean wlanOnly = false,wlanOr3GOnly = false;
	private String value;

	public void update() {
	
	}
	
	public String getValue() {
		return value;
	}
	
	void load() {
		update();
		loaded = true;
	}
	
	public int samp_rate() {
		return CODEC_SAMPLE_RATE;
	}
	
	public int frame_size() {
		return CODEC_FRAME_SIZE;
	}

	public boolean isLoaded() {
		return loaded;
	}
    
	public boolean isFailed() {
		return failed;
	}
	
	public void fail() {
		update();
		failed = true;
	}
	
	public void enable(boolean e) {
		enabled = e;
	}

	public boolean isEnabled() {
		return enabled;
	}

	TelephonyManager tm;
	int nt;
	
	public boolean isValid() {
		if (!isEnabled())
			return false;
		
		if (wlanOr3GOnly() && nt < TelephonyManager.NETWORK_TYPE_UMTS)
			return false;
		// \TODO this test is True on Android 3.1 (specifically on Galaxy Tab 
		// 10.1 3G), which means that the codecs will be classified as invalid, 
		// which means that the sound won't work at all 
//		if (nt < TelephonyManager.NETWORK_TYPE_EDGE)
//			return false;
		return true;
	}
		
	private boolean wlanOnly() {
		return enabled && wlanOnly;
	}
	
	private boolean wlanOr3GOnly() {
		return enabled && wlanOr3GOnly;
	}

	public String name() {
		return CODEC_NAME;
	}

	public String key() {
		return CODEC_NAME+"_new";
	}
	
	public String userName() {
		return CODEC_USER_NAME;
	}

	public String getTitle() {
		return CODEC_NAME + " (" + CODEC_DESCRIPTION + ")";
	}

	public int number() {
		return CODEC_NUMBER;
	}

	public void setListPreference(ListPreference l) {
		l.setOnPreferenceChangeListener(this);
		l.setValue(value);
	}

	public boolean onPreferenceChange(Preference p, Object newValue) {
		ListPreference l = (ListPreference)p;
		value = (String)newValue;

		updateFlags(value);

		l.setValue(value);
		l.setSummary(l.getEntry());

		return true;
	}

	private void updateFlags(String v) {

		if (v.equals("never")) {
			enabled = false;
		} else {
			enabled = true;
			if (v.equals("wlan"))
				wlanOnly = true;
			else
				wlanOnly = false;
			if (v.equals("wlanor3g"))
				wlanOr3GOnly = true;
			else
				wlanOr3GOnly = false;
		}
	}

	public String toString() {
		return "CODEC{ " + CODEC_NUMBER + ": " + getTitle() + "}";
	}
}