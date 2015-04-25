package controller;

import java.io.IOException;
import java.util.List;

import model.FileChange;
import model.XmlFactory;
import mydropbox.MyDropboxSwing;

import org.restlet.ext.xml.DomRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;

public class TransactionHTTP {
	public static int getTransaction()
	{
		int tid = 0;
		String url = "http://localhost:8112/user/"+MyDropboxSwing.userId+"/transaction";
		ClientResource client = new ClientResource(url);
		try{
			DomRepresentation dom = new DomRepresentation(client.get());
			tid = Integer.parseInt(dom.getText("/Transaction/TID"));
		}
		catch(NumberFormatException ex)
		{
			System.out.println("Loi chuyen so");
		}
		return tid;
	}
	public DomRepresentation getSync(String tid)
	{
		DomRepresentation dom=null;
		String url = "http://localhost:8112/user/"+1+"/sync/0";
		ClientResource client = new ClientResource(url);
		try{
			dom = new DomRepresentation(client.get());
			Document doc = dom.getDocument();
		}
		catch(ResourceException ex)
		{
			System.out.println("Loi chuyen so");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dom;
	}
//	public static void main(String [] args)
//	{
//		TransactionHTTP http = new TransactionHTTP();
//		http.getSync("0");
//	}
}
