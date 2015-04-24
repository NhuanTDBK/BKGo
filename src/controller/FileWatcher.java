/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import static mydropbox.MyDropboxSwing.diffList;
import static mydropbox.MyDropboxSwing.jTextArea1;
import static mydropbox.MyDropboxSwing.list;
import static mydropbox.MyDropboxSwing.lstCommit;
import static mydropbox.MyDropboxSwing.urls;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Constants;
import model.FileAttr;
import model.FileCreate;
import model.FileDelete;
import model.FileUpdate;
import model.XmlFactory;
import mydropbox.MyDropboxSwing;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class FileWatcher implements FileAlterationListener {

	SimpleDateFormat format = new  SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	public static String log = "";
	@Override
	public void onDirectoryChange(File arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDirectoryCreate(File arg0) {
		// TODO Auto-generated method stub
		String directoryName = ServerUtil.convertPath(arg0.getAbsolutePath(), MyDropboxSwing.urls);
		log +="Directory is created "+ arg0.getName()+"\n";
		System.out.println(log);
		jTextArea1.append(log);
		int tid = TransactionHTTP.getTransaction()+1;
		FileCreate fileCreate = new FileCreate(directoryName, Constants.IS_FOLDER);
		lstCommit.add(fileCreate);
	}

	@Override
	public void onDirectoryDelete(File arg0) {
		// TODO Auto-generated method stub
		String directoryName = ServerUtil.convertPath(arg0.getAbsolutePath(), MyDropboxSwing.urls);
		log +="Directory is delete "+ arg0.getName()+"\n";
		System.out.println(log);
		jTextArea1.append(log);
		XmlFactory xml = new XmlFactory();
		int fileId = xml.getFileIdByFileName(directoryName);
		FileDelete fileDelete = new FileDelete(fileId, directoryName, Constants.IS_FOLDER);
		lstCommit.add(fileDelete);
//		DeleteHTTP.deleteFileByFileName(directoryName);
	}

	@Override
	public void onFileChange(File arg0) {
		diffList.clear(); 
		addAttr(urls);
		//System.out.println("File is changed " + arg0.getAbsolutePath() + " and size: "+format.format(arg0.lastModified()));
		log += "File is changed " + arg0.getAbsolutePath() + " and size: "+format.format(arg0.lastModified()) +"\n";
		String fileName = ServerUtil.convertPath(arg0.getAbsolutePath(), MyDropboxSwing.urls);
		System.out.println(log);
		jTextArea1.append(log);
		list.clear();
		list.addAll(diffList);
		FileUpdate fileCreate = new FileUpdate(fileName,Constants.IS_FILE);
		lstCommit.add(fileCreate);
	}

	@Override
	public void onFileCreate(File arg0) {
		//System.out.println("File is created " + arg0.getAbsolutePath() + " and size: "+arg0.getUsableSpace());
		diffList.clear();
		addAttr(urls);
		String fileName = ServerUtil.convertPath(arg0.getAbsolutePath(), MyDropboxSwing.urls);
		FileCreate fileCreate = new FileCreate(fileName,Constants.IS_FILE);
		lstCommit.add(fileCreate);
		if(diffList.size() > list.size()){
			for(int i = 0 ; i<diffList.size(); i++){
				int count = 0;
				for(int j = 0; j<list.size(); j++){
					if(list.get(j).fileKey.equals(diffList.get(i).fileKey)){
						count++;
					}
				}
				if(count == 0){
					//System.out.println("File create: "+diffList.get(i).fileParent+"/"+diffList.get(i).name);
					log += "File create: "+diffList.get(i).fileParent+"/"+diffList.get(i).fileName +"\n";
					System.out.println(log);
					jTextArea1.append(log);

				}
			}
		}else{
			compareArr();
		}
		list.clear();
		list.addAll(diffList);
	}

	@Override
	public void onFileDelete(File arg0) {
		// TODO Auto-generated method stub
		//System.out.println("File is deleted " + arg0.getAbsolutePath() + " and size: "+arg0.getUsableSpace());
		diffList.clear(); 
		addAttr(urls);
		String fileName = ServerUtil.convertPath(arg0.getAbsolutePath(), MyDropboxSwing.urls);
//		DeleteHTTP.deleteFileByFileName(fileName);
//		UploadHTTP upload = new UploadHTTP();
		if(diffList.size() < list.size()){
			for(int i = 0 ; i<list.size(); i++){
				int count = 0;
				for(int j = 0; j<diffList.size(); j++){
					if(list.get(i).fileKey.equals(diffList.get(j).fileKey)){
						count++;
					}
				}
				if(count == 0){
					//System.out.println("File delete: "+list.get(i).fileParent+"/"+list.get(i).name);
					log += "File delete: "+list.get(i).fileParent+"/"+list.get(i).fileName +"\n";
					System.out.println(log);
					jTextArea1.append(log);
					XmlFactory xml = new XmlFactory();
					int fileId = xml.getFileIdByFileName(fileName);
					FileDelete fileDelete = new FileDelete(fileId, fileName, Constants.IS_FILE);
					lstCommit.add(fileDelete);
				}
			}
			list.clear();
			list.addAll(diffList);
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

	public void compareArr(){
		for(int i = 0; i < diffList.size(); i++){
			for(int j = 0; j < list.size(); j++){
				if(diffList.get(i).fileKey.equals(list.get(j).fileKey)){
					if(!diffList.get(i).fileName.equals(list.get(j).fileName)){
						//System.out.println("File rename:"+list.get(j).name+"->"+diffList.get(i).name);
						log += "File rename:"+list.get(j).fileName+"->"+diffList.get(i).fileName +"\n";
						System.out.println(log);
						jTextArea1.append(log);
					}
					if(!diffList.get(i).fileParent.equals(list.get(j).fileParent)){
						//System.out.println("File move:"+list.get(j).fileParent+"/"+list.get(j).name+"->"+diffList.get(i).fileParent+"/"+diffList.get(i).name);
						log += "File move:"+list.get(j).fileParent+"/"+list.get(j).fileName+"->"+diffList.get(i).fileParent+"/"+diffList.get(i).fileName +"\n";
						System.out.println(log);
						jTextArea1.append(log);
					}
				}
			}
		}
	}

	public void addAttrList(String fileParent_dir) {
		File file = new File(fileParent_dir);
		BasicFileAttributes attr = null;
		if(!file.isDirectory()){
			try {
				attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			} catch (IOException ex) {
				Logger.getLogger(FileWatcher.class.getName()).log(Level.SEVERE, null, ex);
			}
			list.add(new FileAttr(file.getName(), attr.fileKey().toString(), file.getParent()));
		}else{
			File[] arr = file.listFiles();
			for(int i = 0; i < file.listFiles().length; i++){               
				File child_file = arr[i];
				if(child_file.isDirectory()){
					try {
						attr = Files.readAttributes(child_file.toPath(), BasicFileAttributes.class);
					} catch (IOException ex) {
						Logger.getLogger(FileWatcher.class.getName()).log(Level.SEVERE, null, ex);
					}
					list.add(new FileAttr(child_file.getName(), attr.fileKey().toString(), child_file.getParent()));
				}
				addAttr(child_file.toString());
			}
		}
	}

	public void addAttr(String fileParent_dir) {
		File file = new File(fileParent_dir);
		BasicFileAttributes attr = null;
		if(!file.isDirectory()){
			try {
				attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			} catch (IOException ex) {
				Logger.getLogger(FileWatcher.class.getName()).log(Level.SEVERE, null, ex);
			}
			diffList.add(new FileAttr(file.getName(), attr.fileKey().toString(), file.getParent()));
		}else{
			File[] arr = file.listFiles();
			for(int i = 0; i < file.listFiles().length; i++){               
				File child_file = arr[i];
				if(child_file.isDirectory()){
					try {
						attr = Files.readAttributes(child_file.toPath(), BasicFileAttributes.class);
					} catch (IOException ex) {
						Logger.getLogger(FileWatcher.class.getName()).log(Level.SEVERE, null, ex);
					}
					diffList.add(new FileAttr(child_file.getName(), attr.fileKey().toString(), child_file.getParent()));
				}
				addAttr(child_file.toString());
			}
		}
	}
}