package com.musicsweeperpro;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * An activity representing a single Mp3 detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link MpListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link MpDetailFragment}.
 */
public class MpDetailActivity extends FragmentActivity {
	private static final String TAG = "MpDetailActivity";
	protected File mItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(MainActivity.FILE_PATH, getIntent()
					.getStringExtra(MainActivity.FILE_PATH));			

			MpDetailFragment fragment = new MpDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.mp_detail_container, fragment).commit();
		}
		
		startActionMode(mContentSelectionActionModeCallback);
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
//			NavUtils.navigateUpTo(this, new Intent(this, FileListActivity.class));
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	  private ActionMode.Callback mContentSelectionActionModeCallback = 
		       new ActionMode.Callback() {
	      public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	    	  Uri uri = null;
	          switch (item.getItemId()) {
	          case R.id.share:
	              
	              uri = Uri.fromFile(mItem);
	              
	              if (uri != null) {     
		                Intent email = new Intent();
		                email.setType("audio/wav");
		                email.setAction(Intent.ACTION_SEND);
		                email.putExtra(Intent.EXTRA_EMAIL, new String[] {"me@gmail.com"});
		                email.putExtra(Intent.EXTRA_SUBJECT, "Music Sweeper");
		                email.putExtra(Intent.EXTRA_TEXT, "Backup File");
		                email.putExtra(Intent.EXTRA_STREAM, uri);
		
		                startActivity(Intent.createChooser(email, "Mail job"));
	              }
	              mode.finish();
	              break;
	          case R.id.delete:
	
	              String type = "file/*";
	              type = "resource/folder";//This only works for ES file explorer but it works great!
	              File parent = mItem.getParentFile();
	              uri = Uri.fromFile(parent);
	              
	              if (uri != null) {
		              Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		              intent.setDataAndType(uri, type);
		              intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		              try {
		                  startActivity(intent);
		              } catch(Exception e) {
		                  try {
		                      intent = new Intent(Intent.ACTION_GET_CONTENT, uri);
		                      intent.setDataAndType(uri, type);
		                      startActivity(intent);
		                  } catch (Exception e1) {
		                      Log.e(TAG, "folder intent failed", e1);
		                  }
		              }
	              }
	          	
	              mode.finish();
	              break;
	          default:
	              Toast.makeText(MpDetailActivity.this, "Clicked " + item.getTitle(),
	                      Toast.LENGTH_SHORT).show();
	              break;
	          }
	          return true;
	      }

		@Override
		public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
		    // Inflate the menu; this adds items to the action bar if it is present.
		    getMenuInflater().inflate(R.menu.list_select_menu, menu);
		    return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			// TODO Auto-generated method stub
			return false;
		}
	  };
}
