package com.musicsweeperpro;

import java.io.File;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single Mp3 detail screen. This fragment is either
 * contained in a {@link MpListActivity} in two-pane mode (on tablets) or a
 * {@link MpDetailActivity} on handsets.
 */
public class MpDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MpDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(MainActivity.FILE_PATH)) {
			String filePath = getArguments().getString(MainActivity.FILE_PATH);
			((MpDetailActivity)getActivity()).mItem = new File(filePath);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mp_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (((MpDetailActivity)getActivity()).mItem == null) {
			((TextView) rootView.findViewById(R.id.mp_detail))
					.setText("Item Not Found!");
		} else {
			((TextView) rootView.findViewById(R.id.mp_detail))
			.setText(((MpDetailActivity)getActivity()).mItem.getPath());			
		}

		return rootView;
	}
}
