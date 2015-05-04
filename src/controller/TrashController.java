package controller;

import java.io.IOException;
import java.util.List;

import model.FileStorage;
import model.FileStorageDAO;
import model.XMLFactory;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;

public class TrashController extends Restlet {

	@Override
	public void handle(Request request, Response response) {
		// TODO Auto-generated method stub
		int userId = Integer.parseInt((String) request.getAttributes().get("userId"));
		try {
			DomRepresentation dom = null;
			List<FileStorage> lst = FileStorageDAO.getFileDelete(userId);
			dom = XMLFactory.parseFileStorageToXml(lst);
			response.setEntity(dom);
			response.setStatus(Status.SUCCESS_OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
