package com.veclink.poofpetgps.tcp;


public final class Helper {


    public static int CRC16(byte[] paramArrayOfByte) {
        int i1 = 0;
        int i2 = 65535;
        while (true) {
            if (i1 >= paramArrayOfByte.length)
                return i2 & 0xFFFF;
            int i3 = 0xFFFF & (i2 >>> 8 | i2 << 8) ^ 0xFF & paramArrayOfByte[i1];
            int i4 = i3 ^ (i3 & 0xFF) >> 4;
            int i5 = i4 ^ 0xFFFF & i4 << 12;
            i2 = i5 ^ 0xFFFF & (i5 & 0xFF) << 5;
            ++i1;
        }
    }

    public static int CRC8(byte[] paramArrayOfByte) {
        int i1 = 0;
        int i2 = 0;
        int i3;
        while (true) {
            if (i1 >= paramArrayOfByte.length)
                return i2;
            i2 ^= 0xFF & paramArrayOfByte[i1];
            i3 = 0;
            label24:
            if (i3 < 8)
                break;
            ++i1;
        }
        if ((i2 & 0x1) != 0) ;
        for (i2 = 0x8C ^ 0xFF & i2 >> 1; ; i2 = 0xFF & i2 >> 1) {
            ++i3;
        }
    }

    /**
     * @param paramArrayOfByte
     * @return String
     * @author Allen
     * @Description: byte数组转换成hexstring
     */

    public static String bytesToHexString(byte[] paramArrayOfByte) {

        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            byte b1 = paramArrayOfByte[i];
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Byte.valueOf(b1);
            localStringBuilder.append(String.format("%02x ", arrayOfObject));
        }
        return localStringBuilder.toString();
    }

    public static String bytesToNoSpaceHexString(byte[] paramArrayOfByte) {

        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            byte b1 = paramArrayOfByte[i];
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Byte.valueOf(b1);
            localStringBuilder.append(String.format("%02x", arrayOfObject));
        }
        return localStringBuilder.toString();
    }


    /**
     * hex字符串转byte数组<br/>
     * 2个hex转为一个byte
     *
     * @param hexString
     * @return
     */
    public static byte[] hexToBytesArray(String hexString) {
        hexString = hexString.toLowerCase();
        byte[] res = new byte[hexString.length() / 2];
        char[] chs = hexString.toCharArray();
        int[] b = new int[2];

        for (int i = 0, c = 0; i < chs.length; i += 2, c++) {
            for (int j = 0; j < 2; j++) {
                if (chs[i + j] >= '0' && chs[i + j] <= '9') {
                    b[j] = (chs[i + j] - '0');
                } else if (chs[i + j] >= 'A' && chs[i + j] <= 'F') {
                    b[j] = (chs[i + j] - 'A' + 10);
                } else if (chs[i + j] >= 'a' && chs[i + j] <= 'f') {
                    b[j] = (chs[i + j] - 'a' + 10);
                }
            }

            b[0] = (b[0] & 0x0f) << 4;
            b[1] = (b[1] & 0x0f);
            res[c] = (byte) (b[0] | b[1]);
        }

        return res;
    }

    public static String bytesToDeviceId(byte[] paramArrayOfByte) {

        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            byte b1 = paramArrayOfByte[i];
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Byte.valueOf(b1);
            if (i == paramArrayOfByte.length - 1) {
                localStringBuilder.append(String.format("%02x", arrayOfObject));
            } else {
                localStringBuilder.append(String.format("%02x-", arrayOfObject));
            }

        }
        return localStringBuilder.toString();
    }

    /**
     * 十进制字符串转压缩bcd格式byte数组
     *
     * @param asc
     * @return
     */
    public static byte[] str2Bcd(String asc) {
        asc = asc.replaceAll("\\+", "a");
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0) {
            asc = asc + "F";
            len = asc.length();
        }

        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }

        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;

        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }

            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }


    public static byte[] getNeedSendNumberByteArray(String phonenumber) {
        byte[] bcdArray = Helper.str2Bcd(phonenumber);
        byte[] msgContentByteArray = new byte[8];
        for (int i = 0; i < msgContentByteArray.length; i++) {
            msgContentByteArray[i] = (byte) 0xff;
        }
        int length = bcdArray.length > msgContentByteArray.length ? msgContentByteArray.length : bcdArray.length;
        for (int i = 0; i < length; i++) {
            msgContentByteArray[i] = bcdArray[i];
        }
        return msgContentByteArray;

    }


}

