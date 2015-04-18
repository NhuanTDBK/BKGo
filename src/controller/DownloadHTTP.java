package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DownloadHTTP {

	public void getAll() throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
		String URL = "http://localhost:8112/user/files/all";
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(URL);
		CloseableHttpResponse response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream stream = entity.getContent();
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = null;
			try {
				builder = builderFactory.newDocumentBuilder();
				File file = new File("_tmp.xml");
				OutputStream out = new FileOutputStream(file);
				IOUtils.copy(stream, out);
				out.close();
				Document document = builder.parse(new FileInputStream("_tmp.xml"));
				XPath xPath = XPathFactory.newInstance().newXPath();
				String lstDirectoryQuery = "//Directory";
				NodeList nodeListDirectory = (NodeList) xPath.compile(lstDirectoryQuery).evaluate(document,XPathConstants.NODESET);
				for(int i = 0;i<nodeListDirectory.getLength();i++)
				{
					Node node = nodeListDirectory.item(i);
					String name = node.getAttributes().getNamedItem("name").getNodeValue();
					Path pathDir = Paths.get(name);
					Files.createDirectory(pathDir);
			     	System.out.println(node.getAttributes().getNamedItem("name"));
				}
				String lstFileQuery = "//File";
				NodeList nodeListFile = (NodeList) xPath.compile(lstFileQuery).evaluate(document,XPathConstants.NODESET);
				for(int i = 0;i<nodeListFile.getLength();i++)
				{
					Node node = nodeListFile.item(i);
					String fileId = node.getAttributes().getNamedItem("id").getNodeValue();
					String name = node.getAttributes().getNamedItem("name").getNodeValue();
					downloadFile(fileId,name);
			     	System.out.println(name);
				}
				
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

		}
	}
	/*
	 * Download file theo id
	 */
	private void downloadFile(String fileId, String name) {
		// TODO Auto-generated method stub
		String URL = "http://localhost:8112/user/"+fileId;
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(URL);
		CloseableHttpResponse response;
		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity!=null)
			{
				File targetFile = new File(name);
				InputStream stream = entity.getContent();
				OutputStream out = new FileOutputStream(targetFile);
				Date begin = new Date();
				System.out.println("Started: "+begin.getTime());
				IOUtils.copy(stream, out);
				Date end = new Date();
				System.out.println("Ended: "+end.getTime());
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
