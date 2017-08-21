package androidlab.edu.cn.nucyixue.ui.mePack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.jakewharton.rxbinding2.view.RxView;
import java.util.concurrent.TimeUnit;
import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseActivity;
import androidlab.edu.cn.nucyixue.ui.MainActivity;
import androidlab.edu.cn.nucyixue.ui.SplashActivity;
import butterknife.BindView;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import io.reactivex.functions.Consumer;

/**
 * Login Activity
 *
 * 1.忘记密码
 *
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.register_btn)
    TextView mRegisterLogin;
    @BindView(R.id.edit_username_login)
    EditText mEditUsernameLogin;
    @BindView(R.id.edit_password_login)
    EditText mEditPasswordLogin;

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        RxView.clicks(mRegisterLogin)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object mO) throws Exception {
                        Intent mIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                        startActivity(mIntent);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
        String username = mEditUsernameLogin.getText().toString();
        String password = mEditPasswordLogin.getText().toString();
        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if(e == null){
                    if(mAVUserFinal != null){
                        LCChatKit.getInstance().open(mAVUserFinal.getObjectId(), new AVIMClientCallback() {
                            @Override
                            public void done(AVIMClient avimClient, AVIMException e) {
                                if(e != null){
                                    Log.i(TAG, "登录失败" + e);
                                }else{
                                    toast("登录成功",0);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
                    }
                }else{
                    toast("登录失败",0);
                    Log.i(TAG, "登录失败:" + e);
                }
            }
        });
    }
}
