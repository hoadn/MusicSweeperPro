package com.musicsweeperpro.file;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.musicsweeperpro.R;

public class AudioFileArrayAdapter extends ArrayAdapter<String> {
	  private static final String TAG = "AudioFileArrayAdapter";
	  private final Activity context;
	  private final List<String> filePaths;

	  public AudioFileArrayAdapter(Activity context, List<String> filePaths) {
	    super(context, R.layout.file_view, filePaths);
	    this.context = context;
	    this.filePaths = filePaths;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		  ViewHolder viewHolder = null;
		  if (convertView == null || convertView.getTag() == null) {
		      LayoutInflater inflater = context.getLayoutInflater();
//		      convertView = inflater.inflate(android.R.layout.simple_list_item_activated_1, null);
		      convertView = inflater.inflate(R.layout.file_view, null);
		      viewHolder = createViewHolder(position, convertView);
	//	      viewHolder.path = (TextView) convertView.findViewById(R.id.path);
	//	      viewHolder.artist = (TextView) convertView.findViewById(R.id.artist);
	//	      viewHolder.fileName = (TextView) convertView
	//	          .findViewById(R.id.file_name);
		      convertView.setTag(viewHolder);
	    }

		viewHolder = createViewHolder(position, convertView);
	    String filePath = filePaths.get(position);
//	    viewHolder.text.setText(s);	    
//	    String s = filePaths[position];
//		  String filePath = (String)((TextView)convertView).getText();
		  File file = new File(filePath);
		  if (file == null || !file.exists()) {
			  viewHolder.path.setText("Bad File");
		  } else {
			boolean isId3 = setId3Tag(file, viewHolder);
//			String ident = "Folder: ";
//			if (isId3) {
//				ident = "Artist: ";
//				viewHolder.fileName.setText(mp3.getTitle());
//	//			TODO: fix this once I figure out how to read id3 tag data from mp3 header
////				holder.album.setText("Album: "+mp3.getAlbum());
//			}
//			viewHolder.artist.setText(ident+mp3.getArtist());
		  }

	    return convertView;
	  }
	  
	  private boolean setId3Tag(File file, ViewHolder viewHolder) {	  
		  viewHolder.fileName.setText(file.getName());
		  viewHolder.path.setText(file.getPath());
		  viewHolder.artist.setText("Artist: ");
		  try {
			MediaMetadataRetriever mmr = new MediaMetadataRetriever();
			mmr.setDataSource(file.getPath());
			viewHolder.artist.setText(viewHolder.artist.getText() + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST));
//				viewHolder.album.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));	
//				viewHolder.setTitle(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));			
			return true;
		} catch (Exception e) {
			viewHolder.artist.setText("Artist: unknown");
			Log.e(TAG, "Could not parse ID3TagData for file: "+file.getAbsolutePath());
			return false;
		}
	}

		private ViewHolder createViewHolder(final int position, View convertView) {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null || (holder.path == null || holder.fileName == null || holder.artist == null)) {
				holder = new ViewHolder();
				holder.fileName = (TextView) convertView.findViewById(R.id.file_name);
				holder.artist = (TextView) convertView.findViewById(R.id.artist);
				holder.path = (TextView) convertView.findViewById(R.id.path);
			}
	        return holder;
		}
		
		static class ViewHolder {
			TextView path;
			TextView fileName;
			TextView artist;
		}
	} 
