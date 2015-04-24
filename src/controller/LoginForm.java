package controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.restlet.ext.xml.DomRepresentation;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class LoginForm {
	public static DomRepresentation result(String userName,String password) throws ClientProtocolException, IOException
	{
		String url = "http://localhost:8112/login";
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("userName", userName));
		httpPost.setEntity(new UrlEncodedFormEntity(param));
		CloseableHttpResponse response = client.execute(httpPost);
		//System.out.println(response.getStatusLine().getStatusCode());
		//System.out.println(EntityUtils.toString(response.getEntity()));
		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(response.getEntity().getContent());
			DomRepresentation dom = new DomRepresentation();
			dom.setDocument(doc);
			return dom;
		} catch (ParserConfigurationException | IllegalStateException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	}
}
