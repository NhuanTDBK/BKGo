package controller;

import org.restlet.ext.xml.DomRepresentation;
import org.restlet.resource.ClientResource;

import mydropbox.MyDropboxSwing;

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
	public static void main(String [] args)
	{
		System.out.println(getTransaction());
	}
}
