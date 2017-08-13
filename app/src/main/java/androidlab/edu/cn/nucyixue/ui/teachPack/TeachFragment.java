package androidlab.edu.cn.nucyixue.ui.teachPack;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeachFragment extends BaseFragment {

    private static final String TAG = "TeachFragment";

    @BindView(R.id.teach_main_tablayout)
    TabLayout mTeachMainTablayout;
    @BindView(R.id.teach_main_viewpager)
    ViewPager mTeachMainViewpager;
    @BindView(R.id.toolbar_teach)
    Toolbar mToolbarTeach;
    Unbinder unbinder;
    private String[] mString = {"Live", "直播", "附近"};
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private TeachFragmentPagerAdapter mTeachFragmentPagerAdapter;
    private TeachLiveFragment mLive;
    private TeachZhiboFragment mZhibo;
    private TeachNearmapFragment mNear;

    public static TeachFragment getInstance() {
        return new TeachFragment();
    }

    @Override
    protected void init() {
        mLive = TeachLiveFragment.getInstance();
        mNear = TeachNearmapFragment.getInstance();
        mZhibo = TeachZhiboFragment.getInstance();
        mFragmentList.add(mLive);
        mFragmentList.add(mZhibo);
        mFragmentList.add(mNear);
        mTeachFragmentPagerAdapter = new TeachFragmentPagerAdapter(getChildFragmentManager(), mString, mFragmentList);
        mTeachMainViewpager.setAdapter(mTeachFragmentPagerAdapter);
        mTeachMainTablayout.setupWithViewPager(mTeachMainViewpager);
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_teach;
    }

    @Override
    protected void logic() {

    }


}
