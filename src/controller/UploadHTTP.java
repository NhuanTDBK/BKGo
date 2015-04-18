package controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class UploadHTTP {
	public static int uploadFile(File file,String fileName, String fileHash) throws IOException
	{
		String url = "http://localhost:8112/user/files/file";
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
		return 0;
	}
	public static void uploadDirectory(String directoryName) throws ClientProtocolException, IOException
	{

		String url = "http://localhost:8112/user/files/directory";
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		StringEntity stringEntity = new StringEntity(directoryName);
		httpPost.setEntity(stringEntity);
		System.out.println(httpPost.getEntity().toString());
		CloseableHttpResponse response = client.execute(httpPost);
		System.out.println(response.getStatusLine().getStatusCode());
		client.close();
		
	}
}
