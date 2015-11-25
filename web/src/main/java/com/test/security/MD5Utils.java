/**
 * 
 */
package com.test.security;

import java.security.MessageDigest;

/**
 * @author Nguyen Hai Vien
 *
 */
public class MD5Utils {
	public static String md5(String plainText) {
		try{
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			byte[] input = plainText.getBytes("UTF-8");
			messageDigest.update(input);
			byte[] md5hash = messageDigest.digest();
			return convertToHex(md5hash);
		}catch (Exception e) {
			return "";
		}
	}
	
	 private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 

}
