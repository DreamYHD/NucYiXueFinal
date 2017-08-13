package androidlab.edu.cn.nucyixue.ui.teachPack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dreamY on 2017/7/24.
 */

public class TeachFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[]mStringList;
    private List<Fragment>mFragmentList = new ArrayList<>();

    public TeachFragmentPagerAdapter(FragmentManager fm,String []mStringList,ArrayList mFragmentList) {
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
