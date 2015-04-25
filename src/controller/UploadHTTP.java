package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Constants;
import mydropbox.MyDropboxSwing;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class UploadHTTP {
	public static int uploadFile(File file,String fileName, String fileHash) throws IOException
	{
		String url = "http://localhost:8112/user/"+MyDropboxSwing.userId+"/files/file";
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody("file",file,ContentType.create(Files.probeContentType(Paths.get(file.getPath()))),fileName+"@"+fileHash);
		HttpEntity entity = builder.build();
		httpPost.setEntity(entity);
		System.out.println(httpPost.getEntity().toString());
		CloseableHttpResponse response = client.execute(httpPost);
		System.out.println(response.getStatusLine().getStatusCode());
		client.close();
		return 1;
	}
	public static int uploadDirectory(String directoryName) throws ClientProtocolException, IOException
	{

		String url = "http://localhost:8112/user/"+1+"/files/directory";
//		CloseableHttpClient client = HttpClients.createDefault();
//		HttpPost httpPost = new HttpPost(url);
//		StringEntity stringEntity = new StringEntity(directoryName);
//		httpPost.setEntity(stringEntity);
//		System.out.println(httpPost.getEntity().toString());
//		CloseableHttpResponse response = client.execute(httpPost);
//		System.out.println(response.getStatusLine().getStatusCode());
//		client.close();
		ClientResource client = new ClientResource(url);
		StringRepresentation requestEntity = new StringRepresentation(directoryName);
		//System.out.println(client.post(requestEntity).getText());
		Representation res = client.post(requestEntity);
		String fileId = res.getText();
		return Integer.parseInt(fileId);
	}
	public static int uploadDirectory(String directoryName,String tid) throws ClientProtocolException, IOException
	{

		String url = "http://localhost:8112/user/"+1+"/files/directory";
//		CloseableHttpClient client = HttpClients.createDefault();
//		HttpPost httpPost = new HttpPost(url);
//		StringEntity stringEntity = new StringEntity(directoryName);
//		httpPost.setEntity(stringEntity);
//		System.out.println(httpPost.getEntity().toString());
//		CloseableHttpResponse response = client.execute(httpPost);
//		System.out.println(response.getStatusLine().getStatusCode());
//		client.close();
		ClientResource client = new ClientResource(url);
		StringRepresentation requestEntity = new StringRepresentation(directoryName+"@"+tid);
		//System.out.println(client.post(requestEntity).getText());
		Representation res = client.post(requestEntity);
		String fileId = res.getText();
		return Integer.parseInt(fileId);
	}
	public static int patchFile(int type,String tid, String oldName,String newName,Date timestamp)
	{
		String url = "http://localhost:8112/user/"+MyDropboxSwing.userId+"/file/"+oldName;
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPatch httpPatch = new HttpPatch(url);
		DomRepresentation xml;
		try {
			xml = new DomRepresentation();
			xml.setIndenting(true);
		Document doc = xml.getDocument();
		
		Node userNode = doc.createElement("File");
		doc.appendChild(userNode);
		
		Node oldNameNode = doc.createElement("OldName");
		oldNameNode.setTextContent(oldName);
		userNode.appendChild(oldNameNode);
		
		
		Node newNameNode = doc.createElement("NewName");
		newNameNode.setTextContent(newName);
		userNode.appendChild(newNameNode);
		
		Node actionNode = doc.createElement("Type");
		actionNode.setTextContent(Integer.toString(type));
		userNode.appendChild(actionNode);
		
		Node tidNode = doc.createElement("TID");
		tidNode.setTextContent(tid);
		userNode.appendChild(tidNode);

		
		Node timestampNode = doc.createElement("NewName");
		timestampNode.setTextContent(timestamp.toString());
		userNode.appendChild(timestampNode);
		
		httpPatch.setEntity(new StringEntity(xml.toString(),ContentType.TEXT_XML));
		ClientResource cr = new ClientResource(url);
		cr.patch(xml);
		//CloseableHttpResponse response = client.execute(httpPatch);
		 	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
	public synchronized int uploadFile(String fileName,String fileHash,String tid)
	{
		
		String url = "http://localhost:8112/user/"+MyDropboxSwing.userId+"/files/file";
		//String url = "http://localhost:8112/user/"+1+"/files/file";
		DomRepresentation xml;
		String filePath = MyDropboxSwing.urls+"/"+fileName;
		File file = new File(filePath);
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		int fileId = 0;
		try {
			xml = new DomRepresentation();
			xml.setIndenting(true);
		Document doc = xml.getDocument();
		
		Node userNode = doc.createElement("File");
		doc.appendChild(userNode);
		
		Node oldNameNode = doc.createElement("FileName");
		oldNameNode.setTextContent(fileName);
		userNode.appendChild(oldNameNode);
		
		
		Node newNameNode = doc.createElement("FileHash");
		newNameNode.setTextContent(fileHash);
		userNode.appendChild(newNameNode);
		
		Node actionNode = doc.createElement("FileType");
		actionNode.setTextContent(Files.probeContentType(file.toPath()));
		userNode.appendChild(actionNode);
		
		Node fileSizeNode = doc.createElement("FileSize");
		fileSizeNode.setTextContent(Long.toString(file.getTotalSpace()));
		userNode.appendChild(fileSizeNode);

		Node tidNode = doc.createElement("TID");
		tidNode.setTextContent(tid);
		userNode.appendChild(tidNode);
		
		Node timestampNode = doc.createElement("CreateDate");
		timestampNode.setTextContent(format.format(file.lastModified()));
		userNode.appendChild(timestampNode);
		
		Node fileContentNode = doc.createElement("FileContent");
		StringBuilder fileContent = new StringBuilder();
		byte[] arr = FileUtils.readFileToByteArray(file);
		String encode = org.restlet.engine.util.Base64.encode(arr, false);
		//String fileContent = FileUtils.readFileToString(file,StandardCharsets.UTF_8);
		fileContentNode.setTextContent(encode);
		userNode.appendChild(fileContentNode);
		
		ClientResource cr = new ClientResource(url);
		DomRepresentation response = new DomRepresentation(cr.post(xml));
		fileId = Integer.parseInt(response.getText("/File/FileId"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(NumberFormatException ex)
		{
			System.out.println("File da ton tai");
		}
		return fileId;
	}
	public static int uploadFile(String fileName,String tid)
	{
		String url = "http://localhost:8112/user/"+MyDropboxSwing.userId+"/files/file";
		DomRepresentation xml;
		File file = new File(MyDropboxSwing.urls+"/"+fileName);
		String fileHash=null;
		try {
			fileHash = ServerUtil.encryptFile(new FileInputStream(MyDropboxSwing.urls+"/"+fileName));
		} catch (NoSuchAlgorithmException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		int fileId = 0;
		try {
			xml = new DomRepresentation();
			xml.setIndenting(true);
		Document doc = xml.getDocument();
		
		Node userNode = doc.createElement("File");
		doc.appendChild(userNode);
		
		Node oldNameNode = doc.createElement("FileName");
		oldNameNode.setTextContent(fileName);
		userNode.appendChild(oldNameNode);
		
		
		Node newNameNode = doc.createElement("FileHash");
		newNameNode.setTextContent(fileHash);
		userNode.appendChild(newNameNode);
		
		Node actionNode = doc.createElement("FileType");
		actionNode.setTextContent(Files.probeContentType(file.toPath()));
		userNode.appendChild(actionNode);
		
		Node fileSizeNode = doc.createElement("FileSize");
		fileSizeNode.setTextContent(Long.toString(file.getTotalSpace()));
		userNode.appendChild(fileSizeNode);

		Node tidNode = doc.createElement("TID");
		tidNode.setTextContent(tid);
		userNode.appendChild(tidNode);
		
		Node timestampNode = doc.createElement("CreateDate");
		timestampNode.setTextContent(format.format(file.lastModified()));
		userNode.appendChild(timestampNode);
		
		Node fileContentNode = doc.createElement("FileContent");
		StringBuilder fileContent = new StringBuilder();
		byte[] arr = FileUtils.readFileToByteArray(file);
		String encode = org.restlet.engine.util.Base64.encode(arr, false);
		//String fileContent = FileUtils.readFileToString(file,StandardCharsets.UTF_8);
		fileContentNode.setTextContent(encode);
		userNode.appendChild(fileContentNode);
		
		ClientResource cr = new ClientResource(url);
		DomRepresentation response = new DomRepresentation(cr.post(xml));
		fileId = Integer.parseInt(response.getText("/File/FileId"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileId;
	}
//	public static void main(String [] args)
//	{
//		//patchFile(Constants.RENAME,"1", "old","new",new Date());
//		try {
//			String fileHash = ServerUtil.encryptFile(new FileInputStream("power"));
//			UploadHTTP upload = new UploadHTTP();
//			upload.uploadFile("power",fileHash,"2");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
