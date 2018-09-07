package com.artolia.utils.base64;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Utils {

	public static String byteToString(byte[] asBytes) {
		return Base64.getEncoder().encodeToString(asBytes);
	}
	
	public static byte[] stringToByte(String asBase64) {
		return Base64.getDecoder().decode(asBase64);
	}
	
	public static String urlEncode(String url) throws UnsupportedEncodingException {
		return Base64.getUrlEncoder().encodeToString(url.getBytes("UTF-8"));
	}
	
	public static String urlDecode(String url) throws UnsupportedEncodingException {
		return new String(Base64.getUrlDecoder().decode(url), "UTF-8");
	}
}
