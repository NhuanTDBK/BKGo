package controller;

import java.io.IOException;
import java.util.Map;

import model.User;
import model.UserDAO;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class UserController extends Restlet {

	@Override
	public void handle(Request request, Response response) {
		// TODO Auto-generated method stub
		if(request.getMethod().equals(Method.POST))
		{
			
			try {
//				String authorization = request.getHeaders().getValues("Authorization");
				Representation form = request.getEntity();
				Form forms = new Form(form);
				String text = forms.getFirstValue("userName");
				
				User user = UserDAO.getUserByName(text);
				if(user==null)
				{
					response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					return;
				}
				DomRepresentation xml = new DomRepresentation();
				xml.setIndenting(true);
				Document doc = xml.getDocument();
				
				Node userNode = doc.createElement("User");
				doc.appendChild(userNode);
				
				Node userId = doc.createElement("UserId");
				userId.setTextContent(Integer.toString(user.getUserId()));
				userNode.appendChild(userId);
				
				
				Node accessToken = doc.createElement("AccessToken");
				accessToken.setTextContent("ajr414");
				userNode.appendChild(accessToken);
				
				response.setEntity(xml);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			try{
			DomRepresentation xml = new DomRepresentation();
			xml.setIndenting(true);
			Document doc = xml.getDocument();
			
			Node user = doc.createElement("User");
			doc.appendChild(user);
			
			Node userId = doc.createElement("UserId");
			userId.setTextContent(Integer.toString(12));
			user.appendChild(userId);

			
			Node accessToken = doc.createElement("AccessToken");
			accessToken.setTextContent("ajr414");
			user.appendChild(accessToken);
			
			response.setEntity(xml);
			}catch(Exception ex)
			{
				response.setEntity(new StringRepresentation(ex.getMessage()));
			}
		}
	}

}
