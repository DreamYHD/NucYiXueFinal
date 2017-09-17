package androidlab.edu.cn.nucyixue.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RtmpUtils {

    private static final String KEY = "";

    private StringBuilder mStringBuilder = new StringBuilder();

    public static void main(String[] args) {
        System.out.println(getSafeUrl("1111", "2017-09-16 23:59:59"));
    }

    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     *
     * @param streamId live id，use AVUserId can make only
     * @param txTime  the live useful time util
     * @return
     */
    public static String getSafeUrl(String streamId, String txTime) {

        String stream = "10305_"+streamId;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(txTime);
        } catch (ParseException mE) {
            mE.printStackTrace();
        }
        String input = new StringBuilder().
                append(KEY).
                append(stream).
                append(Long.toHexString(date.getTime()/1000).toUpperCase()).toString();
        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret  = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return txSecret == null ? "" :
                new StringBuilder().
                        append("rtmp://10305.livepush.myqcloud.com/live/10305_").
                        append(streamId).
                        append("?bizid=10305&").
                        append("txSecret=").
                        append(txSecret).
                        append("&").
                        append("txTime=").
                        append(Long.toHexString(date.getTime()/1000).toUpperCase()).
                        toString();
    }

    public static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }
}