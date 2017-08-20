package androidlab.edu.cn.nucyixue.ui.mePack;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class RegistActivity extends BaseActivity {

    private static final String TAG = "RegistActivity";
    @BindView(R.id.username_regist)
    EditText mUsernameRegist;
    @BindView(R.id.yanzhengma_regist)
    EditText mYanzhengmaRegist;
    @BindView(R.id.password_regist)
    EditText mPasswordRegist;
    @BindView(R.id.major_regist)
    EditText mMajorRegist;
    @BindView(R.id.schoolname_regist)
    EditText mSchoolnameRegist;
    @BindView(R.id.phone_regist)
    EditText mPhoneRegist;
    @BindView(R.id.get_yzm_btn)
    Button mGetYzmBtn;
    @BindView(R.id.regist_btn)
    Button mRegistBtn;
    @BindView(R.id.back_regist_image)
    ImageView mBackRegistImage;
    Boolean isTrue = false;
    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        //初始化bmob短信验证
        BmobSMS.initialize(this,"6ac3eb1e1846f7b8c6a6c1eb83439666");
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist;
    }

    /**
     * 注册逻辑
     */
    @OnClick(R.id.regist_btn)
    public void regist(){
        String userName = mUsernameRegist.getText().toString();
        String passWorld = mPasswordRegist.getText().toString();
        String major = mMajorRegist.getText().toString();
        String phone = mPhoneRegist.getText().toString();
        String school = mSchoolnameRegist.getText().toString();
        String yanzhengma = mYanzhengmaRegist.getText().toString();
        final AVUser mAVUser = new AVUser();
        mAVUser.setUsername(userName);
        mAVUser.setPassword(passWorld);
        mAVUser.setMobilePhoneNumber(phone);
        mAVUser.put("school",school);
        mAVUser.put("major",major);
        BmobSMS.verifySmsCode(this, mPhoneRegist.getText().toString(), mYanzhengmaRegist.getText().toString().trim(), new VerifySMSCodeListener() {
            @Override
            public void done(BmobException ex) {
                if(ex == null){
                    isTrue = true;
                    snackBar(findViewById(R.id.regist_btn),"验证码正确",0);
                }else {

                }
            }
        });
        mAVUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException mE) {
                if (mE == null){
                    if (isTrue){
                        Log.i(TAG, "done: success regist");
                        toast("注册成功",0);
                        mActivity.finish();
                    }
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

    /**
     * 获取验证码
     */
    @OnClick(R.id.get_yzm_btn)
    public void getGetYzmBtn(){
        Log.i(TAG, "regist: do click");
        BmobSMS.requestSMSCode(this, mPhoneRegist.getText().toString(), "注册验证码", new RequestSMSCodeListener() {
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
    @OnClick(R.id.back_regist_image)
    public void back(){
        this.finish();
    }
}
