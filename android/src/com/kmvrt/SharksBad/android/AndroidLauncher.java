package com.kmvrt.SharksBad.android;

import android.os.Bundle;
import android.os.Build;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.kmvrt.SharksBad.SharksBadGame;
import com.kmvrt.SharksBad.Advertiser;

public class AndroidLauncher extends AndroidApplication implements Advertiser {
	
	private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-6573443238300994/3465245262";
	private static final String TEST_DEVICE_ID = "EAOKBC453263";
	
	private InterstitialAd interstitialAd;
	private AdRequest.Builder adBuilder;
	
	private boolean isInitialized;
	
	public AndroidLauncher() {
		isInitialized = false;
	}
	
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		if(!isInitialized) {
			super.onCreate(savedInstanceState);
			initAd();	// initialise the advertisement
			AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
			initImmersive(config);
			initialize(new SharksBadGame(this), config);
			isInitialized = true;
		}
	}
	
	private void initImmersive(AndroidApplicationConfiguration config) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
			config.useImmersiveMode = true;
	}
	
	private void initAd() {
		adBuilder = new AdRequest.Builder();
//		adBuilder.addTestDevice(TEST_DEVICE_ID);
		
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
		interstitialAd.setAdListener(
			new AdListener() {
				
				@Override
				public void onAdClosed() {
					
					// load a new ad
					AdRequest adRequest = adBuilder.build();
					interstitialAd.loadAd(adRequest);
				}
			});
		
		// load the first ad
		AdRequest adRequest = adBuilder.build();
		interstitialAd.loadAd(adRequest);
	}
	
	public void showAd() {
		
		try {
			this.runOnUiThread(
				new Runnable() {
					
					@Override
					public void run() {
						if(interstitialAd.isLoaded()) {
							interstitialAd.show();
						}
					}
				});
		} catch(Exception e) {}
	}
}
