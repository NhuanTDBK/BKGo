package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="FILE_CHANGE")
public class FileChange implements Serializable{
	@Id @GeneratedValue
	@Column(name="FILE_CHANGE_ID")
	private int fileChangeId;

	@Column(name="FILE_ID")
	private int fileId;
	//Transaction ID
	@Column(name="TID")
	private int tid;
	@Column(name="TYPE")
	private int type ;
	@Column(name="BEFORE_CHANGE")
	private String beforeChange;
	@Column(name="AFTER_CHANGE")
	private String afterChange;
	@Column(name="TIMESTAMP")
	private Date timestamp;
	@Column(name="IS_FILE",nullable=true)
	private int isFile;
	@Column(name="FILE_NAME")
	private String fileName;
	@Column(name="USER_ID")
	private int userId;
	@Transient
	private String ipAddress;
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
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
	public int getFileChangeId() {
		return fileChangeId;
	}
	public void setFileChangeId(int fileChangeId) {
		this.fileChangeId = fileChangeId;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getBeforeChange() {
		return beforeChange;
	}
	public void setBeforeChange(String beforeChange) {
		this.beforeChange = beforeChange;
	}
	public String getAfterChange() {
		return afterChange;
	}
	public void setAfterChange(String afterChange) {
		this.afterChange = afterChange;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
