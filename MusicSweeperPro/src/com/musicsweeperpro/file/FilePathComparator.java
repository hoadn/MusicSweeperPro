package com.musicsweeperpro.file;

import java.util.Comparator;

public class FilePathComparator implements Comparator<String> {
	  
		public int compare(String file1, String file2) {
			if (file1 == null || file2 == null)
				return -1;
			return file1.toLowerCase().compareTo(file2.toLowerCase());

		}
}
