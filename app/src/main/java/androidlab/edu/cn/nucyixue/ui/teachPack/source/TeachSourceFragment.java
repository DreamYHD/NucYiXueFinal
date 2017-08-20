package androidlab.edu.cn.nucyixue.ui.teachPack.source;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseFragment;
import androidlab.edu.cn.nucyixue.base.RecyclerOnClickListener;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeachSourceFragment extends BaseFragment {
    private static final String TAG = "TeachSourceFragment";
    @BindView(R.id.teach_source_recycler)
    RecyclerView mTeachSourceRecycler;
    @BindView(R.id.add_file)
    FloatingActionButton mAddFile;
    Unbinder unbinder;
    private TeachSourceAdapter mTeacherSourceAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    public static TeachSourceFragment getInstance() {

        // Required empty public constructor
        return new TeachSourceFragment();
    }


    @Override
    protected void init() {
        mTeacherSourceAdapter = new TeachSourceAdapter(getContext());
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mTeachSourceRecycler.setLayoutManager(mLinearLayoutManager);
        mTeachSourceRecycler.setAdapter(mTeacherSourceAdapter);


    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_teach_zhibo;
    }

    @Override
    protected void logic() {
        mTeacherSourceAdapter.setOnItemClickListener(new RecyclerOnClickListener() {
            @Override
            public void click(View mView, int position) {

            }
        });
    }


    @OnClick(R.id.add_file)
    public void onViewClicked() {
        Intent mIntent = new Intent(getContext(),TeachSourceFileUpdataActivity.class);
        startActivity(mIntent);
    }
}
