package de.abiegel.jaxrs.auth;

public class AuthParams {

	public static AuthParams withCredentials(String user, String passwort) {
		return new AuthParams(user, passwort);
	}
	private String user;
	private String passwort;
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPasswort() {
		return passwort;
	}
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
	private AuthParams(String user, String passwort) {
		super();
		this.user = user;
		this.passwort = passwort;
	}
	public AuthParams() {
		// TODO Auto-generated constructor stub
	}
	
	
}
