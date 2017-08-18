package androidlab.edu.cn.nucyixue.ui.teachPack;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeachLiveFragment extends BaseFragment {

    private static final String TAG = "TeachLiveFragment";

    public static TeachLiveFragment getInstance() {
        // Required empty public constructor
        return new TeachLiveFragment();
    }


    @Override
    protected void init() {

    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_teach_live;
    }

    @Override
    protected void logic() {

    }
}
