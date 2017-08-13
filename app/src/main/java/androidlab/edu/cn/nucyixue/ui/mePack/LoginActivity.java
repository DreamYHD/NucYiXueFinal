package androidlab.edu.cn.nucyixue.ui.mePack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseActivity;
import butterknife.BindView;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.forget_login)
    TextView mForgetLogin;
    @BindView(R.id.login_btn)
    TextView mRegistLogin;

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        mRegistLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                Intent mIntent = new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(mIntent);
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }


}
