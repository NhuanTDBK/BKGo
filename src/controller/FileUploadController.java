package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.FileChange;
import model.FileChangeDAO;
import model.FileCursor;
import model.FileStorage;
import model.FileStorageDAO;
import model.FileVersion;
import model.FileVersionDAO;
import model.Version;
import model.VersionDAO;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import utils.ClientUtils;
import utils.Constants;
import utils.ServerUtil;

public class FileUploadController extends Restlet {

	public int userId;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public FileUploadController(int userId) {
		// TODO Auto-generated constructor stub
		this.userId = userId;
	}
	@Override
	public void handle(Request request, Response response) {
		// TODO Auto-generated method stub
		Method method = request.getMethod();

		String ipAddressString = request.getClientInfo().getAddress();
		String tid = request.getHeaders().getFirst("X-TID").getValue();
		System.out.println(tid);
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		FileChangeDAO fileChangeDao = new FileChangeDAO();
		if(method.compareTo(Method.POST)==0)
		{
			Representation entity = request.getEntity();
			if(entity!=null)
			{
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1000240);
				// 2/ Create a new file upload handler based on the Restlet
				// FileUpload extension that will parse Restlet requests and
				// generates FileItems.
				RestletFileUpload upload = new RestletFileUpload(factory);

				// 3/ Request is parsed by the handler which generates a
				// list of FileItems
				try {
					List<FileItem> fileItems = upload.parseRepresentation(entity);
					for(FileItem fi: fileItems)
					{
						
						String fileName = fi.getName();
						Date createDate = new Date();
						float fileSize = (float)fi.getSize();

						FileChange fileChange = new FileChange();
						fileChange.setType(Constants.CREATE);
						//Kiểm tra v
						FileVersion file = FileVersionDAO.getByFileName(fileName);
						int version;
						//Nếu file chưa tồn tại, version = 1. Ngược lại version sẽ bằng version cũ + 1
						if(file!=null)
						{
							version = file.getLastVersion()+1;
							fileChange.setType(Constants.UPDATE);
						}
						else version = 1;
						//Mã hóa tên
						String fileRealPath = ServerUtil.encryptMessage(fileName+"_version_"+version);
						System.out.println(fileName);
						//Ghi file
						String path = "storage/"+userId+"/"+fileName;
						Path filePath = Paths.get(path);
						File f = filePath.toFile();
						if(fi.getSize()<2*FileUtils.ONE_GB)
							IOUtils.copy(fi.getInputStream(), new FileOutputStream(f));
						else
							IOUtils.copyLarge(fi.getInputStream(), new FileOutputStream(f));
						//Lưu file hash
						String fileHash = ServerUtil.encryptFile(new FileInputStream(f));
						String fileType = Files.probeContentType(f.toPath());
						//Đánh dấu thuộc tính sync vào file
						ClientUtils.addFileAttribute(f.toPath(),Constants.SYNC_KEY, Constants.SYNC_VALUE);

						FileStorage fs = new FileStorage();
						fs.setFileName(fileName);
						fs.setFileHash(fileHash);
						fs.setCreateDate(createDate);
						fs.setFileRealPath(f.getAbsolutePath());
						fs.setFileSize(fileSize);
						fs.setFileType(fileType);
						fs.setIsFile(Constants.IS_FILE);
						fs.setIsActive(Constants.IS_ACTIVE);
						fs.setUserId(this.userId);
						FileStorageDAO.insertFile(fs);

						Version fileVersion = new Version();
						fileVersion.setFileId(fs.getFileId());
						fileVersion.setFileName(fs.getFileName());
						fileVersion.setFileVersion(version);
						fileVersion.setModifyDate(createDate);
						fileVersion.setUserId(1);
						VersionDAO.addVersion(fileVersion);

						fileChange.setFileId(fs.getFileId());
						fileChange.setTimestamp(createDate);
						fileChange.setTid(Integer.parseInt(tid));
						fileChange.setIsFile(Constants.IS_FILE);
						fileChange.setFileName(fileName);
						fileChange.setUserId(this.userId);
						fileChange.setIpAddress(ipAddressString);
						fileChangeDao.insertFile(fileChange,true);

						FileCursor currentCursor = new FileCursor(fileChange.getTid(),fileChange.getFileChangeId());
						TransactionController.memCached.put(Integer.toString(userId), currentCursor);
						String result = fs.getFileId()+"@"+fileChange.getFileChangeId();
						response.setEntity(new StringRepresentation(result));	
						//Hien thi log
					}
				} catch (FileUploadException | NoSuchAlgorithmException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
}