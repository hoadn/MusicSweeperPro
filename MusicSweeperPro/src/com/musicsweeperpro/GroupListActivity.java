package com.musicsweeperpro;

import com.expandablelistexample.R;
import com.musicsweeperpro.file.FileHelper.DummyFolder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * An activity representing a list of Groups(folders) that contains Mp3s. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link GroupDetailActivity} representing item details. On tablets, the activity
 * presents the list of items and item details side-by-side using two vertical
 * panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link GroupListFragment} and the item details (if present) is a
 * {@link GroupDetailFragment}.
 * <p>
 * This activity also implements the required {@link MpListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class GroupListActivity extends FragmentActivity implements
		GroupListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);

//		if (findViewById(R.id.mp_detail_container) != null) {
//			// The detail container view will be present only in the
//			// large-screen layouts (res/values-large and
//			// res/values-sw600dp). If this view is present, then the
//			// activity should be in two-pane mode.
//			mTwoPane = true;
//
//			// In two-pane mode, list items should be given the
//			// 'activated' state when touched.
//			((MpListFragment) getSupportFragmentManager().findFragmentById(
//					R.id.mp_list)).setActivateOnItemClick(true);
//		}

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
/*			Bundle arguments = new Bundle();
			arguments.putString(GroupDetailFragment.ARG_ITEM_ID, id);
			GroupDetailFragment fragment = new GroupDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.mp_detail_container, fragment).commit();
			TODO: Figure out what to do here. Should I have a Group Detail? 		*/

		} else {
			// Try to set the group in the list fragment
			DummyFolder group = null;
			GroupListFragment gFrag = ((GroupListFragment) getSupportFragmentManager().findFragmentById(
					R.id.group_list));
			if (gFrag != null) {
				group = gFrag.getCurrentGroup();
			}

			MpListFragment mFrag = ((MpListFragment) getSupportFragmentManager().findFragmentById(
					R.id.mp_list));
			if (mFrag != null && group != null) {
				mFrag.setCurrentGroup(group);
			}
			
			// In single-pane mode, simply start the mp3 list activity
			// for the selected item ID (group).
			Intent mp3ListIntent = new Intent(this, MpListActivity.class);
			mp3ListIntent.putExtra(MpListActivity.ARG_GROUP_FILE_PATH, id);
			startActivity(mp3ListIntent);
		}
	}
}
