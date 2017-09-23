package androidlab.edu.cn.nucyixue.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVUser;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dreamY on 2017/7/20.
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    protected  Unbinder unbinder;
    protected AVUser mAVUserFinal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(getResourcesLayout(),container,false);
        unbinder=ButterKnife.bind(this,mView);
        Log.i(TAG, "onCreateView: "+getResourcesLayout());
        mAVUserFinal = AVUser.getCurrentUser();
        init(mView,savedInstanceState);
        logic();
        return mView;
    }

    protected abstract void init(View mView, Bundle mSavedInstanceState);

    protected abstract int getResourcesLayout();

    protected abstract void logic();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: "+getResourcesLayout());

    }
}
