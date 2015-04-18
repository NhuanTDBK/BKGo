/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class FileWatcher implements FileAlterationListener {
	String txt = "";
	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	@Override
	public void onDirectoryChange(File arg0) {
		// TODO Auto-generated method stub
		// System.out.println("Directory is changed " +
		// arg0.getAbsolutePath()+"\n");
		// txt += "Directory is changed " + arg0.getAbsolutePath()+"\n";
		// mydropbox.MyDropboxSwing.jTextArea1.setText(txt);
	}

	@Override
	public void onDirectoryCreate(File arg0) {
		// TODO Auto-generated method stub
		System.out.println("Directory is created " + arg0.getAbsolutePath()
				+ "\n");
		txt += "Directory is created " + arg0.getAbsolutePath() + "\n";
		mydropbox.MyDropboxSwing.jTextArea1.setText(txt);
	}

	@Override
	public void onDirectoryDelete(File arg0) {
		// TODO Auto-generated method stub
		System.out.println("Directory is deleted " + arg0.getAbsolutePath()
				+ "\n");
		txt += "Directory is deleted " + arg0.getAbsolutePath() + "\n";

		mydropbox.MyDropboxSwing.jTextArea1.setText(txt);
	}

	@Override
	public void onFileChange(File arg0) {
		if (!Files.isSymbolicLink(arg0.toPath()))
		{
		Path path = Paths.get(arg0.getAbsolutePath());
		try {
			BasicFileAttributes attr = Files.readAttributes(path,
					BasicFileAttributes.class);
			System.out.println("File is changed " + arg0.getAbsolutePath()
					+ " and size: " + format.format(arg0.lastModified())
					+ attr.fileKey());
			txt += "File is changed " + arg0.getAbsolutePath() + " and size: "
					+ format.format(arg0.lastModified()) + "\n";
			mydropbox.MyDropboxSwing.jTextArea1.setText(txt);
			System.out.println(txt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	@Override
	public void onFileCreate(File arg0) {
		if (!Files.isSymbolicLink(arg0.toPath()))
		{
		Path path = Paths.get(arg0.getAbsolutePath());
		try {
			BasicFileAttributes attr = Files.readAttributes(path,
					BasicFileAttributes.class);
			System.out.println("File is created " + arg0.getAbsolutePath()
					+ " and size: " + format.format(arg0.lastModified())
					+ attr.fileKey());
			txt += "File is created " + arg0.getAbsolutePath() + " and size: "
					+ format.format(arg0.lastModified()) + "\n";
			mydropbox.MyDropboxSwing.jTextArea1.setText(txt);
			System.out.println(txt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	@Override
	public void onFileDelete(File arg0) {
		// TODO Auto-generated method stub
		if (!Files.isSymbolicLink(arg0.toPath())) {
			System.out.println("File is deleted " + arg0.getAbsolutePath()
					+ " and size: " + arg0.getUsableSpace());
			txt += "File is deleted " + arg0.getAbsolutePath() + " and size: "
					+ arg0.getUsableSpace() + "\n";
			mydropbox.MyDropboxSwing.jTextArea1.setText(txt);
			System.out.println(txt);
		}
	}

	@Override
	public void onStart(FileAlterationObserver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop(FileAlterationObserver arg0) {
		// TODO Auto-generated method stub

	}

}
