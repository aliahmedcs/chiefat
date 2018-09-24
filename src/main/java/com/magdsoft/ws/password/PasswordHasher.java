package com.magdsoft.ws.password;

//import org.mindrot.jbcrypt.BCrypt;

public interface PasswordHasher {
	 public String hashPassword(String password);
	 public boolean isPasswordValid(String password, String hash);

	 
}
