package com.musicsweeperpro.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class FileHelper {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<DummyFile> ITEMS = new ArrayList<DummyFile>();
	/**
	 * An array of sample (dummy) groups.
	 */
	public static List<DummyFolder> GROUPS = new ArrayList<DummyFolder>();	

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, DummyFile> ITEM_MAP = new HashMap<String, DummyFile>();
	
	/**
	 * A map of sample (dummy) groups, by ID.
	 */
	public static Map<String, DummyFolder> GROUP_MAP = new HashMap<String, DummyFolder>();

	static {
		// Add 3 sample items.
		addGroup("1", "Group 1", new DummyFile[]{
				new DummyFile("1", "1", "Item 1"),
				new DummyFile("2", "1", "Item 2"),
				new DummyFile("3", "1", "Item 3")
				});
		addGroup("2", "Group 2", new DummyFile[]{
				new DummyFile("1", "2", "Item 1"),
				new DummyFile("2", "2", "Item 2"),
				new DummyFile("3", "2", "Item 3")
				});		
		//addItem(new DummyFile("1", "Item 1"));
		//addItem(new DummyFile("2", "Item 2"));
		//addItem(new DummyFile("3", "Item 3"));
	}

	private static void addGroup(String id, String content, DummyFile[] items) {
		Map<String, DummyFile> dummies = new HashMap<String, DummyFile>();
		List<DummyFile> dummieList = new ArrayList<DummyFile>();
		for (DummyFile item: items) {
			dummies.put(item.id, item);
			dummieList.add(item);
			
			ITEMS.add(item);
			ITEM_MAP.put(item.id, item);
		}
		DummyFolder group = new DummyFolder(id, content, dummieList);
		GROUPS.add(group);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class DummyFile {
		public String id;
		public String groupId;
		public String content;

		public DummyFile(String id, String groupId, String content) {
			this.id = id;
			this.groupId = groupId;
			this.content = content;		
		}

		@Override
		public String toString() {
			return content;
		}
	}
	
	/**
	 * A dummy item representing a piece of content.
	 */
	public static class DummyFolder {
		public String id;
		public String content;
		public Map<String, DummyFile> dummies = new HashMap<String, DummyFile>();
		List<DummyFile> dummieList = new ArrayList<DummyFile>();

		public List<DummyFile> getDummieList() {
			return dummieList;
		}

		public DummyFolder(String id, String content, DummyFile[] items) {
			this.id = id;
			this.content = content;
			for (DummyFile item: items) {
				dummies.put(item.id, item);
				dummieList.add(item);
			}			
		}

		public DummyFolder(String id2, String content2,
				List<DummyFile> dummieList) {
			this.id = id2;
			this.content = content2;
			this.dummieList = dummieList;
			for (DummyFile item: dummieList) {
				dummies.put(item.id, item);
			}	
		}

		@Override
		public String toString() {
			return content;
		}
	}	
}
