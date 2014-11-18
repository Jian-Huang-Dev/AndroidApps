package tinkercoder.stockcalculator.preferenceactivity;

import tinkercoder.stockcalculator.stockcalculator.R;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class adMobPreference extends Preference implements OnClickListener {
	AdView adView;
	Button rateBtn;

	public adMobPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public adMobPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public adMobPreference(Context context) {
		super(context);
	}
	@Override
	protected View onCreateView(ViewGroup parent) {
//		 View view = super.onCreateView(parent);
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.admob, null);

		// // find and load ads
		adView = (AdView) view.findViewById(R.id.adView);
		rateBtn = (Button) view.findViewById(R.id.rate_btn);
		
		rateBtn.setOnClickListener(this);
		
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("98C719A04DF7111D2DDD25D764C88F8E").build();
		if (adView != null) {
			adView.loadAd(adRequest);
		}
		return view;
	}
	
	public void rateThisApp(String packageName) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://details?id=" + packageName));
		try {
			getContext().startActivity(intent);
		} catch (android.content.ActivityNotFoundException ActivityNotFoundException) {
			intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ packageName));
			getContext().startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		rateThisApp("tinkercoder.stockcalculator.stockcalculator");
	}

}