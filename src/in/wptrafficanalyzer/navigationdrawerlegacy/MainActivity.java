package in.wptrafficanalyzer.navigationdrawerlegacy;

import in.qlc.fragments.MyCalendarFragment;
import in.qlc.fragments.MyExpenseFragment;
import in.qlc.fragments.MyMessageFragment;
import in.qlc.fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.MapsInitializer;

public class MainActivity extends ActionBarActivity {

	int mPosition = -1;
	String mTitle = "";

	// Array of strings storing country names
	String[] mCountries;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private LinearLayout mDrawer;
	private List<HashMap<String, String>> mList;
	private SimpleAdapter mAdapter;
	final private String COUNTRY = "country";
	final private String COUNT = "count";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Getting an array of country names
		mCountries = getResources().getStringArray(R.array.countries);

		// Title of the activity
		mTitle = (String) getTitle();

		// Getting a reference to the drawer listview
		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		// Getting a reference to the sidebar drawer ( Title + ListView )
		mDrawer = (LinearLayout) findViewById(R.id.drawer);

		// Each row in the list stores country name, count and flag
		mList = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < mCountries.length; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(COUNTRY, mCountries[i]);
			hm.put(COUNT, "");
			mList.add(hm);
		}

		// Keys used in Hashmap
		String[] from = { COUNTRY, COUNT };

		// Ids of views in listview_layout
		int[] to = { R.id.country, R.id.count };

		// Instantiating an adapter to store each items
		// R.layout.drawer_layout defines the layout of each item
		mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout, from,
				to);

		// Getting reference to DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Creating a ToggleButton for NavigationDrawer with drawer event
		// listener
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				highlightSelectedCountry();
				supportInvalidateOptionsMenu();
			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle("Select a Country");
				supportInvalidateOptionsMenu();
			}
		};

		// Setting event listener for the drawer
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// ItemClick event handler for the drawer items
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				// Increment hit count of the drawer list item
				incrementHitCount(position);

				showFragment(position);
				// Closing the drawer
				mDrawerLayout.closeDrawer(mDrawer);
			}
		});

		// Enabling Up navigation
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setDisplayShowHomeEnabled(true);

		// Setting the adapter to the listView
		mDrawerList.setAdapter(mAdapter);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void incrementHitCount(int position) {
		HashMap<String, String> item = mList.get(position);
		String count = item.get(COUNT);
		item.remove(COUNT);
		if (count.equals("")) {
			count = "  1  ";
		} else {
			int cnt = Integer.parseInt(count.trim());
			cnt++;
			count = "  " + cnt + "  ";
		}
		item.put(COUNT, count);
		mAdapter.notifyDataSetChanged();
	}

	public void showFragment(int position) {

		// Currently selected country
		mTitle = mCountries[position];

		Fragment mainTab = new in.qlc.navigationtabdemo.MainActivity();

		Fragment cFragment = new in.qlc.navigationtabdemo.MainActivity();

		MyCalendarFragment calendarFrag = new MyCalendarFragment();
		MyExpenseFragment expenseFrag = new MyExpenseFragment();
		MyMessageFragment msgFrag = new MyMessageFragment();
		SettingsFragment settingFrag = new SettingsFragment();

		switch (position) {
		case 1:
			cFragment = msgFrag;
			break;
		case 2:
			cFragment = calendarFrag;
			break;
		case 3:
			cFragment = expenseFrag;
			break;
		case 4:
			cFragment = settingFrag;
			break;

		default:
			break;
		}

		// Creating a Bundle object
		Bundle data = new Bundle();

		// Setting the index of the currently selected item of mDrawerList
		data.putInt("position", position);

		// Setting the position to the fragment
		cFragment.setArguments(data);

		// Getting reference to the FragmentManager
		FragmentManager fragmentManager = getSupportFragmentManager();

		// Creating a fragment transaction
		FragmentTransaction ft = fragmentManager.beginTransaction();

		if (position == 0) {
			ft.replace(R.id.content_frame, mainTab);
			try {
				MapsInitializer.initialize(this);
			} catch (GooglePlayServicesNotAvailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			ft.replace(R.id.content_frame, cFragment);

		// Committing the transaction
		ft.commit();
	}

	// Highlight the selected country : 0 to 4
	public void highlightSelectedCountry() {
		int selectedItem = mDrawerList.getCheckedItemPosition();

		if (selectedItem > 4)
			mDrawerList.setItemChecked(mPosition, true);
		else
			mPosition = selectedItem;

		if (mPosition != -1)
			getSupportActionBar().setTitle(mCountries[mPosition]);
	}
}