package androidlab.edu.cn.nucyixue.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseActivity;
import androidlab.edu.cn.nucyixue.ui.findPack.FindFragment;
import androidlab.edu.cn.nucyixue.ui.mePack.MeFragment;
import androidlab.edu.cn.nucyixue.ui.teachPack.TeachFragment;
import androidlab.edu.cn.nucyixue.ui.xuanshangPack.XuanshangFragment;
import androidlab.edu.cn.nucyixue.utils.ActivityUtils;
import androidlab.edu.cn.nucyixue.utils.BottomNavigationViewHelper;
import butterknife.BindView;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.bottom_menu)
    BottomNavigationView mBottomMenu;

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        if (mSavedInstanceState==null){
            ActivityUtils.replaceFragmentToActivity(mFragmentManager,FindFragment.getInstance(),R.id.content_main);
        }
        BottomNavigationViewHelper.disableShiftMode(mBottomMenu);
        mBottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.find_item:
                        ActivityUtils.replaceFragmentToActivity(mFragmentManager,FindFragment.getInstance(),R.id.content_main);
                        break;
                    case R.id.teach_item:
                        ActivityUtils.replaceFragmentToActivity(mFragmentManager, TeachFragment.getInstance(),R.id.content_main);
                        break;
                    case R.id.addque_item:
                        ActivityUtils.replaceFragmentToActivity(mFragmentManager, XuanshangFragment.getInstance(),R.id.content_main);
                        break;
                    case R.id.me_item:
                        ActivityUtils.replaceFragmentToActivity(mFragmentManager, MeFragment.getInstance(),R.id.content_main);
                        break;
                }

                return true;
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("temp", mBottomMenu.getSelectedItemId()+"");
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
