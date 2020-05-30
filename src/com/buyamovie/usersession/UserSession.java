package com.buyamovie.usersession;

public class UserSession {
	private String sessionID;
	private String userName;
	
	public UserSession(String sessionID, String userName) {
		this.sessionID = sessionID;
		this.userName = userName;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
