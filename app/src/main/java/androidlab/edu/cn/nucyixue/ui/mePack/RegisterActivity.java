package androidlab.edu.cn.nucyixue.ui.mePack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import androidlab.edu.cn.nucyixue.MyApp;
import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseActivity;
import androidlab.edu.cn.nucyixue.data.bean.UserInfo;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * Register Activity
 */
public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";

    @BindView(R.id.username_register)
    EditText mUsernameRegister;
    @BindView(R.id.code_register)
    EditText mCodeRegister;
    @BindView(R.id.password_register)
    EditText mPasswordRegister;
    @BindView(R.id.major_register)
    EditText mMajorRegister;
    @BindView(R.id.school_register)
    EditText mSchoolNameRegister;
    @BindView(R.id.phone_register)
    EditText mPhoneRegister;

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        BmobSMS.initialize(this,"6ac3eb1e1846f7b8c6a6c1eb83439666");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    /**
     * 注册逻辑
     */
    @OnClick(R.id.register_btn)
    public void register(){
        String phone = mPhoneRegister.getText().toString();
        String code = mCodeRegister.getText().toString();

        if(MyApp.isDebug){
            uploadInfo();
        }else{
            BmobSMS.verifySmsCode(this, phone, code.trim(), new VerifySMSCodeListener() {
                @Override
                public void done(BmobException ex) {
                    if(ex == null){
                        snackBar(mUsernameRegister,"验证码正确",0);

                        uploadInfo();
                    }else {
                        snackBar(mUsernameRegister, "获取验证码失败", 0);
                        Log.i(TAG, "获取验证码失败："+ex.toString());
                    }
                }
            });
        }

    }

    private void uploadInfo() {
        final AVUser mAVUser = new AVUser();
        mAVUser.setUsername(mUsernameRegister.getText().toString());
        mAVUser.setPassword(mPasswordRegister.getText().toString());
        mAVUser.setMobilePhoneNumber(mPhoneRegister.getText().toString());
        mAVUser.put("school",mSchoolNameRegister.getText().toString());
        mAVUser.put("major",mMajorRegister.getText().toString());

        mAVUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException mE) {
                if (mE == null){
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUsername(mUsernameRegister.getText().toString());
                    userInfo.setUserId(mAVUser.getObjectId());
                    userInfo.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e != null){
                                Log.i(TAG, "上传 UserInfo 失败：" + e.toString());
                            }else{
                                Log.i(TAG, "done: success register");
                                toast("注册成功",0);

                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    });
                }else {
                    if (mE.getCode() == 214){
                        toast("手机号已经注册",0);
                    }
                    if (mE.getCode() == 217){
                        toast("无效的手机号码",0);
                    }
                    if (mE.getCode() == 202){
                        toast("用户名已经存在",0);
                    }
                }
            }
        });
    }

    @OnClick(R.id.get_yzm_btn)
    public void getGetYzmBtn(){
        BmobSMS.requestSMSCode(this, mPhoneRegister.getText().toString(), "注册验证码", new RequestSMSCodeListener() {
            @Override
            public void done(Integer mInteger, BmobException mE) {
                if (mE== null){
                    Log.i(TAG, "done: sms send success"+mInteger);
                }else {
                    Log.e(TAG, "done: failed"+mInteger );
                }
            }
        });
    }

    @OnClick(R.id.back_register_image)
    public void back(){
        finish();
    }


}
