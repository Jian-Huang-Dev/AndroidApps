package jianhuang.flashlightpro.navdrawer;

import java.util.Date;

import johnbodun.flashlightpro.About;
import johnbodun.flashlightpro.R;
import johnbodun.flashlightpro.R.drawable;
import johnbodun.flashlightpro.R.id;
import johnbodun.flashlightpro.R.layout;
import johnbodun.flashlightpro.R.string;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

public class DrawerActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private int press = 0;
	private long currentTime = 0, latterTime = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_activity);

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
				getActionBar().setTitle("Flash Light Pro");
				// invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("More ...");
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		// update the main content by replacing fragments
		Intent intent = null;
		switch (position) {
		case 0:
			intent = new Intent(this, About.class);
			startActivity(intent);
			finish();
			break;
		case 1:
			intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://search?q=pub:johnbodun"));
			try {
				startActivity(intent);
			} catch (android.content.ActivityNotFoundException ActivityNotFoundException) {
				intent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("https://play.google.com/store/apps/developer?id=johnbodun"));
				startActivity(intent);
			}
			break;
		case 2:
			intent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("market://details?id=tinkercoder.mathtoolbox.mathtoolbox"));
			try {
				startActivity(intent);
			} catch (android.content.ActivityNotFoundException ActivityNotFoundException) {
				intent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id=tinkercoder.mathtoolbox.mathtoolbox"));
				startActivity(intent);
			}
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
		if (string.equalsIgnoreCase("home")) {
			activityView = layoutInflater.inflate(R.layout.flash,
					(ViewGroup) frameLayout, false);
		} else if (string.equalsIgnoreCase("about")) {
			activityView = layoutInflater.inflate(R.layout.about,
					(ViewGroup) frameLayout, false);
		}
		frameLayout.addView(activityView);
		return activityView;
	}

	public ObjectDrawerItem[] getDrawerItem() {
		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[3];
		drawerItem[0] = new ObjectDrawerItem(R.drawable.drawer_about,
				"About");
		drawerItem[1] = new ObjectDrawerItem(R.drawable.drawer_more_apps,
				"More apps");
		drawerItem[2] = new ObjectDrawerItem(R.drawable.drawer_rate,
				"Rate this app");
		return drawerItem;
	}

	protected ListView getDrawerList() {
		return this.mDrawerList;
	}

	protected DrawerLayout getDrawerLayout() {
		return this.mDrawerLayout;
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
		} else {
			// reset press count
			press = 0;
		}
	}
}
