package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USERS")
public class User {
	@Id @GeneratedValue
	@Column(name="USER_ID")
	private int userId;
	
	
	@Column(name="USER_FOLDER_PATH")
	private String userFolderPath;
	
	@Column(name="USER_STORAGE_SIZE")
	private long userStorageSize;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name="PASSWORD")
	private String password;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserFolderPath() {
		return userFolderPath;
	}

	public void setUserFolderPath(String userFolderPath) {
		this.userFolderPath = userFolderPath;
	}

	public long getUserStorageSize() {
		return userStorageSize;
	}

	public void setUserStorageSize(long userStorageSize) {
		this.userStorageSize = userStorageSize;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
