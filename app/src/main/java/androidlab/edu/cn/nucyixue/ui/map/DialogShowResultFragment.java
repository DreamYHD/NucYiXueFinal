package androidlab.edu.cn.nucyixue.ui.map;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;

import java.util.List;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseRecyclerAdapter;
import androidlab.edu.cn.nucyixue.ui.teachPack.xuanshangPack.XunshangSendImageAdapter;
import androidlab.edu.cn.nucyixue.utils.BigImageDialog;
import androidlab.edu.cn.nucyixue.utils.config.LCConfig;
import androidlab.edu.cn.nucyixue.utils.config.MTConfig;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 用来展示最后的成果
 */
public class DialogShowResultFragment extends DialogFragment {
    private static final String TAG = "DialogShowResultFragmen";
    @BindView(R.id.dialog_show_result_title)
    TextView mDialogShowResultTitle;
    @BindView(R.id.dialog_result_description1)
    TextView mDialogResultDescription1;
    @BindView(R.id.dialog_result_description2)
    TextView mDialogResultDescription2;
    @BindView(R.id.dialog_show_result_recycler)
    RecyclerView mDialogShowResultRecycler;
    @BindView(R.id.share_qq)
    ImageView mShareQq;
    @BindView(R.id.share_wechat)
    ImageView mShareWechat;
    Unbinder unbinder;
    private String mTeamId;
    private List list_images;
    private XunshangSendImageAdapter mXunshangSendImageAdapter;
    public DialogShowResultFragment(String mId) {
        mTeamId = mId;
    }
    public static DialogShowResultFragment getInstance(String id) {
        return new DialogShowResultFragment(id);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_show_result, container, false);
        unbinder = ButterKnife.bind(this, view);
        mXunshangSendImageAdapter = new XunshangSendImageAdapter(R.layout.activity_xuanshang_send_image_item,getContext(),list_images);
        mDialogShowResultRecycler.setAdapter(mXunshangSendImageAdapter);
        mDialogShowResultRecycler.setLayoutManager(new GridLayoutManager(getContext(),4));
        mXunshangSendImageAdapter.setOnClickerListener(new BaseRecyclerAdapter.OnClickerListener() {
            @Override
            public void click(View mView, int position) {
                showBigImage(mView, position);
            }
        });
        logic(view);
        return view;
    }

    private void showBigImage(View mView, int mPosition) {
        BigImageDialog bigImageDialog=new BigImageDialog(getContext(), R.style.DialogBigImage,list_images.get(mPosition).toString());
        bigImageDialog.show();
    }
    private void logic(View mView) {
        AVQuery<AVObject>mTeam = new AVQuery<>(MTConfig.getTABLE_NAME());
        mTeam.getInBackground(mTeamId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject mAVObject, AVException mE) {
                if ( null == mE ){
                    List<String>people_list = (List) mAVObject.get("people"); //获取小组成员
                    int people_num = people_list.size();
                    String start_time = mAVObject.getString(MTConfig.getTEAM_START_TIME());
                    String end_time = mAVObject.getString(MTConfig.getTEAM_END_TIME());
                    String team_plan = mAVObject.getString(MTConfig.getTEAM_DESCRIPTION());
                    String team_name = mAVObject.getString(MTConfig.getTEAM_NAME());
                    list_images = mAVObject.getList(MTConfig.getTEAM_IMAGES());
                    mXunshangSendImageAdapter.notifyDataSetChanged();
                    final StringBuilder all_name = new StringBuilder();
                    for (String people_id:
                        people_list ) {
                        AVQuery<AVUser> mUser = new AVQuery<AVUser>(LCConfig.getUSER_TABLE());
                        mUser.getInBackground(people_id, new GetCallback<AVUser>() {
                            @Override
                            public void done(AVUser mAVUser, AVException mE) {
                                String user_name = mAVUser.getString(LCConfig.getUI_USER_NAME());
                                all_name.append(user_name).append(",");
                            }
                        });
                    }
                    String show_title = "恭喜"+team_name;
                    StringBuilder description_1 = all_name.append(people_num)
                            .append("名同学在")
                            .append(start_time)
                            .append("至")
                            .append(end_time)
                            .append("时间里完成了以下任务\n");
                    mDialogShowResultTitle.setText(show_title);
                    mDialogResultDescription1.setText(description_1);
                    mDialogResultDescription2.setText(team_plan);
                }else{
                    Log.e(TAG, "done: " + mE.getMessage());
                }
            }
        });
    }
    /*
     *設置全屏
    */
    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @OnClick({R.id.share_qq, R.id.share_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_qq:
                shareqq(view);
                break;
            case R.id.share_wechat:
                sharewechat(view);
                break;
        }
    }
    private void sharewechat(View mView) {

    }
    private void shareqq(View mView) {
    }
}
