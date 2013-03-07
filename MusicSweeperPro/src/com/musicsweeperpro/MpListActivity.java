package com.musicsweeperpro;

import com.expandablelistexample.R;
import com.musicsweeperpro.file.FileHelper;
import com.musicsweeperpro.file.FileHelper.DummyFolder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * An activity representing a list of Mp3s. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link MpDetailActivity} representing item details. On tablets, the activity
 * presents the list of items and item details side-by-side using two vertical
 * panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MpListFragment} and the item details (if present) is a
 * {@link MpDetailFragment}.
 * <p>
 * This activity also implements the required {@link MpListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class MpListActivity extends FragmentActivity implements
		MpListFragment.Callbacks {

	public static final String ARG_GROUP_FILE_PATH = "file_path";
	
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyFolder mGroup;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp_list);

		//Get the file path that was passed from the GroupListActivity
		Bundle passedBundle = getIntent().getExtras();
		if (passedBundle != null) {
			String filePath = passedBundle.getString(ARG_GROUP_FILE_PATH);
			if (filePath != null) {
				// Load the dummy content specified by the fragment
				// arguments. In a real-world scenario, use a Loader
				// to load content from a content provider.
				setCurrentGroup(FileHelper.GROUP_MAP.get(filePath));
				
				// TODO: figure out how to setup the list fragment with the Group's
				//		 List of Items.
				((MpListFragment) getSupportFragmentManager().findFragmentById(
						R.id.mp_list)).setCurrentGroup(getCurrentGroup());				
			}
		}
		//TODO: do something with the code above GT!

		if (findViewById(R.id.mp_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((MpListFragment) getSupportFragmentManager().findFragmentById(
					R.id.mp_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link MpListFragment.Callbacks} indicating that the
	 * item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(MpDetailFragment.ARG_ITEM_ID, id);
			MpDetailFragment fragment = new MpDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.mp_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, MpDetailActivity.class);
			detailIntent.putExtra(MpDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this, GroupListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public DummyFolder getCurrentGroup() {
		return mGroup;
	}

	public void setCurrentGroup(DummyFolder mGroup) {
		this.mGroup = mGroup;
	}	
}
