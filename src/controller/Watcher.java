package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import model.FileAttr;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class Watcher {
	public static ArrayList<FileAttr> list;
	public static ArrayList<FileAttr> diffList;
	public static String home_dir = "/home/nhuan/Dropbox";

	public static void addAttr(String parent_dir) throws IOException {
		File file = new File(parent_dir);
		BasicFileAttributes attr = null;
		if (!file.isDirectory()) {
			attr = Files.readAttributes(file.toPath(),
					BasicFileAttributes.class);
			list.add(new FileAttr(file.getName(), attr.fileKey().toString(),
					file.getParent()));
		} else {
			File[] arr = file.listFiles();
			for (int i = 0; i < file.listFiles().length; i++) {
				File child_file = arr[i];
				if (child_file.isDirectory()) {
					attr = Files.readAttributes(child_file.toPath(),
							BasicFileAttributes.class);
					list.add(new FileAttr(child_file.getName(), attr.fileKey()
							.toString(), child_file.getParent()));
				}
				addAttr(child_file.toString());
			}
		}
	}
	public Watcher() throws IOException
	{
		list = new ArrayList<>();
		diffList = new ArrayList<>();
		final File directory = new File(home_dir);
		addAttr(home_dir);
		// for(int i = 0; i < list.size(); i++){
		// String t = list.get(i).dev + "|" + list.get(i).name + "|" +
		// list.get(i).parent;
		// System.out.println(t);
		// }
		FileAlterationObserver fao = new FileAlterationObserver(directory);
		fao.addListener(new FileWatcher());
		final FileAlterationMonitor watcher = new FileAlterationMonitor();
		watcher.addObserver(fao);
		System.out.println("Starting monitor. Ctrc - C to stop");
		System.out.println(list.size());
		System.out.println(diffList.size());
		try {
			watcher.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					watcher.stop();
					System.out.println("Stopped monitor");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}));
	}
}
