package com.buyamovie.usersession;

public class UserSession {
	private String sessionID;
	private String userEmail;
	private String userName;
	
	public UserSession(String sessionID, String userEmail, String userName) {
		this.sessionID = sessionID;
		this.setUserEmail(userEmail);
		this.userName = userName;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
