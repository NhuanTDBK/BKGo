package model;

import java.io.IOException;
import java.util.Date;

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
		DownloadHTTP download = new DownloadHTTP();
		download.downloadFile(Integer.toString(this.getFileId()),this.getFileName());
	}
}
