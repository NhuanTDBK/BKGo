package controller;


import java.io.IOException;
import java.util.List;

import model.FileChange;
import model.FileChangeDAO;
import model.FileCursor;
import model.MemCached;
import model.XMLFactory;

import org.hibernate.HibernateException;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.StringRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import utils.ServerUtil;

public class TransactionController extends Restlet{
	public static MemCached memCached = new MemCached();
	@Override
	public void handle(Request request, Response response) {
		// TODO Auto-generated method stub
		Method method = request.getMethod();
		String lastSegment = request.getCurrent().getResourceRef().getLastSegment();
		String userIdStr = (String)request.getAttributes().get("userId");
		//response.setEntity(new StringRepresentation(lastSegment));
		if(lastSegment.equals("transaction"))
		{
			if(method.equals(Method.GET))
			{
				int latestTransaction=0;
				int index=0;
				try{
					try{
						List lst = FileChangeDAO.getCursor();
						latestTransaction =Integer.parseInt(lst.get(0).toString());
						index = Integer.parseInt(lst.get(1).toString());
					}catch(IndexOutOfBoundsException ex)
					{

					}
					DomRepresentation xml = new DomRepresentation();
					xml.setIndenting(true);
					Document doc = xml.getDocument();

					Node userNode = doc.createElement("Transaction");
					doc.appendChild(userNode);

					Node oldNameNode = doc.createElement("TID");
					oldNameNode.setTextContent(Integer.toString(latestTransaction));
					userNode.appendChild(oldNameNode);

					Node indexNode = doc.createElement("Index");
					indexNode.setTextContent(Integer.toString(index));
					userNode.appendChild(indexNode);

					response.setEntity(xml);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response.setStatus(Status.SERVER_ERROR_SERVICE_UNAVAILABLE);
				}
				catch(IndexOutOfBoundsException ex)
				{
					ex.printStackTrace();
					response.setStatus(Status.SERVER_ERROR_SERVICE_UNAVAILABLE);
				}
			}
			else
			{
				try {
					String requestStr = request.getEntity().getText();
					String tidStr;
					String index;
					int tid = 0;
					try{
						List<String> lst = ServerUtil.getToken(requestStr);
						tidStr = lst.get(0);
						tid = Integer.parseInt(tidStr);
						index = lst.get(1);
						FileCursor clientCursor = new FileCursor(tidStr, index);
						FileCursor serverCursor = memCached.get(userIdStr);
						serverCursor.toString();
						if(clientCursor.compareTo(serverCursor)<0)
						{
							List<FileChange> lstResponse = FileChangeDAO.getUpperByTransactionId(tid, Integer.parseInt(index));
							XMLFactory fileChangeFactory = new XMLFactory();
							if(!lstResponse.isEmpty())
							{
								DomRepresentation dom = fileChangeFactory.getUpperByTransactionId(lstResponse);
								response.setEntity(dom);
								response.setStatus(Status.SUCCESS_OK);
							}
							else 
							{
								response.setEntity("Latest version", MediaType.TEXT_PLAIN);
								response.setStatus(Status.SUCCESS_OK);
							}
						}
						else
						{
							response.setEntity("Latest version", MediaType.TEXT_PLAIN);
							response.setStatus(Status.SUCCESS_OK);
						}
					}catch(IndexOutOfBoundsException ex)
					{

						response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					response.setEntity(new StringRepresentation(e.getMessage()));
					response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					e.printStackTrace();
				}

			}
		}
		else
		{
			String tid = (String) request.getAttributes().get("tid");
			XMLFactory factory = new XMLFactory();
			try{
				DomRepresentation dom = factory.getUpperByTransactionId(tid);
				if(dom!=null)
				{
					response.setEntity(dom);
					response.setStatus(Status.SUCCESS_CREATED);
				}	
			}	catch(HibernateException ex)
			{
				String errMessage = ex.getMessage();
				response.setEntity(new StringRepresentation(errMessage));
				response.setStatus(Status.SERVER_ERROR_VERSION_NOT_SUPPORTED);
			}
		}
	}
}

