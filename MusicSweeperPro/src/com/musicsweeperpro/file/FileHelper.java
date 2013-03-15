package com.musicsweeperpro.file;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

public class FileHelper {
	private static final String TAG = "FileHelper";
//	public static List<File> GROUPS = new ArrayList<File>();
	private List<String> filePathExclusions = new ArrayList<String>();
	private boolean externalStorage, extraExternalStorage;
	private String extraExternalStorageDirString;
	private String rootDirString;
	private static SharedPreferences prefs = null;

	public FileHelper(SharedPreferences prefs2) {
		prefs = prefs2;
	}

	public List<String> getAudioFolderPaths(URI uri) {
		List<String> list = new ArrayList<String>();
		if (hasExternalStorage()) {
			File file = new File(uri);
			if (file != null) {
				buildFilePathExclusions();
				Set<String> set = new HashSet<String>();
				getAudioFolders(file, set);
				if (!set.isEmpty()) {
					list.addAll(set);
					Collections.sort(list);
					return list;
				}
			}
		}
		Log.e(TAG, "No Files Found!");
		return list;		
	}

	/** recursively finds and returns file paths of folders that contain audio files  */
	private void getAudioFolders(File file, Set<String> files) {
		if (file != null && !filePathExclusions.contains(file.getAbsolutePath())) {
			if (file.isDirectory()) {
				if (containsAudioFiles(file)) {
					files.add(file.getAbsolutePath());
				}
				File[] children = file.listFiles();
				if (children != null) {
					for (File child : children) {
						getAudioFolders(child, files);
					}
				}
			}
		}
	}

	private boolean containsAudioFiles(File file) {
		File[] children = file.listFiles();
		if (children != null) {
			for (File child : children) {
				if (isMusicFile(child)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isMusicFile(File file) {
		if (prefs != null) {
			return (file.isFile() && ( 
					(file.getName().endsWith(".mp3") && prefs.getBoolean("mp3", true)) 
					|| (file.getName().endsWith(".wma") && prefs.getBoolean("wma", true))
					|| (file.getName().endsWith(".ogg") && prefs.getBoolean("ogg", true))
					|| (file.getName().endsWith(".vqf") && prefs.getBoolean("vqf", true))
					|| (file.getName().endsWith(".aac") && prefs.getBoolean("aac", true))
					|| (file.getName().endsWith(".wav") && prefs.getBoolean("wav", true))
					));
		} else {
		return (file.isFile() && (file.getName().endsWith(".mp3")
			|| file.getName().endsWith(".wma") || file.getName().endsWith(".ogg")
			|| file.getName().endsWith(".vqf") || file.getName().endsWith(".aac") || file.getName().endsWith(".wav")));
		}
	}

	public boolean hasExternalStorage() {
		if (!this.externalStorage) {
			File root = Environment.getExternalStorageDirectory();
		    if (root.canWrite()){
		    	this.externalStorage = true;
		    	this.rootDirString = root.getAbsolutePath();
		    }
		}
		return this.externalStorage;
	}
	
	//TODO: figure out how to get mnt/sd_card/external_sd using the environment variable
	public boolean hasExtraExternalStorage()
	{
		if (!this.externalStorage) {
		    File root = Environment.getExternalStorageDirectory();
		    if (root.canWrite()){
		    	String extraStoragePath = root.getAbsolutePath() + "/external_sd";
		    	File extraStorageRoot = new File(extraStoragePath);
		    	if (extraStorageRoot.exists()) {
		    		extraExternalStorage = true;
		    		extraExternalStorageDirString = extraStorageRoot.getAbsolutePath();
		    	}
		    }
		}
		return extraExternalStorage;
	}
	
	private void buildFilePathExclusions() {
		if (filePathExclusions == null || filePathExclusions.isEmpty()) {
			filePathExclusions = new ArrayList<String>();
			filePathExclusions.add((this.rootDirString+"/backups"));
			filePathExclusions.add((this.rootDirString+"/.android_secure"));
		}
	}	
}
