package androidlab.edu.cn.nucyixue.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class XuanshangFragment extends BaseFragment {


    public static XuanshangFragment getInstance(){
        return new XuanshangFragment();
    }


    @Override
    protected void init() {

    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_xuanshang;
    }

    @Override
    protected void logic() {

    }
}
