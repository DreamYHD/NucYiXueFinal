package androidlab.edu.cn.nucyixue.ui.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.utils.config.MTConfig;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by dreamY on 2017/9/8.
 */

public class DialogCreateTeamFragment extends DialogFragment {
    private static final String TAG = "DialogCreateTeamFragment";
    @BindView(R.id.create_team_name)
    EditText mCreateTeamName;
    @BindView(R.id.create_team_num)
    EditText mCreateTeamNum;
    @BindView(R.id.teamstart_time_img)
    ImageView mStartTimeImg;
    @BindView(R.id.create_team_time)
    TextView mCreateTeamTime;
    @BindView(R.id.create_team_plan)
    EditText mCreateTeamPlan;
    @BindView(R.id.team_create_image)
    ImageView mTeamCreateImage;
    Unbinder unbinder;
    private LatLng mLatLng;
    private String time;
    public  DialogCreateTeamFragment(LatLng mLocation, String mTime){
        this.mLatLng = mLocation;
        this.time = mTime;
    }
    public static DialogCreateTeamFragment getInstance(LatLng location , String time){
        return new DialogCreateTeamFragment(location,time);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_dialog_create_team, container);

        unbinder = ButterKnife.bind(this, mView);
        mCreateTeamTime.setText(time);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.teamstart_time_img, R.id.team_create_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.teamstart_time_img://获取时间
                break;
            case R.id.team_create_image://上传
                sendRequest();
                break;
        }
    }
    /**
     * 上传team
     */
    private void sendRequest() {

        List<String>team_mList = new ArrayList<>();
        if (AVUser.getCurrentUser().getObjectId() != null){
            team_mList.add(AVUser.getCurrentUser().getObjectId());
            String team_Name = mCreateTeamName.getText().toString();
            int team_num = Integer.parseInt(mCreateTeamNum.getText().toString());
            String team_description = mCreateTeamPlan.getText().toString();
            Double mDoubleLat = mLatLng.latitude;
            Double mDoubleLong = mLatLng.longitude;
            String team_leader = AVUser.getCurrentUser().getObjectId();
            String team_time = time;
            //构造team
            AVObject mTeam = new AVObject(MTConfig.getTABLE_NAME());
            mTeam.put(MTConfig.getTEAM_NAME(),team_Name);
            mTeam.put(MTConfig.getTEAM_LOCATION_LAT(),mDoubleLat);
            mTeam.put(MTConfig.getTEAM_LOCATION_LONG(),mDoubleLong);
            mTeam.put(MTConfig.getTEAM_NUM(),team_num);
            mTeam.put(MTConfig.getTEAM_PEOPLE(),team_mList);
            mTeam.put(MTConfig.getTEAM_DESCRIPTION(),team_description);
            mTeam.put(MTConfig.getTEAM_START_TIME(),team_time);
            mTeam.put(MTConfig.getTEAM_LEADER(),team_leader);
            mTeam.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException mE) {
                    if (null == mE){
                        Toast.makeText(getContext(),"MapTeam提交申请，等待小伙伴的加入", Toast.LENGTH_SHORT).show();
                    }else {
                        Log.e(TAG, "done: "+mE );
                    }
                }
            });

        }else {
            Toast.makeText(getContext(), "please login in first ", Toast.LENGTH_SHORT).show();
        }

    }
    /*
     *設置全屏
     */
    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        getDialog().getWindow().setLayout( dm.widthPixels, getDialog().getWindow().getAttributes().height );
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogAnim;

    }

}
