package tinkercoder.graphingquadratic.navdrawer;

import java.util.Date;

import tinkercoder.graphingquadratic.gatracker.MyApplication;
import tinkercoder.graphingquadratic.graphingquadratic.About;
import tinkercoder.graphingquadratic.graphingquadratic.QuadraticSolver;
import tinkercoder.graphingquadratic.graphingquadratic.R;
import tinkercoder.graphingquadratic.preferenceactivity.PreferenceSetting;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;

public class DrawerActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private int press = 0;
	private long currentTime = 0, latterTime = 0;

	@Override
	protected void onStart() {
		super.onStart();

		// Get an Analytics tracker to report app starts & uncaught exceptions
		// etc.
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

		// Stop the analytics tracking
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_activity);
		overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
		
		// Get a Tracker (should auto-report)
		((MyApplication) getApplication())
				.getTracker(MyApplication.TrackerName.APP_TRACKER);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		DrawerItemAdapter itemAdapter = new DrawerItemAdapter(this,
				R.layout.drawer_list_item, this.getDrawerItem());
		mDrawerList.setAdapter(itemAdapter);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and
		// mDrawerList.setAdapter(new ArrayAdapter<String>(this,
		// R.layout.drawer_list_item, mDrawerItems));

		// click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(getString(R.string.app_name));
				// invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(getString(R.string.drawer_open_name));
				// invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		//
		// if (savedInstanceState == null) {
		// selectItem(0);
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_share:
			shareMyApp(
					"Share via ...",
					"Graphing Quadratic Android application",
					"http://play.google.com/store/apps/details?id=tinkercoder.graphingquadratic.graphingquadratic\n");
			return true;
		case R.id.action_rate:
			rateThisApp("tinkercoder.graphingquadratic.graphingquadratic");
			return true;
		case R.id.action_info:
            performSearch(getString(R.string.search_string));
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// this.playSound(2);
		// update the main content by replacing fragments
		Log.e("ERROR", "PASS selectItem");

		Intent intent = null;
		switch (position) {
		case 0:
			intent = new Intent(this, QuadraticSolver.class);
			startActivity(intent);
			// finish();
			break;
		case 1: // share button
			shareMyApp(
					"Share via ...",
					"Tip Calculator Android application",
					"http://play.google.com/store/apps/details?id=tinkercoder.graphingquadratic.graphingquadratic\n");
			break;
		case 2:
			getMoreApps("Tinkercoder");
			break;
		case 3:
			rateThisApp("tinkercoder.graphingquadratic.graphingquadratic");
			break;
		case 4:
			intent = new Intent(this, PreferenceSetting.class);
			startActivity(intent);
			// finish();
			break;
		case 5:
			performSearch(getString(R.string.search_string));
			break;
		case 6:
			intent = new Intent(this, About.class);
			startActivity(intent);
			finish();
			break;
		default:
			intent = new Intent(this, QuadraticSolver.class);
			startActivity(intent);
			finish();
			break;
		}

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		// setTitle(mDrawerItems[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	protected View setInflaterOnView(String string) {
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
		View activityView = null;

		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (string.equalsIgnoreCase("quadraticsolver")) {
			activityView = layoutInflater.inflate(R.layout.quadratic_solver,
					(ViewGroup) frameLayout, false);
		} else if (string.equalsIgnoreCase("about")) {
			activityView = layoutInflater.inflate(R.layout.about,
					(ViewGroup) frameLayout, false);
//		} else if (string.equalsIgnoreCase("tippinginfo")) {
//			activityView = layoutInflater.inflate(R.layout.tipping_info,
//					(ViewGroup) frameLayout, false);
		}
		frameLayout.addView(activityView);
		return activityView;
	}

	public ObjectDrawerItem[] getDrawerItem() {
		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[7];
		drawerItem[0] = new ObjectDrawerItem(R.drawable.drawer_circle,
				getString(R.string.drawer_quad));
		drawerItem[1] = new ObjectDrawerItem(R.drawable.drawer_circle,
				getString(R.string.drawer_share));
		drawerItem[2] = new ObjectDrawerItem(R.drawable.drawer_circle,
				getString(R.string.drawer_more_apps));
		drawerItem[3] = new ObjectDrawerItem(R.drawable.drawer_circle,
				getString(R.string.drawer_rate));
		drawerItem[4] = new ObjectDrawerItem(R.drawable.drawer_circle,
				getString(R.string.drawer_setting));
		drawerItem[5] = new ObjectDrawerItem(R.drawable.drawer_circle,
				getString(R.string.drawer_info));
		drawerItem[6] = new ObjectDrawerItem(R.drawable.drawer_circle,
				getString(R.string.drawer_about));
		return drawerItem;
	}

	protected ListView getDrawerList() {
		return this.mDrawerList;
	}

	protected DrawerLayout getDrawerLayout() {
		return this.mDrawerLayout;
	}

	protected void shareMyApp(String shareChooserTitle, String shareSubject,
			String shareBody) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(intent, shareChooserTitle));
	}

	protected void getMoreApps(String publisher) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://search?q=pub:" + publisher));
		try {
			startActivity(intent);
		} catch (android.content.ActivityNotFoundException ActivityNotFoundException) {
			intent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/developer?id="
							+ publisher));
			startActivity(intent);
		}
	}

	protected void rateThisApp(String packageName) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://details?id=" + packageName));
		try {
			startActivity(intent);
		} catch (android.content.ActivityNotFoundException ActivityNotFoundException) {
			intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ packageName));
			startActivity(intent);
		}
	}

	public void onBackPressed() {
		Date date = new Date();
		press = press + 1;
		Toast.makeText(getApplicationContext(), "Press Back key again to exit",
				Toast.LENGTH_SHORT).show();
		if (press == 1) {
			currentTime = date.getTime();
			latterTime = currentTime + 2000;
		} else if (press > 1 && date.getTime() <= latterTime) {
			// only exit activity if user press back key twice within 2 seconds
			finish();
			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
		} else {
			// reset press count
			press = 0;
		}
	}
	
	protected void performSearch (String string) {
		Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, string);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
        }
	}
}
