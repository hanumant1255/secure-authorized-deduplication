package com.hybrid.cloud.util;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEncrypterDecrypter {

	private static final String PBKDF2_WITH_HMAC_SHA1 = "PBKDF2WithHmacSHA1";
	private static final String AES = "AES";
	private static final String UTF8 = "UTF8";
	private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
	private static final byte[] SALT = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35,
			(byte) 0xE3, (byte) 0x03 };
	private static final int ITERATION_COUNT = 65536;
	private static final int KEY_LENGTH = 256;
	final IvParameterSpec iv = new IvParameterSpec(getUTF8Bytes("1234567890123456"));

	private Cipher ecipher;
	private Cipher dcipher;
	private static byte[] getUTF8Bytes(String input) {
		   return input.getBytes(StandardCharsets.UTF_8);
	}
	public AESEncrypterDecrypter(String passPhrase) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_WITH_HMAC_SHA1);
		KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), AES);

		ecipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
		ecipher.init(Cipher.ENCRYPT_MODE, secret,iv);

		dcipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
		dcipher.init(Cipher.DECRYPT_MODE, secret, iv);
	}

	public String encrypt(String encrypt) throws Exception {
		byte[] bytes = encrypt.getBytes(UTF8);
		byte[] encrypted = encrypt(bytes);
		return Base64.getEncoder().encodeToString(encrypted);
	}

	public byte[] encrypt(byte[] plain) throws Exception {
		return ecipher.doFinal(plain);
	}

	public String decrypt(String encrypt) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(encrypt.getBytes(UTF8));
		byte[] decrypted = decrypt(bytes);
		return new String(decrypted, UTF8);
	}

	public byte[] decrypt(byte[] encrypt) throws Exception {
		return dcipher.doFinal(encrypt);
	}

//    public static void main(String[] args) throws Exception {
// 
//        String message = "MESSAGE";
//        String password = "PASSWORD";
// 
//        AESEncrypter encrypter = new AESEncrypter(password);
//        String encrypted = encrypter.encrypt(message);
//        String decrypted = encrypter.decrypt(encrypted);
// 
//        System.out.println("Encrypt(\"" + message + "\", \"" + password + "\") = \"" + encrypted + "\"");
//        System.out.println("Decrypt(\"" + encrypted + "\", \"" + password + "\") = \"" + decrypted + "\"");
//    }
}