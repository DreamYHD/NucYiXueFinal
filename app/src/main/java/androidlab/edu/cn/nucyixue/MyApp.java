package androidlab.edu.cn.nucyixue;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

import androidlab.edu.cn.nucyixue.data.bean.UserInfo;

/**
 * Created by dreamY on 2017/7/24.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVObject.registerSubclass(UserInfo.class);
        AVOSCloud.initialize(this,"O5aEuqARNjtbvT2tGTW23bB5-gzGzoHsz","XMaxhc0a9L5cDOIAXrBeqoS8");
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(true);
    }
}
