package androidlab.edu.cn.nucyixue.ui.mePack;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        BmobSMS.initialize(this,"6ac3eb1e1846f7b8c6a6c1eb83439666");
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist;
    }
    @OnClick(R.id.regist_btn)
    public void regist(){
        BmobSMS.verifySmsCode(this, mPhoneRegist.getText().toString(), mYanzhengmaRegist.getText().toString().trim(), new VerifySMSCodeListener() {
            @Override
            public void done(BmobException ex) {
                if(ex == null){
                 snackBar(findViewById(R.id.regist_btn),"注册成功",0);
                }else {

                }
            }
        });

    }
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
