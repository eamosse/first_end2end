package first.endtoend.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 *  
 * @author Giulio
 */
public class Crypto {
 
    private Key key;
    private String encryptKey;
    /**
     * Generates the encryption key. using "des" algorithm
     * 
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException 
     * @throws InvalidKeySpecException 
     * @throws UnsupportedEncodingException 
     */
    private void generateKey(String encryptKey) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, UnsupportedEncodingException {
    	byte[] arrayBytes = encryptKey.getBytes("UTF-8");
	    KeySpec ks = new DESKeySpec(arrayBytes);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		key = skf.generateSecret(ks);
    }
 
    @SuppressWarnings("unused")
	private String encrypt(String message) throws IllegalBlockSizeException,
	    BadPaddingException, NoSuchAlgorithmException,
	    NoSuchPaddingException, InvalidKeyException,
	    UnsupportedEncodingException {
	// Get a cipher object.
	Cipher cipher = Cipher.getInstance("DES");
	cipher.init(Cipher.ENCRYPT_MODE, key);
 
	// Gets the raw bytes to encrypt, UTF8 is needed for
	// having a standard character set
	byte[] stringBytes = message.getBytes("UTF-8");
 
	// encrypt using the cypher
	byte[] raw = cipher.doFinal(stringBytes);
 
	// converts to base64 for easier display.

	String base64 = Base64.encodeToString(raw,1);
 
	return base64;
    }
 
      public String decrypt(String encrypted) throws InvalidKeyException,
	    NoSuchAlgorithmException, NoSuchPaddingException,
	    IllegalBlockSizeException, BadPaddingException, IOException {
 
		// Get a cipher object.
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key);
 
		//decode the BASE64 coded message
		byte[] raw = Base64.decode(encrypted,1);
		
 
		//decode the message
		byte[] stringBytes = cipher.doFinal(raw);
 
		//converts the decoded message to a String
		String clear = new String(stringBytes, "UTF8");
		return clear;
    }
      
      public Crypto(String key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
    	  this.encryptKey = key;
    	  generateKey(encryptKey);
    } 
}