package tinkercoder.tipcalculator.tipcalculator;

import tinkercoder.tipcalculator.navdrawer.DrawerActivity;
import android.os.Bundle;

public class About extends DrawerActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.setInflaterOnView("about");
		super.getDrawerList().setItemChecked(6, true);
	}
}
