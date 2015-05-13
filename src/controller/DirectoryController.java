package controller;

import java.io.IOException;
import java.util.Date;

import model.FileChange;
import model.FileChangeDAO;
import model.FileCursor;
import model.FileStorage;
import model.FileStorageDAO;
import model.Version;
import model.VersionDAO;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.representation.StringRepresentation;

import frame.ServerFrame;
import utils.Constants;

public class DirectoryController extends Restlet {

	@Override
	public void handle(Request request, Response response) {
		// TODO Auto-generated method stub
				Method method = request.getMethod();
		String ipAddressString = request.getClientInfo().getAddress();
		ServerFrame.logArea.append("Create directory from "+ipAddressString);

		String userIdStr = (String) request.getAttributes().get("userId");
		String tid = request.getHeaders().getFirst("X-TID").getValue();
		System.out.println(tid);
		int userId = Integer.parseInt(userIdStr);
		FileChangeDAO fileChangeDao = new FileChangeDAO();

		try {
			String directoryName = request.getEntity().getText();
			FileStorage directory = new FileStorage();
			directory.setCreateDate(new Date());
			directory.setFileName(directoryName);
			directory.setFileSize(0);
			directory.setIsFile(Constants.IS_FOLDER);
			directory.setIsActive(Constants.IS_ACTIVE);
			directory.setUserId(1);
			FileStorageDAO.insertFile(directory);

			Version version = new Version();
			version.setFileName(directoryName);
			version.setFileVersion(1);
			version.setModifyDate(directory.getCreateDate());
			version.setUserId(1);
			version.setFileId(directory.getFileId());
			VersionDAO.addVersion(version);

			FileChange fileChange = new FileChange();
			fileChange.setFileId(directory.getFileId());
			fileChange.setType(Constants.CREATE);
			fileChange.setTimestamp(new Date());
			fileChange.setIsFile(Constants.IS_FOLDER);
			fileChange.setTid(Integer.parseInt(tid));
			fileChange.setFileName(directoryName);
			fileChange.setUserId(1);
			fileChange.setIpAddress(ipAddressString);
			fileChangeDao.insertFile(fileChange,true);

			FileCursor currentCursor = new FileCursor(fileChange.getTid(),fileChange.getFileChangeId());
			TransactionController.memCached.put(userIdStr, currentCursor);

			response.setEntity(new StringRepresentation(Integer.toString(directory.getFileId())));
			response.setStatus(org.restlet.data.Status.SUCCESS_OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			response.setStatus(org.restlet.data.Status.CLIENT_ERROR_PAYMENT_REQUIRED);
			e.printStackTrace();
		}
		
	}

}
