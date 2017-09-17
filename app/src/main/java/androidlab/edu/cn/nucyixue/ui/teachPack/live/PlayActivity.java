package androidlab.edu.cn.nucyixue.ui.teachPack.live;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.avos.avoscloud.AVUser;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.data.bean.Live;
import androidlab.edu.cn.nucyixue.utils.config.LCConfig;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayActivity extends AppCompatActivity {
    private static final String TAG = "PlayActivity";

    @BindView(R.id.live_play)
    TXCloudVideoView mCaptView;
    @BindView(R.id.stop_play)
    Button mStopPlay;
    @BindView(R.id.pause_stop)
    Button mPauseStop;
    @BindView(R.id.again_play)
    Button mAgainPlay;
    @BindView(R.id.jiasu)
    Button mJiasu;

    private TXLivePlayer mLivePlayer;
    private String flvUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        init();

    }
    /*
       播放地址 (RTMP)：	rtmp://10305.liveplay.myqcloud.com/live/10305_d716621c0f
       播放地址 (FLV)：	http://10305.liveplay.myqcloud.com/live/10305_d716621c0f.flv
       播放地址 (HLS)：	http://10305.liveplay.myqcloud.com/live/10305_d716621c0f.m3u8
     */
    private void init() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Live live = bundle.getParcelable(LCConfig.getLIVE_TABLE());
            if(live != null){
                Log.i(TAG, live.toString());


                //mPlayerView即step1中添加的界面view
                //创建player对象
                mLivePlayer = new TXLivePlayer(this);
                //关键player对象与界面view
                mLivePlayer.setPlayerView(mCaptView);
                //当前知道的主播的id
                String url = live.getObjectId();
                flvUrl = "http://10305.liveplay.myqcloud.com/live/10305_" + url + ".flv";
                Log.i(TAG, "onCreate: " + flvUrl);
                mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_LIVE_FLV); //推荐FLV
                mLivePlayer.setRenderMode(0);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLivePlayer.stopPlay(true); // true代表清除最后一帧画面
        mCaptView.onDestroy();
    }

    @OnClick(R.id.stop_play)
    public void onMStopPlayClicked() {
        mLivePlayer.stopPlay(true); // true代表清除最后一帧画面
    }

    @OnClick(R.id.pause_stop)
    public void onMPauseStopClicked() {
        // 暂停
        mLivePlayer.pause();
    }

    @OnClick(R.id.again_play)
    public void onMAgainPlayClicked() {
        // 继续
        mLivePlayer.resume();
    }

    @OnClick(R.id.jiasu)
    public void onMJiasuClicked() {
        mLivePlayer.stopPlay(true);
        mLivePlayer.enableHardwareDecode(true);
        mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_LIVE_FLV);
    }

}
