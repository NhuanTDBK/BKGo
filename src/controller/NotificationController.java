package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import model.FileChange;
import model.FileChangeDAO;
import model.FileCursor;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;

public class NotificationController extends Restlet implements Observer{

//	public static int count = 0;
//	public static Map<Integer, FileCursor> latestCursor = new HashMap<Integer, FileCursor>();
	@Override
	public void handle(Request request, Response response) {
//		DomRepresentation dom;
//		try {
//			dom = new DomRepresentation(request.getEntity());
//			int tid = Integer.parseInt(dom.getText("/Transaction/TID"));
//			int index = Integer.parseInt(dom.getText("/Transaction/Index"));
//			
//			List lst = FileChangeDAO.getCursor();
//			int maxTid = Integer.parseInt(lst.get(0).toString());
//			int maxIndex = Integer.parseInt(lst.get(1).toString());
//			DomRepresentation res = null;
//			FileChangeFactory fileChangeController = new FileChangeFactory();
//			if(maxTid>tid)
//				res = fileChangeController.getUpperByTransactionId(Integer.toString(tid));
//			else if((maxTid==tid)&&(maxIndex>index))
//			{
//				List<FileChange>diff = FileChangeDAO.getUpperByTransactionId(tid, index);
//				res = fileChangeController.getUpperByTransactionId(diff);
//			}
//			response.setEntity(res);
//			response.setStatus(Status.SUCCESS_OK);
//		}
//		catch (NumberFormatException | IndexOutOfBoundsException e) {
//			response.setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);// TODO Auto-generated catch block
//			System.out.println("Loi roi: "+e.getMessage());
//			}

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		FileChange fileChange = (FileChange)arg;
		//Push into queues
	}
}
