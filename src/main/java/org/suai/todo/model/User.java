package org.suai.todo.model;

import java.sql.SQLException;
import java.sql.ResultSet;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.servlet.ServletException;

public class User {
	public Integer id;
	public String name;
	public String salt;
	public String hash;
	public Integer itemsPerPage;
	
	public User(String name, String password) throws ServletException {
		this.id = 0;
		this.name = name.trim();
		this.itemsPerPage = 10;
		updatePassword(password);
	}
	
	public User(ResultSet rs) throws SQLException {
		id           = rs.getInt("userid");
		name         = rs.getString("name");
		salt         = rs.getString("salt");
		hash         = rs.getString("hash");
		itemsPerPage = rs.getInt("itemsperpage");
	}

	public String getName(){
		return name;
	}
	private static byte[] hex2byte(String hexStr) {
		byte[] bytes = new byte[hexStr.length() / 2];

		for (int i = 0; i < bytes.length; i++) {
			int index = i * 2;
			int val = Integer.parseInt(hexStr.substring(index, index + 2), 16);
			bytes[i] = (byte)val;
		}
		return bytes;
	}
	
	private static String byte2hex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();		
	}
	
	private String makeHash(String password) throws ServletException {
		try {
			// may be exception java.security.NoSuchAlgorithmException
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(salt.getBytes());
			md.update(password.getBytes());
			
			return byte2hex(md.digest());
		} catch (Exception err) {
			throw new ServletException(err);
		}
	}
	
	public void updatePassword(String password) throws ServletException {
		SecureRandom rand = new SecureRandom();
		byte[] bytes = new byte[16];
		rand.nextBytes(bytes);
		salt = byte2hex(bytes);
		hash = makeHash(password);
	}
	
	public boolean isValidPassword(String password) throws ServletException {
		if (password == null || hash == null || salt == null || password == null) {
			return false;
		}
		return makeHash(password).equals(hash);
	}
}
