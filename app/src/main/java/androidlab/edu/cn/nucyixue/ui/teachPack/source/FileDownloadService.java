package androidlab.edu.cn.nucyixue.ui.teachPack.source;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import androidlab.edu.cn.nucyixue.R;

/**
 * Created by dreamY on 2017/8/21.
 */

public class FileDownloadService extends Service {
    private static final String TAG = "FileDownloadService";
    Notification.Builder  mBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
       //获取NotificationManager实例
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle("最简单的Notification")
                //设置通知内容
                .setContentText("只有小图标、标题、内容");
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(1, builder.build());


    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent mIntent) {
        return null;
    }

    class MyBinder extends Binder {

        public void startDownload( ) {
            Log.d("TAG", "startDownload() executed");
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
        }

    }

}
