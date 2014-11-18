package tinkercoder.budgetcalculator.budgetcalculator;

import tinkercoder.budgetcalculator.navdrawer.DrawerActivity;
import android.os.Bundle;

public class About extends DrawerActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.setInflaterOnView("about");
		super.getDrawerList().setItemChecked(6, true);
	}
}
