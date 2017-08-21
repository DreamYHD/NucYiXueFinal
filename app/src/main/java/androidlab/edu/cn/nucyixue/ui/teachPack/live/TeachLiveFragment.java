package androidlab.edu.cn.nucyixue.ui.teachPack.live;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseFragment;
import butterknife.BindView;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;

/**
 * Live Fragment
 */
public class TeachLiveFragment extends BaseFragment {
    private static final String TAG = "TeachLiveFragment";

    @BindView(R.id.fragment_source_recycler)
    RecyclerView fragmentSourceRecycler;
    @BindView(R.id.add_live)
    FloatingActionButton addLive;

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

    @OnClick(R.id.add_live)
    public void onViewClicked() {
        if(LCChatKit.getInstance().getClient() != null){
            startActivity(new Intent(getContext(), CreateLiveActivity.class));
        }else{
            Snackbar.make(addLive, "请先登录", Snackbar.LENGTH_LONG).show();
        }

    }
}
