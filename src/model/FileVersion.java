package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="FILE_VERSION")
public class FileVersion implements Serializable {
	
	private static final long serialVersionUID = 8765016103450361311L;
	@Id
	@Column(name="FILE_ID")
	private int fileId;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="FILE_REAL_PATH")
	private String fileRealPath;

	@Column(name="LATEST")
	private int lastVersion;
	
	@Column(name="IS_FILE")
	private int isFile;
	
	public int getIsFile() {
		return isFile;
	}

	public void setIsFile(int isFile) {
		this.isFile = isFile;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(int lastVersion) {
		this.lastVersion = lastVersion;
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
	
}
