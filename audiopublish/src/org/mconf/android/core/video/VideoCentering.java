package org.mconf.android.core.video;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class VideoCentering {
	private static final Logger log = LoggerFactory.getLogger(VideoCentering.class);
	private float aspectRatio = 4 / (float) 3;
	
	public LayoutParams getVideoLayoutParams(DisplayMetrics metrics, LayoutParams layoutParams) {		
		int h = 0, w = 0;
		float displayAspectRatio = metrics.widthPixels / (float) metrics.heightPixels;
		if (displayAspectRatio < aspectRatio) {
			w = metrics.widthPixels;
			h = (int) (w / aspectRatio);
		} else {
			h = metrics.heightPixels;
			w = (int) (h * aspectRatio);			
		}
		layoutParams.width = w;
		layoutParams.height = h;
		return layoutParams;
	}
	
	public DisplayMetrics getDisplayMetrics(Context context, int margin){
		DisplayMetrics metrics = new DisplayMetrics();
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getMetrics(metrics);
        log.debug("Maximum display resolution: {} X {}\n", metrics.widthPixels, metrics.heightPixels);
        metrics.widthPixels -= margin;
		metrics.heightPixels -= margin;
		return metrics;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public float getAspectRatio() {
		return aspectRatio;
	}	
	
	public LayoutParams hidePreview(LayoutParams layoutParams){
		layoutParams.width = 1;
		layoutParams.height = 1;
		return layoutParams;
	}

	public LayoutParams destroyPreview(LayoutParams layoutParams) {
		layoutParams.width = 0;
		layoutParams.height = 0;
		return layoutParams;
	}
}
