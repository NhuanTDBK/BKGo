package model;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;

import mydropbox.MyDropboxSwing;

import org.apache.http.client.ClientProtocolException;

import controller.DownloadHTTP;
import controller.UploadHTTP;

public class FileCreate extends FileChange {


	public FileCreate(String fileName,int isFile)
	{
		this.setType(Constants.CREATE);
		this.setFileName(fileName);
		//		this.setFileId(fileId);
		//		this.setTid(tid);
		//		this.setTimestamp(timestamp);
		this.setIsFile(isFile);
	}

	@Override
	public void doAction() {
		int id=0;
		if(this.getIsFile()==Constants.IS_FILE)
			id = UploadHTTP.uploadFile(this.getFileName(),this.getTid());
		else
			try {
				id = UploadHTTP.uploadDirectory(this.getFileName(), this.getTid());
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		this.setFileId(id);
		//Append du lieu vao file xml
		XmlFactory fac = new XmlFactory(MyDropboxSwing.dom);
		fac.insertFileNode(this);
	}
	@Override
	public void doUpdate() {
		// TODO Auto-generated method stub
		if(this.getIsFile()==Constants.IS_FILE)
		{
			DownloadHTTP download = new DownloadHTTP();
			download.downloadFile(Integer.toString(this.getFileId()),this.getFileName());
		}
		else
		{
			String path = MyDropboxSwing.urls+"/"+this.getFileName();
			try{
				Path filePath = Paths.get(path);
				Files.createDirectory(filePath);
			}catch(InvalidPathException ex)
			{
				System.out.println("Duong dan khong ton tai");
			} catch (FileAlreadyExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Duong dan da ton tai");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
