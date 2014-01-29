package net.njay.serverinterconnect.encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encoder {

	private Cipher dCipher;
	private Cipher eCipher;
	private SecretKeySpec secretKey;
	private IvParameterSpec ivParamSpec;

	public Encoder(byte[] keyBytes, byte[] ivBytes) {
	    ivParamSpec = new IvParameterSpec(ivBytes);
	    try {
	         DESKeySpec dkey = new  DESKeySpec(keyBytes);
	         this.dCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	         this.eCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	         this.secretKey = new SecretKeySpec(dkey.getKey(), "DES");	         
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (NoSuchPaddingException e) {
	        e.printStackTrace();
	    } catch (InvalidKeyException e) {
	        e.printStackTrace();
	    }
	}
	
	public Object decrypt(byte[] encrypted) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException, ClassNotFoundException {
	    dCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParamSpec);
	    return toObject(dCipher.doFinal(encrypted));
	}
	
	public byte[] encrypt(Object obj) throws InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, ShortBufferException, BadPaddingException {
	    byte[] input = toByteArray(obj);
	    eCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParamSpec);
	    return eCipher.doFinal(input);
	}
	
	public void init() throws InvalidKeyException, InvalidAlgorithmParameterException{
		dCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParamSpec);
		eCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParamSpec);
	}

	private byte[] toByteArray(Object complexObject) throws IOException {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();;
	    ObjectOutputStream out = new ObjectOutputStream(baos);
	    out.writeObject(complexObject);
	    out.close();
	    return baos.toByteArray();
	}
	
	private Object toObject(byte[] byteObject) throws IOException,
    	ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(byteObject);
		ObjectInputStream in = new ObjectInputStream(bais);
		Object o = in.readObject();
		in.close();
		return o;
	}
	
	public Cipher getEncryptionCipher(){ return this.eCipher; }
	public Cipher getDecryptionCipher(){ return this.dCipher; }
}
