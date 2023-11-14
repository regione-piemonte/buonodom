/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.business.be.service.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.csi.buonodom.buonodombo.util.LoggerUtil;

@Component
public class FilesEncrypt extends LoggerUtil {

	@Value("${encryptionkey}")
	private String keyEncrypt;

	public void creaFileCifrato(int cipherMode, InputStream inputFile, File outputFile) {
		final String methodName = "execCryptDecrypt";
		try {
			Key secretKey = new SecretKeySpec(keyEncrypt.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(cipherMode, secretKey);
			byte[] inputBytes;
			inputBytes = inputFile.readAllBytes();
			inputFile.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);
			try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
				outputStream.write(outputBytes);
			}
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException e) {
			logError(methodName, "Errore in cifratura: ", e);
		}
	}

	public void creaFileCifratoByte(int cipherMode, byte[] inputFile, File outputFile) {
		final String methodName = "execCryptDecrypt";
		try {
			Key secretKey = new SecretKeySpec(keyEncrypt.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(cipherMode, secretKey);

			byte[] outputBytes = cipher.doFinal(inputFile);
			// uotputfile deve contenere il parametro
			try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
				outputStream.write(outputBytes);
			}
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException e) {
			logError(methodName, "Errore in cifratura: ", e);
		}
	}

	public byte[] creaFileCifratoByte(int cipherMode, byte[] inputFile) {
		final String methodName = "execCryptDecrypt";
		byte[] outputBytes = null;
		try {
			Key secretKey = new SecretKeySpec(keyEncrypt.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(cipherMode, secretKey);

			outputBytes = cipher.doFinal(inputFile);
			logInfo("file cifrato ", methodName);
			return outputBytes;
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException e) {
			logError(methodName, "Errore in cifratura: ", e);
			return null;
		}
	}

	public byte[] creaFileDeCifratoByte(int cipherMode, byte[] inputFile) {
		final String methodName = "execDeCryptDecrypt";

		Key secretKey = new SecretKeySpec(keyEncrypt.getBytes(), "AES");
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");

			cipher.init(cipherMode, secretKey);

			byte[] outputBytes = cipher.doFinal(inputFile);

			if (outputBytes != null) {
				// codifico in Base64 se non lo e'
//				if (! Util.isBase64Encoded(outputBytes)) {
//					outputBytes = Base64.getEncoder().encode(outputBytes);
//				}
				return outputBytes;
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException e) {
			logError(methodName, "Errore in decifratura: ", e);
		}
		return null;
	}

}
