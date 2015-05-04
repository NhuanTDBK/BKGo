package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import model.FileStorage;
import model.FileStorageDAO;
import model.FileVersion;
import model.FileVersionDAO;
import model.UserDAO;
import model.XMLFactory;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.StringRepresentation;

import utils.Constants;
import utils.ServerUtil;

public class UploadController extends Restlet {

	@Override
	public void handle(Request request, Response response) {
		// TODO Auto-generated method stub
		// System.out.println(request.getEntityAsText());
		String type = (String) request.getAttributes().get("type");
		String userIdStr = (String) request.getAttributes().get("userId");
		String ipAddressString = request.getClientInfo().getAddress();
		System.out.println(ipAddressString);
		int userId = 0;
		try
		{
			userId = Integer.parseInt(userIdStr);
			boolean checkUserId = UserDAO.checkUserId(userId) ;
//					TransactionController.memCached.getUserTransaction().containsKey(userIdStr);
			//UserDAO.checkUserId(userId) ;
//			if(checkUserId==false)
//			{
//				response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
//				response.setEntity(new StringRepresentation("User id sai"));
//				return;
//			}
		}
		catch(NumberFormatException ex)
		{
			ex.printStackTrace();
		}
		System.out.println(type);

		Method method = request.getMethod();
		if (method.compareTo(Method.POST) == 0) {
			if (type.equals("file")) {
				FileUploadController files = new FileUploadController(userId);
				files.handle(request, response);
			} else if (type.equals("directory")) {
				DirectoryUploadController directory = new DirectoryUploadController();
				directory.handle(request, response);
			}
		} else if (method.compareTo(Method.GET) == 0) {
			if (type.equals("all")) {
				// tra ve file xml
				List<FileVersion> lstFile = FileVersionDAO.getAll();
				try {
					response.setEntity(XMLFactory
							.parseFileVersionToXml(lstFile));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				response.setStatus(Status.SUCCESS_OK);
			}
		}
	}

}
