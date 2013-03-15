/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.musicsweeperpro;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.musicsweeperpro.file.AudioFileArrayAdapter;
import com.musicsweeperpro.file.FilePathComparator;

/**
 * This demo illustrates the use of CHOICE_MODE_MULTIPLE_MODAL, a.k.a. selection mode on ListView
 * couple with the new simple_list_item_activated_1 which uses a highlighted border for selected
 * items.
 */
public class FileListActivity extends ListActivity implements OnClickListener {

	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	public static final String TAG = "FileListActivity";
    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;
    private FilePathComparator fileComparator = new FilePathComparator();
    
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private String mGroup;
	private boolean wasRotated = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null || !wasRotated) {
	        Set<String> filePaths = new HashSet<String>();
	        
			//Get the file path that was passed from the MainActivity
			Bundle passedBundle = getIntent().getExtras();
			if (passedBundle != null) {
				Integer position = passedBundle.getInt(MainActivity.LIST_POS);
	//			String position = passedBundle.getString(MainActivity.LIST_POS);			
				if (position != null) {
					Toast.makeText(getApplicationContext(), MainActivity.LIST_POS + " = " + position, Toast.LENGTH_SHORT);
					String folderPath = MainActivity.groups.get(position);
					File selectedFolder = new File(folderPath);
					for (File file: selectedFolder.listFiles()) {
						if (isMusicFile(file)) {
							filePaths.add(file.getAbsolutePath());
						}
					}
				}
			}        
	        
	        // Gesture detection
	        gestureDetector = new GestureDetector(new MyGestureDetector());
	        gestureListener = new View.OnTouchListener() {
	            public boolean onTouch(View v, MotionEvent event) {
	                return gestureDetector.onTouchEvent(event);
	            }
	        };
	        
	        ListView lv = getListView();
	        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
	        lv.setMultiChoiceModeListener(new ModeCallback());
	        lv.setOnTouchListener(gestureListener);
	        lv.setEmptyView(findViewById(android.R.id.empty));
		    lv.setOnItemClickListener(
				new android.widget.AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
					
						String filePath = getPathFromView(view);
				    	Toast.makeText(getApplicationContext(), "file: "+filePath, Toast.LENGTH_SHORT);
				    	File file = new File(filePath);
				    	if (file != null && file.exists()) {
				    		Toast.makeText(getApplicationContext(), "About to play file: "+filePath, Toast.LENGTH_SHORT);
					    	Intent intent = new Intent();  
					    	intent.setAction(android.content.Intent.ACTION_VIEW);    
					    	intent.setDataAndType(Uri.fromFile(file), "audio/*");  
					    	startActivity(intent);
				    	} else {
				    		Toast.makeText(getApplicationContext(), "File not found "+filePath, Toast.LENGTH_SHORT);
				    	}
					}
			});


		    List<String> filePathList = new ArrayList<String>(filePaths);
		    Collections.sort(filePathList, fileComparator);
	        setListAdapter(new AudioFileArrayAdapter(this, filePathList));//TODO: figure out how to make this work like android.R.layout.simple_list_item_activated_1
//	        setListAdapter(new ArrayAdapter(this,
//	                android.R.layout.simple_list_item_activated_1, filePaths));	        
        }
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActionBar().setSubtitle("Long press to start selection");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
//        Log.d(TAG, "in onConfigurationChanged" );
        wasRotated = true;
        super.onConfigurationChanged(newConfig);
    }
    
    @Override
    public void onClick(View v) {    	
//        Filter f = (Filter) v.getTag();
//        FileListActivity.show(this, input, f);
    }
    
	protected String getPathFromView(View view) {
		RelativeLayout rLayout = (RelativeLayout)view;
		String filePath = (String)((TextView)rLayout.getChildAt(2)).getText();//TODO: see if this gets the path
		return filePath;
	}
    
	/**
	 * Callback method from {@link MpListFragment.Callbacks} indicating that the
	 * item with the given ID was selected.
	 */
