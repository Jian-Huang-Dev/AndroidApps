package tinkercoder.tipcalculator.preferenceactivity;

import tinkercoder.tipcalculator.tipcalculator.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class PreferenceSetting extends PreferenceActivity {

	Intent intent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.user_settings);

	}
	
	public SharedPreferences getSharedPreferences (Context context) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref;
	}
	
//	public void onBackPressed() {
//		intent = new Intent (this, Home.class);
//		startActivity(intent);
//	}

}