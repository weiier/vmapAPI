package com.buptmap.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.misc.BASE64Encoder;

public class Base64 {
	
	private static final String BASE64HASH = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	
	public static void main(String[] args) {
		String picPath = "D:\\palmap_files\\mappictures\\23f9e68b-23d4-47aa-bac2-c5e764006e30\\Floor0\\svg_0.svg";
		Base64 base = new Base64();
		
		System.out.println(base.encode(picPath));
		//System.out.println(base.encode(picPath, ""));
		//String s = "Lucy";
		//byte[] data = s.getBytes();
		//System.out.println(base.encode(data));
		//base.byteTo8BinaryBytes((byte)'L');
		//System.out.println(base.getAscii((byte)'L'));
	}
	
	public String encode(String picPath, String tt) throws IOException {
		InputStream is = null;
		byte[] data = null;
		//try{
			is = new FileInputStream(picPath);
			data = new byte[is.available()];
			is.read(data);
			is.close();
		//} catch (Exception e) {
		//	e.printStackTrace();
		//}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}
	
	public String encode(byte[] data) {
		String result = "";
		int ascii = 0;
		int prev = 0;
		int mod = -1;
		for (int i = 0 ; i < data.length; i++) {
			ascii = getAscii(data[i]);
			//System.out.println(data[i]+": "+ascii);
			mod = i % 3;
			switch (mod) {
			case 0 :
				result += BASE64HASH.charAt(ascii >> 2); break;
			case 1 :
				result += BASE64HASH.charAt(((prev & 3) << 4) | (ascii >> 4)); break;
			case 2 :
				result += BASE64HASH.charAt(((prev & 0x0f) << 2) | (ascii >> 6)); 
				result += BASE64HASH.charAt(ascii & 0x3f);
				break;
			}
			prev = ascii;
		}
		if (mod == 0) {
			result += BASE64HASH.charAt((prev & 3) << 4) + "==";
		}
		else if (mod == 1) {
			result += BASE64HASH.charAt((prev & 0x0f) << 2) + "=";
		} 
		return result;
	}
	
	public String encode(String filePath) {
		InputStream is = null;
		byte[] data = null;
		try {
			is = new FileInputStream(filePath);
			data = new byte[is.available()];
			is.read(data);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = "";
		int ascii = 0;
		int prev = 0;
		int mod = -1;
		for (int i = 0 ; i < data.length; i++) {
			ascii = getAscii(data[i]);
			//System.out.println(data[i]+": "+ascii);
			mod = i % 3;
			switch (mod) {
			case 0 :
				result += BASE64HASH.charAt(ascii >> 2); break;
			case 1 :
				result += BASE64HASH.charAt(((prev & 3) << 4) | (ascii >> 4)); break;
			case 2 :
				result += BASE64HASH.charAt(((prev & 0x0f) << 2) | (ascii >> 6)); 
				result += BASE64HASH.charAt(ascii & 0x3f);
				break;
			}
			prev = ascii;
		}
		if (mod == 0) {
			result += BASE64HASH.charAt((prev & 3) << 4) + "==";
		}
		else if (mod == 1) {
			result += BASE64HASH.charAt((prev & 0x0f) << 2) + "=";
		} 
		return result;
	}
	
	public byte[] byteTo8BinaryBytes(byte b) {
		int length = 8;
		byte[] res = new byte[length];
		int shift = 1;
		int mask = 1;
		
		try {
			do {
				res[--length] = (byte) (b & mask);
				b >>>= shift;
			} while (length != 0);
			//for (int i = 0; i < 8; i ++) {
			//	System.out.println(res[i]);
			//}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getAscii(byte b) {
		byte[] data = byteTo8BinaryBytes(b);
		int temp = 1;
		int res = 0;
		for (int i = 0; i < data.length; i++) {
			temp <<= (data.length - 1 - i);
			//System.out.println(i+": "+temp);
			res += (temp*(int)data[i]);
			temp = 1;
		}
		return res;
	}
}
