package androidlab.edu.cn.nucyixue.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by dreamY on 2017/7/20.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected FragmentManager mFragmentManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ButterKnife.bind(this);
        mFragmentManager=getSupportFragmentManager();
        logicActivity(savedInstanceState);
    }
    protected abstract void logicActivity(Bundle mSavedInstanceState);

    protected abstract int getLayoutId();
    public void toast(String toast,int time){
        if (time == 0){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }
    public void snackBar(View v,String snackBar, int time){
        if (time==0){
            Snackbar.make(v,snackBar,Snackbar.LENGTH_SHORT)
                    .show();
        }else {
            Snackbar.make(v,snackBar,Snackbar.LENGTH_LONG)
                    .show();
        }
    }

}
