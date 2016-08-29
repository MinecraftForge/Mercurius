package net.minecraftforge.mercurius.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by tamas on 7/15/16.
 */
public class DataHelper {

    public static String CreateID() {
        String id = "";
        UUID random = UUID.randomUUID();
        try {
            id = DataHelper.Anonymize(random.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static String Anonymize(String data) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        byte[] dataBytes = data.getBytes();
        for (int i = 0; i < StatsConstants.HASHCOUNT; i++) {
            dataBytes = sha256.digest(dataBytes);
        }

        return DataHelper.bytesToHex(dataBytes);
    }

    final static protected char[] hexArray = "0123456789abcdef".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
