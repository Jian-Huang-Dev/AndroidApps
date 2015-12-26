package tinkercoder.graphingquadratic.graphingquadratic;

import tinkercoder.graphingquadratic.navdrawer.DrawerActivity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class About extends DrawerActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.setInflaterOnView("about");
		super.getDrawerList().setItemChecked(6, true);
	}
}
