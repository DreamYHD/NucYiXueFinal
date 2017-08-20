package androidlab.edu.cn.nucyixue.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.net.URISyntaxException;

import static butterknife.internal.Utils.arrayOf;

/**
 * Created by dreamY on 2017/8/19.
 */

public class FileUtils {
    public static String getFilePahtFromUri(Context context, Uri uri) {
        String data = null;
        Cursor mCursor = context.getContentResolver().query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null);
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



}
