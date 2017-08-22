package androidlab.edu.cn.nucyixue.ui.teachPack;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseFragment;
import androidlab.edu.cn.nucyixue.ui.teachPack.live.TeachLiveFragment;
import androidlab.edu.cn.nucyixue.ui.teachPack.map.TeachMapFragment;
import androidlab.edu.cn.nucyixue.ui.teachPack.source.TeachSourceFragment;
import butterknife.BindView;

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
    private String[] mString = {"Live", "资源", "附近"};
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    public static TeachFragment getInstance() {
        return new TeachFragment();
    }

    @Override
    protected void init() {
        TeachLiveFragment mLive = TeachLiveFragment.INSTANCE;
        TeachMapFragment mNear = TeachMapFragment.INSTANCE;
        TeachSourceFragment mSource = TeachSourceFragment.getInstance();
        mFragmentList.add(mLive);
        mFragmentList.add(mSource);
        mFragmentList.add(mNear);
        TeachFragmentPagerAdapter mTeachFragmentPagerAdapter = new TeachFragmentPagerAdapter(getChildFragmentManager(), mString, mFragmentList);

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

    private class TeachFragmentPagerAdapter extends FragmentPagerAdapter {

        private String[]mStringList;
        private List<Fragment> mFragmentList = new ArrayList<>();

        TeachFragmentPagerAdapter(FragmentManager fm, String[] mStringList, List<Fragment> mFragmentList) {
            super(fm);
            this.mStringList = mStringList;
            this.mFragmentList = mFragmentList;
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mStringList[position];
        }

    }


}
