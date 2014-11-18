package tinkercoder.tipcalculator.tipcalculator;

import tinkercoder.tipcalculator.navdrawer.DrawerActivity;
import android.os.Bundle;

public class TippingInfo extends DrawerActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.setInflaterOnView("tippinginfo");
		super.getDrawerList().setItemChecked(5, true);
	}
}
