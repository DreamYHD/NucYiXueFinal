package androidlab.edu.cn.nucyixue.ui.imPack;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;
import androidlab.edu.cn.nucyixue.data.bean.UserInfo;
import androidlab.edu.cn.nucyixue.utils.config.LCConfig;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;

/**
 * Live 用户信息
 *
 * Created by MurphySL on 2017/8/20.
 */

public class CustomUserProvider implements LCChatProfileProvider {
    private static final String TAG = "CustomUserProvider";

    private static CustomUserProvider customUserProvider;

    public synchronized static CustomUserProvider getInstance() {
        if (null == customUserProvider) {
            customUserProvider = new CustomUserProvider();
        }
        return customUserProvider;
    }

    private CustomUserProvider() {
    }

    @Override
    public void fetchProfiles(final List<String> userIdList, final LCChatProfilesCallBack profilesCallBack) {
        AVQuery<UserInfo> query = new AVQuery<>(LCConfig.getUI_TABLE());
        query.findInBackground(new FindCallback<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, AVException e) {
                List<LCChatKitUser> users = new ArrayList<>();
                if(e != null){
                    Log.i(TAG, "fetchProfiles: "+e);
                }else{
                    if(list != null && !list.isEmpty()){
                        for(UserInfo userinfo : list){
                            for(String userId : userIdList){
                                if(userId.equals(userinfo.getUserId())){
                                    Log.i(TAG, "userId :" + userinfo.getUserId() +
                                            " username :"+ userinfo.getUsername()+
                                            " avatar :" + userinfo.getAvatar());
                                    LCChatKitUser user = new LCChatKitUser(
                                            userinfo.getUserId(),
                                            userinfo.getUsername(),
                                            userinfo.getAvatar());
                                    users.add(user);
                                }
                            }
                        }
                    }
                }
                profilesCallBack.done(users, e);
            }
        });
    }

}
