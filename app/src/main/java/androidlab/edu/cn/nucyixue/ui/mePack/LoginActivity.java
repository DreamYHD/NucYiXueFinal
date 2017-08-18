package androidlab.edu.cn.nucyixue.ui.mePack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.forget_login)
    TextView mForgetLogin;
    @BindView(R.id.login_btn)
    TextView mRegistLogin;
    @BindView(R.id.edit_phone_login)
    EditText mEditPhoneLogin;
    @BindView(R.id.edit_passworld_login)
    EditText mEditPassworldLogin;
    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        RxView.clicks(mRegistLogin)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object mO) throws Exception {
                        Intent mIntent = new Intent(LoginActivity.this,RegistActivity.class);
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
        String phone = mEditPhoneLogin.getText().toString();
        String passworld = mEditPassworldLogin.getText().toString();
        AVUser.loginByMobilePhoneNumberInBackground(phone, passworld, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser mAVUser, AVException mE) {
                if ( mE == null){
                    toast("登录成功",0);
                    mActivity.finish();
                }else if ( mE.getCode() == 210){
                    toast("密码错误",0);
                }else if ( mE.getCode() == 211){
                    toast("用户不存在",0);
                }
            }
        });
    }
}
