package tinkercoder.stockcalculator.calculator;

import tinkercoder.stockcalculator.navdrawer.DrawerActivity;
import tinkercoder.stockcalculator.stockcalculator.R;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class About extends DrawerActivity {

	private AdView adView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.setInflaterOnView("about");
		super.getDrawerList().setItemChecked(5, true);

		// find and load ads
		AdView adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("98C719A04DF7111D2DDD25D764C88F8E").build();
		if (adView != null) {
			adView.loadAd(adRequest);
		}
	}
}
