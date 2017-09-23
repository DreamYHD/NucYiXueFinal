package androidlab.edu.cn.nucyixue.ui.teachPack.source;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseFragment;
import androidlab.edu.cn.nucyixue.base.BaseRecyclerAdapter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeachSourceFragment extends BaseFragment {
    private static final String TAG = "TeachSourceFragment";


    private List<AVObject> mListRecycler;
    @BindView(R.id.teach_source_recycler)
    RecyclerView mTeachSourceRecycler;
    @BindView(R.id.add_file)
    FloatingActionButton mAddFile;
    private TeachSourceAdapter mTeacherSourceAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private FileDownloadService.MyBinder mBinder;
    boolean isRegist = false;

    public static TeachSourceFragment getInstance() {
        return new TeachSourceFragment();
    }

    @Override
    protected void init(View mView, Bundle mSavedInstanceState) {
        mListRecycler = new ArrayList<>();
        mTeacherSourceAdapter = new TeachSourceAdapter(R.layout.fragment_teach_source_item, getContext(), mListRecycler);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mTeachSourceRecycler.setLayoutManager(mLinearLayoutManager);
        mTeachSourceRecycler.setAdapter(mTeacherSourceAdapter);
        AVQuery<AVObject> mAVQuery = new AVQuery<>("FileInfo");
        mAVQuery.orderByDescending("createdAt");
        mAVQuery.include("targetTodoFolder.targetAVUser");
        mAVQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> mList, AVException mE) {
                if (mE == null) {
                    mListRecycler.addAll(mList);
                    Log.i(TAG, "done: " + mListRecycler.size());
                    if (mListRecycler.size() > 0) {
                        mTeacherSourceAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_teach_zhibo;
    }
    @Override
    protected void logic() {
        mTeacherSourceAdapter.setOnClickerListener(new BaseRecyclerAdapter.OnClickerListener() {
            @Override
            public void click(View mView, final int position) {
                Intent mIntent = new Intent(getContext(),TeachSourceFileDownActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("fileId",mListRecycler.get(position).getObjectId().toString());
                Log.i(TAG, "click: "+mListRecycler.get(position).getObjectId().toString());
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
            }
        });
    }
    @OnClick(R.id.add_file)
    public void onViewClicked() {
        Intent mIntent = new Intent(getContext(), TeachSourceFileUpdataActivity.class);
        startActivity(mIntent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
