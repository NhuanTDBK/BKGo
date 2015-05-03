package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="VERSIONS")
//@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class Version {
	@Id @GeneratedValue
	@Column(name="VERSION_ID")
	private int versionId;
	
	@Column(name="MODIFIED_DATE")
	private java.util.Date modifyDate;
	
	@Column(name="VERSION")
	private int fileVersion;
	
	@Column(name="FILE_ID")
	private int fileId;
	
	@Column(name="USER_ID")
	private int userId;

	@Column(name="FILE_NAME")
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public java.util.Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(java.util.Date createDate) {
		this.modifyDate = createDate;
	}

	public int getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(int fileVersion) {
		this.fileVersion = fileVersion;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
}
