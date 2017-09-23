package androidlab.edu.cn.nucyixue.ui.map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.FileNotFoundException;
import java.util.List;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.utils.FileUtils;
import androidlab.edu.cn.nucyixue.utils.config.MTConfig;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static androidlab.edu.cn.nucyixue.ui.teachPack.xuanshangPack.XuanshangSendActivity.SELECT_CODE;

/**
 * Created by dreamY on 2017/9/8.
 */
public class DialogShowInviteFragment extends DialogFragment {
    private static final String TAG = "DialogShowInviteFragmen";
    @BindView(R.id.invite_team_name)
    TextView mInviteTeamName;
    @BindView(R.id.invite_team_num)
    TextView mInviteTeamNum;
    @BindView(R.id.invite_team_time)
    TextView mInviteTeamTime;
    @BindView(R.id.invite_team_plan)
    TextView mInviteTeamPlan;
    @BindView(R.id.want_join_btn)
    Button mWantJoinBtn;
    Unbinder unbinder;
    private String id;
    private List<String> image_list;
    private List<String> mStringList;

    public DialogShowInviteFragment(String mId) {
        id = mId;
    }

    public static DialogShowInviteFragment getInstance(String mId) {
        return new DialogShowInviteFragment(mId);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_dialog_show_invite, container);
        unbinder = ButterKnife.bind(this, mView);
        logic();
        return mView;
    }
    private void logic() {
        final AVQuery<AVObject> mTeam = new AVQuery<>(MTConfig.getTABLE_NAME());
        mTeam.getInBackground(id, new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject mAVObject, AVException mE) {
                if (mE == null) {
                    String current_user_id = AVUser.getCurrentUser().getObjectId();
                    String team_name = mAVObject.getString(MTConfig.getTEAM_NAME());
                    String team_start_time = mAVObject.getString(MTConfig.getTEAM_START_TIME());
                    String team_plan = mAVObject.getString(MTConfig.getTEAM_DESCRIPTION());
                    String team_leader = mAVObject.getString(MTConfig.getTEAM_LEADER());
                    final List mPeople = mAVObject.getList(MTConfig.getTEAM_PEOPLE());
                    int need_people_num = mAVObject.getInt(MTConfig.getTEAM_NUM());
                    int current_people_num = mPeople.size();
                    String team_num = getString(R.string.need_people_num_code) + need_people_num + getString(R.string.cuerrent_people_num_code) + current_people_num;
                    if (need_people_num == current_people_num && current_user_id.equals(team_leader)) {
                        mWantJoinBtn.setText(R.string.finish_code);
                    } else if (need_people_num == current_people_num) {
                        mWantJoinBtn.setText(R.string.team_is_on_code);
                    }
                    mInviteTeamName.setText(team_name);
                    mInviteTeamTime.setText(team_start_time);
                    mInviteTeamNum.setText(team_num);
                    mInviteTeamPlan.setText(team_plan);
                    mWantJoinBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View mView) {
                            if (mWantJoinBtn.getText().equals(getString(R.string.finish_code))) {
                                Toast.makeText(getContext(), "请选择你们的学习回忆", Toast.LENGTH_SHORT).show();
                                Matisse.from(getParentFragment())
                                        .choose(MimeType.allOf())
                                        .theme(R.style.Matisse_Dracula)
                                        .countable(false)
                                        .maxSelectable(9)
                                        .imageEngine(new GlideEngine())
                                        .forResult(SELECT_CODE);

                            } else if (mWantJoinBtn.getText().equals(getString(R.string.team_is_on_code))) {
                                Toast.makeText(getContext(), "小组人数满了，去看看其他的吧", Toast.LENGTH_SHORT).show();
                            } else if (isOnTeam(mPeople)){
                                Toast.makeText(getContext(), "你已经在小组了", Toast.LENGTH_SHORT).show();
                            } else {
                                List mList = mPeople;
                                mList.add(AVUser.getCurrentUser().getObjectId());
                                mAVObject.put(MTConfig.getTEAM_PEOPLE(),mList);
                                Toast.makeText(getContext(), "成功加入小组，快去做任务吧", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    /*
    *设置全屏
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_CODE) {
                Log.i(TAG, "onActivityResult: select_code");
                List<Uri> mList = Matisse.obtainResult(data);
                for (Uri m :
                        mList) {
                    image_list.add(m.toString());
                    for (int j = 0; j < image_list.size(); j++) {
                        final AVFile file;
                        try {
                            String path = FileUtils.getFilePahtFromUri(getContext(), Uri.parse(image_list.get(j)));
                            file = AVFile.withAbsoluteLocalPath(FileUtils.getFileName(path), path);
                            file.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException mE) {
                                    if (mE == null) {
                                        mStringList.add(file.getUrl());
                                        Toast.makeText(getContext(),"回忆上传成功", Toast.LENGTH_SHORT).show();
                                        Log.i(TAG, "done: " + file.getUrl());
                                    } else {
                                        Log.e(TAG, "done: " + mE.getMessage());
                                    }
                                }
                            });
                        } catch (FileNotFoundException mE) {
                            mE.printStackTrace();
                        }
                    }
                    Log.i(TAG, "onActivityResult: " + m.toString());
                }
            }
        }
    }

    /**
     * 判断当前用户是不是在小组中
     * @param mPeople 小组的所有成员
     * @return
     * */
    public boolean isOnTeam(List mPeople) {
        boolean mOnTeam = false;
        for (Object s:
             mPeople) {
            if (AVUser.getCurrentUser().getObjectId().equals(s.toString())){
                mOnTeam =true;
                return mOnTeam;
            }
        }
        return mOnTeam;
    }
}
