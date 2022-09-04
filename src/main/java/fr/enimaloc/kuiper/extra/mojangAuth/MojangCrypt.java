/*
 * MojangCrypt
 *
 * 0.0.1
 *
 * 04/09/2022
 */
package fr.enimaloc.kuiper.extra.mojangAuth;

import java.nio.charset.StandardCharsets;
import java.security.*;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static fr.enimaloc.kuiper.MinecraftServer.Markers.AUTH;

/**
 *
 */
public class MojangCrypt {
    private static final Logger LOGGER = LoggerFactory.getLogger(MojangCrypt.class);

    public static @Nullable KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(AUTH, "Key pair generation failed!", e);
            return null;
        }
    }

    public static byte @Nullable [] digestData(String data, PublicKey publicKey, SecretKey secretKey) {
        return digestData("SHA-1", data.getBytes(StandardCharsets.ISO_8859_1), secretKey.getEncoded(), publicKey.getEncoded());
    }

    private static byte @Nullable [] digestData(String algorithm, byte[]... data) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            for (byte[] bytes : data) {
                digest.update(bytes);
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(AUTH, "Algorithm not found", e);
            return null;
        }
    }

    public static SecretKey decryptByteToSecretKey(PrivateKey privateKey, byte[] bytes) {
        return new SecretKeySpec(decryptUsingKey(privateKey, bytes), "AES");
    }

    public static byte[] decryptUsingKey(Key key, byte[] bytes) {
        return cipherData(2, key, bytes);
    }

    private static byte[] cipherData(int mode, Key key, byte[] data) {
        try {
            return setupCipher(mode, key.getAlgorithm(), key).doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            LOGGER.error(AUTH, "Cipher data failed!", e);
        }
        return new byte[0];
    }

    private static Cipher setupCipher(int mode, String transformation, Key key) {
        try {
            Cipher cipher4 = Cipher.getInstance(transformation);
            cipher4.init(mode, key);
            return cipher4;
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            LOGGER.error(AUTH, "Cipher creation failed!", e);
        }
        return null;
    }

    public static Cipher getCipher(int mode, Key key) {
        try {
            Cipher cipher3 = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher3.init(mode, key, new IvParameterSpec(key.getEncoded()));
            return cipher3;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
