package androidlab.edu.cn.nucyixue.ui;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;


import androidlab.edu.cn.nucyixue.R;
import io.reactivex.functions.Consumer;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        RxPermissions mRxPermissions = new RxPermissions(this);
        startActivity(intent);
        mRxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.SEND_SMS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission mPermission) throws Exception {
                        if (mPermission.granted){
                            Log.i(TAG, "accept: success "+mPermission.name);
                        }else {
                            Log.e(TAG, "accept: fail"+mPermission.name );
                        }
                    }
                });
        this.finish();
    }

}