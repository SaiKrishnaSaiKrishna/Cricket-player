package com.wipro.authentication.model;

public class Token {
	 
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
	
	public Token() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Token(String token) {
		super();
		this.token = token;
	}

	@Override
	public String toString() {
		return "Token [token=" + token + "]";
	}
	
	
}
