package androidlab.edu.cn.nucyixue;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

import androidlab.edu.cn.nucyixue.data.bean.LU;
import androidlab.edu.cn.nucyixue.data.bean.Live;
import androidlab.edu.cn.nucyixue.data.bean.UserInfo;

/**
 * MyApp
 *
 * Created by dreamY on 2017/7/24.
 */
public class MyApp extends Application {

    public static final Boolean isDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();

        AVObject.registerSubclass(Live.class);
        AVObject.registerSubclass(UserInfo.class);
        AVObject.registerSubclass(LU.class);

        AVOSCloud.initialize(this,"O5aEuqARNjtbvT2tGTW23bB5-gzGzoHsz","XMaxhc0a9L5cDOIAXrBeqoS8");

        AVOSCloud.setDebugLogEnabled(true);
    }
}
