package controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.FileChange;
import model.FileChangeDAO;
import model.FileCursor;
import model.FileStorage;
import model.FileStorageDAO;
import model.FileVersionDAO;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.FileRepresentation;
import org.w3c.dom.Document;

import utils.Constants;

public class FileResourceController extends Restlet {

	@Override
	public void handle(Request request, Response response) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String ipAddressString = request.getClientInfo().getAddress();
		FileChangeDAO fileChangeDao = new FileChangeDAO();
		//Download file
		if(request.getMethod().equals(Method.GET))
		{
		String fileIdString = (String) request.getAttributes().get("fileId");
		String userId = (String) request.getAttributes().get("userId");
		try {
			int fileId = Integer.parseInt(fileIdString);
			if (request.getMethod().equals(Method.GET)) {
			
				try {
//					FileVersion fileVersion = FileVersionDAO
//							.getById(fileId);
					FileStorage fileStorage = FileStorageDAO
							.getById(fileId);
					File file = new File(fileStorage.getFileRealPath());
					FileRepresentation f = new FileRepresentation(file,new MediaType(fileStorage.getFileType()));
					response.setEntity(f);
					response.setStatus(Status.SUCCESS_OK);
				} catch (Exception e) {
					e.printStackTrace();
					response.setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
				}
			}
		} catch (NumberFormatException ex) {
			response.setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
		}
		}
		//Xoa file
		else if(request.getMethod().equals(Method.DELETE))
		{
			String requestStr = (String) request.getAttributes().get("fileId");
			String userIdStr = (String) request.getAttributes().get("userId");
			int userId = Integer.parseInt(userIdStr);
			int fileId = Integer.parseInt(requestStr);
			String tid = request.getHeaders().getFirst("X-TID").getValue();
			FileStorage fileStorage = FileStorageDAO.getById(fileId);
			String fileName = fileStorage.getFileName();
			try{
				List<FileStorage> files = FileStorageDAO.getFileByFileName(fileName);
				List<FileChange> fileChangeLst = new ArrayList<FileChange>();
				for(FileStorage file:files)
				{
					FileChange fileChange = new FileChange();
					fileChange.setFileId(file.getFileId());
					fileChange.setType(Constants.DELETE);
					fileChange.setIsFile(file.getIsFile());
					fileChange.setTid(Integer.parseInt(tid));
					fileChange.setTimestamp(new Date());
					fileChange.setFileName(fileName);
					fileChange.setUserId(userId);
					fileChange.setIpAddress(ipAddressString);
					
					//fileChangeLst.add(fileChange);
					fileChangeDao.insertFile(fileChange,true);
					FileCursor currentCursor = new FileCursor(fileChange.getTid(),fileChange.getFileChangeId());
					TransactionController.memCached.put(userIdStr, currentCursor);
				}
				//FileChangeDAO.insertList(fileChangeLst);
				FileStorageDAO.deleteFileByFileName(fileName);
				response.setStatus(Status.SUCCESS_OK);
			}	
			catch(Exception ex)
			{
				ex.printStackTrace();
				response.setStatus(Status.SERVER_ERROR_SERVICE_UNAVAILABLE);
			}
		}
		//Update file moi
		else if(request.getMethod().equals(Method.PATCH))
		{
			DomRepresentation xmlRequest = new DomRepresentation(request.getEntity());
			try {
				Document doc = xmlRequest.getDocument();
				String fileOldName = xmlRequest.getText("/File/OldName");
				String fileNewName = xmlRequest.getText("/File/NewName");
				String fileTimestamp = xmlRequest.getText("/File/Timestamp");
				String tid = xmlRequest.getText("/File/TID");
				int action = Integer.parseInt(xmlRequest.getText("/File/Type"));
				//Luu file moi
				FileStorage file = FileStorageDAO.getById(FileVersionDAO.getByFileName(fileOldName).getFileId());
				file.setFileName(fileNewName);
				FileStorageDAO.insertFile(file);
				//Tao moi 1 transaction
				FileChange fileChange = new FileChange();
				fileChange.setType(action);
				fileChange.setTid(Integer.parseInt(tid));
				fileChange.setFileChangeId(file.getFileId());
				fileChange.setBeforeChange(fileOldName);
				fileChange.setAfterChange(fileNewName);
				Date date = format.parse(fileTimestamp);
				fileChange.setTimestamp(date);
				fileChange.setIpAddress(ipAddressString);
				//Luu vao DB
				fileChangeDao.insertFile(fileChange,true);
			} catch (IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
