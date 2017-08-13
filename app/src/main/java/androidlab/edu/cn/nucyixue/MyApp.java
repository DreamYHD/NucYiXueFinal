package androidlab.edu.cn.nucyixue;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by dreamY on 2017/7/24.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this,"KElRaSz2NTiDTiMPpaHKKI4y-gzGzoHsz","EcQnyT6mczTqjcBtSXIKys3m");
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(true);
    }
}
