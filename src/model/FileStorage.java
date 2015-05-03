package model;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
@Entity
@Table(name="FILES")
public class FileStorage {
	@Id @GeneratedValue
	@Column(name="FILE_ID")
	private int fileId;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="FILE_REAL_PATH")
	private String fileRealPath;
	
	@Column(name="FILE_HASH")
	private String fileHash;
	
	@Column(name="FILE_TYPE")
	private String fileType;
	
	@Column(name="USER_ID")
	private int userId;

	@Column(name="CREATED_DATE")
	private Date createDate;
	
	@Column(name="IS_FILE")
	private int isFile;
	
	@Column(name="FILE_SIZE")
	private float fileSize;
	
	@Column(name="IS_ACTIVE")
	private int isActive;
	
	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileRealPath() {
		return fileRealPath;
	}

	public void setFileRealPath(String fileRealPath) {
		this.fileRealPath = fileRealPath;
	}

	public String getFileHash() {
		return fileHash;
	}

	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getIsFile() {
		return isFile;
	}

	public void setIsFile(int isFile) {
		this.isFile = isFile;
	}

	public float getFileSize() {
		return fileSize;
	}

	public void setFileSize(float fileSize) {
		this.fileSize = fileSize;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileType()
	{
		return fileType;
	}
	
}