//	@Override
//	public void onItemSelected(String id) {
//		File currentGroup = getCurrentGroup();
//
//		// In single-pane mode, simply start the detail activity
//		// for the selected item ID.
//		Intent detailIntent = new Intent(this, MpDetailActivity.class);
//		detailIntent.putExtra(ARG_GROUP_FILE_PATH, currentGroup.id);
//		detailIntent.putExtra(MpDetailFragment.ARG_ITEM_ID, id);
//		startActivity(detailIntent);
//	}

    
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
//			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}    

	public String getCurrentGroup() {
		return mGroup;
	}

	public void setCurrentGroup(String mGroup) {
		this.mGroup = mGroup;
	}	

	private boolean isMusicFile(File file) {
		return (file.isFile() && (file.getName().endsWith(".mp3")
			|| file.getName().endsWith(".wma") || file.getName().endsWith(".ogg")
			|| file.getName().endsWith(".vqf") || file.getName().endsWith(".aac") || file.getName().endsWith(".wav")));
	}	
	
	/** Nested class */
    private class ModeCallback implements ListView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_select_menu, menu);
            mode.setTitle("Select Items");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
            case R.id.menu_share:
                Toast.makeText(FileListActivity.this, "Shared " + getListView().getCheckedItemCount() +
                        " items", Toast.LENGTH_SHORT).show();

                ArrayList<Uri> uris = new ArrayList<Uri>();
                SparseBooleanArray array = getListView().getCheckedItemPositions();

                if (array != null) {
                    for (int i=0; i<array.size(); i++) {
                        if (array.valueAt(i)) {
                            String filePath = (String) getListView().getAdapter().getItem(array.keyAt(i));
                            uris.add(Uri.fromFile(new File(filePath)));
                        }
                    }
                }
                if (uris != null && !uris.isEmpty()) {     
	                Intent email = new Intent();
	                email.setType("audio/wav");
	                email.setAction(Intent.ACTION_SEND_MULTIPLE);
	                email.putExtra(Intent.EXTRA_EMAIL, new String[] {"me@gmail.com"});
	                email.putExtra(Intent.EXTRA_SUBJECT, "Music Sweeper");
	                email.putExtra(Intent.EXTRA_TEXT, "Backup Files");
	                email.putExtra(Intent.EXTRA_STREAM, uris);
	
	                startActivity(Intent.createChooser(email, "Mail job"));
                }
                mode.finish();
                break;
            case R.id.menu_delete:

                SparseBooleanArray array2 = getListView().getCheckedItemPositions();
                File parent = null;
                if (array2 != null) {
                    for (int i=0; i<array2.size(); i++) {
                        if (array2.valueAt(i)) {
                            String filePath = (String) getListView().getAdapter().getItem(array2.keyAt(i));
                            File file = new File(filePath);
                            parent = file.getParentFile();
                            break;
                        }
                    }
                }
          	
//            	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            	intent.setType("folder/*");
//            	intent.setData(Uri.fromFile(parent));
//            	startActivity(Intent.createChooser(intent, "File Explorer"));
                String type = "file/*";
                type = "resource/folder";//This only works for ES file explorer but it works great!
                Uri uri = Uri.fromFile(parent);
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
            	
            	
                mode.finish();
                break;
            default:
                Toast.makeText(FileListActivity.this, "Clicked " + item.getTitle(),
                        Toast.LENGTH_SHORT).show();
                break;
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode,
                int position, long id, boolean checked) {
            final int checkedCount = getListView().getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    mode.setSubtitle(null);
                    break;
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + checkedCount + " items selected");
                    break;
            }
        }
        
    }
    
    class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(FileListActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                    
                    int id = getListView().pointToPosition((int) e1.getX(), (int) e1.getY());
                    String filePath = (String) getListView().getAdapter().getItem(id);

                    if (filePath != null && filePath.length()>0) {
        			Intent detailIntent = new Intent(FileListActivity.this, MpDetailActivity.class);
        			detailIntent.putExtra(MainActivity.FILE_PATH, filePath);
        			startActivity(detailIntent);
                    }
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(FileListActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                    
                    int id = getListView().pointToPosition((int) e1.getX(), (int) e1.getY());
                    String filePath = (String) getListView().getAdapter().getItem(id);

                    if (filePath != null && filePath.length()>0) {
        			Intent detailIntent = new Intent(FileListActivity.this, MpDetailActivity.class);
        			detailIntent.putExtra(MainActivity.FILE_PATH, filePath);
        			startActivity(detailIntent);
                    }
                }
            } catch (Exception e) {
            	Toast.makeText(FileListActivity.this, "Swipe failed:"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return false;
        }

    }    
}
