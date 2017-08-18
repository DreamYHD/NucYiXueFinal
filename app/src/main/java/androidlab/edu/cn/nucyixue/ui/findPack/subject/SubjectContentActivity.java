package androidlab.edu.cn.nucyixue.ui.findPack.subject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseActivity;
import androidlab.edu.cn.nucyixue.utils.ActivityUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubjectContentActivity extends BaseActivity {


    @BindView(R.id.subject_content_toolbar)
    Toolbar mSubjectContentToolbar;
    @BindView(R.id.content_live)
    TextView mContentLive;
    @BindView(R.id.content_source)
    TextView mContentSource;
    @BindView(R.id.content_near)
    TextView mContentNear;
    @BindView(R.id.content_xuanshang)
    TextView mContentXuanshang;
    @BindView(R.id.content_content_frame)
    FrameLayout mContentContentFrame;
    @BindView(R.id.subject_content_title)
    TextView mSubjectContentTitle;

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {

        Bundle mBundle = getIntent().getExtras();
        mSubjectContentTitle.setText(mBundle.getString("subjectName"));
        mSubjectContentToolbar.setTitle("");

        setSupportActionBar(mSubjectContentToolbar);
        mSubjectContentToolbar.setNavigationIcon(R.drawable.back);

        mSubjectContentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                mActivity.finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_subject_content;
    }



    @OnClick({R.id.content_live, R.id.content_source, R.id.content_near, R.id.content_xuanshang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.content_live:
                ActivityUtils.replaceFragmentToActivity(mFragmentManager, SubjectLiveFragment.getInstance(), R.id.content_content_frame);
                break;
            case R.id.content_source:
                ActivityUtils.replaceFragmentToActivity(mFragmentManager, SubjectSourceFragment.getInstance(), R.id.content_content_frame);
                break;
            case R.id.content_near:
                ActivityUtils.replaceFragmentToActivity(mFragmentManager, SubjectNearFragment.getInstance(), R.id.content_content_frame);
                break;
            case R.id.content_xuanshang:
                ActivityUtils.replaceFragmentToActivity(mFragmentManager, SubjectXuanshangFragment.getInstance(), R.id.content_content_frame);
                break;
        }
    }

}
