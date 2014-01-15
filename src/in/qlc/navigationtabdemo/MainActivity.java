package in.qlc.navigationtabdemo;

import in.wptrafficanalyzer.navigationdrawerlegacy.R;
import in.wptrafficanalyzer.navigationdrawerlegacy.R.layout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class MainActivity extends Fragment {
	TabHost tHost;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.activity_main1, container, false);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

		tHost = (TabHost) view.findViewById(android.R.id.tabhost);
		tHost.setup();

		/**
		 * Defining Tab Change Listener event. This is invoked when tab is
		 * changed
		 */
		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				android.support.v4.app.FragmentManager fm = getActivity()
						.getSupportFragmentManager();
				AndroidFragment androidFragment = (AndroidFragment) fm
						.findFragmentByTag("android");
				AppleFragment appleFragment = (AppleFragment) fm
						.findFragmentByTag("apple");
				android.support.v4.app.FragmentTransaction ft = fm
						.beginTransaction();

				/** Detaches the androidfragment if exists */
				if (androidFragment != null)
					ft.detach(androidFragment);

				/** Detaches the applefragment if exists */
				if (appleFragment != null)
					ft.detach(appleFragment);

				/** If current tab is android */
				if (tabId.equalsIgnoreCase("android")) {

					if (androidFragment == null) {
						/**
						 * Create AndroidFragment and adding to
						 * fragmenttransaction
						 */
						ft.add(R.id.realtabcontent, new AndroidFragment(),
								"android");
					} else {
						/**
						 * Bring to the front, if already exists in the
						 * fragmenttransaction
						 */
						ft.attach(androidFragment);
					}

				} else {
					/** If current tab is apple */
					if (appleFragment == null) {
						/**
						 * Create AppleFragment and adding to
						 * fragmenttransaction
						 */
						ft.add(R.id.realtabcontent, new AppleFragment(),
								"apple");
					} else {
						/**
						 * Bring to the front, if already exists in the
						 * fragmenttransaction
						 */
						ft.attach(appleFragment);
					}
				}
				ft.commit();
			}
		};

		/** Setting tabchangelistener for the tab */
		tHost.setOnTabChangedListener(tabChangeListener);

		/** Defining tab builder for Andriod tab */
		TabHost.TabSpec tSpecAndroid = tHost.newTabSpec("android");
		tSpecAndroid.setIndicator("Android",
				getResources().getDrawable(R.drawable.ic_launcher));
		tSpecAndroid.setContent(new DummyTabContent(getActivity()));
		tHost.addTab(tSpecAndroid);

		/** Defining tab builder for Apple tab */
		TabHost.TabSpec tSpecApple = tHost.newTabSpec("apple");
		tSpecApple.setIndicator("Apple",
				getResources().getDrawable(R.drawable.ic_launcher));
		tSpecApple.setContent(new DummyTabContent(getActivity()));
		tHost.addTab(tSpecApple);

	}

}
