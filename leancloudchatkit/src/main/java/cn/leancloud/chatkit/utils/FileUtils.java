package cn.leancloud.chatkit.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by dreamY on 2017/8/19.
 */
public class FileUtils {

    public static String getFilePahtFromUri(Context context, Uri uri) {
        String data = null;
        Cursor mCursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
        if (mCursor.moveToFirst()) {
            int index = mCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            if (index > -1) {
                data = mCursor.getString(index);
            }
        }
        mCursor.close();
        return data;
    }

    public static String getFileName(String filePath){

        return filePath.substring(filePath.lastIndexOf("/")+1);
    }

    public static String byte2MemorySize(Long byteNum){
        double B = Math.pow(2.0, 10.0);
        double KB = Math.pow(2.0, 20.0);
        double MB = Math.pow(2.0, 30.0);
        double GB = Math.pow(2.0, 40.0);

        if(byteNum < 0){
            return "shouldn't be less than zero";
        }else if(byteNum < B){
            return String.format("%.2fB", byteNum + 0.0005);
        }else if(byteNum < KB){
            return String.format("%.2fKB", byteNum/ B + 0.0005);
        }else if(byteNum < MB){
            return String.format("%.2fMB", byteNum/ KB + 0.0005);
        }else if(byteNum < GB){
            return String.format(".2fGB", byteNum/ GB + 0.0005);
        }else{
            return "it's too large";
        }
    }

    public static String byte2MemorySize(int byteNum){
        double B = Math.pow(2.0, 10.0);
        double KB = Math.pow(2.0, 20.0);
        double MB = Math.pow(2.0, 30.0);
        double GB = Math.pow(2.0, 40.0);

        if(byteNum < 0){
            return "shouldn't be less than zero";
        }else if(byteNum < B){
            return String.format("%.2fB", byteNum + 0.0005);
        }else if(byteNum < KB){
            return String.format("%.2fKB", byteNum/ B + 0.0005);
        }else if(byteNum < MB){
            return String.format("%.2fMB", byteNum/ KB + 0.0005);
        }else if(byteNum < GB){
            return String.format(".2fGB", byteNum/ GB + 0.0005);
        }else{
            return "it's too large";
        }
    }



}
