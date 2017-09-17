package androidlab.edu.cn.nucyixue.ui.teachPack.live;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.avos.avoscloud.AVUser;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.data.bean.Live;
import androidlab.edu.cn.nucyixue.utils.RtmpUtils;
import androidlab.edu.cn.nucyixue.utils.config.LCConfig;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PushActivity extends AppCompatActivity {

    private static final String TAG = "PushActivity";
    @BindView(R.id.push_live)
    TXCloudVideoView mCaptview;
    @BindView(R.id.start_live)
    Button mStartLive;
    @BindView(R.id.stop_live)
    Button mStopLive;
    @BindView(R.id.change)
    Button mChange;
    @BindView(R.id.luzhi_live)
    Button mLuzhiLive;
    Button mStartShanguangdeng;
    private TXLivePusher mLivePusher;
    private TXLivePushConfig mLivePushConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mLivePusher = new TXLivePusher(this);
        mLivePushConfig = new TXLivePushConfig();
        //设置自动对焦
        mLivePushConfig.setTouchFocus(true);
        //设置视频水印 必须设置且为png
        //mLivePushConfig.setWatermark(BitmapFactory.decodeResource(getResources(),R.drawable.test), 10, 10);
        mLivePusher.setConfig(mLivePushConfig);
    }

    @OnClick(R.id.start_live)
    public void onMStartLiveClicked() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Log.i(TAG, "GET BUNDLE");
            Live live = bundle.getParcelable(LCConfig.getLIVE_TABLE());
            if(live != null){
                Log.i(TAG, live.toString());

                String id = live.getObjectId();
                Date date = live.getStartAt();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                //获取live开启时间
                String time = format.format(date);
                //获取推流地址
                String rtmp = RtmpUtils.getSafeUrl(id, time);
                Log.i(TAG, "onMStartLiveClicked: ");
                //告诉 SDK 音视频流要推到哪个推流URL上去。
                mLivePusher.startPusher(rtmp);
                //界面元素和Pusher对象关联起来，从而能够将手机摄像头采集到的画面渲染到屏幕上。
                mLivePusher.startCameraPreview(mCaptview);
                //设置清晰度
                setVideoQuality(mLivePusher, 1, true, true);
                //设置美颜
                setBeautiful(mLivePusher, 0, 1, 1, 1);
            }
        }


    }

    /**
     * @param mLivePusher
     * @param mI          磨皮风格：  0：光滑  1：自然  2：朦胧
     * @param mI1         磨皮等级： 取值为0-9.取值为0时代表关闭美颜效果.默认值:0,即关闭美颜效果.
     * @param mI2         美白等级： 取值为0-9.取值为0时代表关闭美白效果.默认值:0,即关闭美白效果.
     * @param mI3         红润等级： 取值为0-9.取值为0时代表关闭美白效果.默认值:0,即关闭美白效果.
     */
    private void setBeautiful(TXLivePusher mLivePusher, int mI, int mI1, int mI2, int mI3) {
        mLivePusher.setBeautyFilter(mI, mI1, mI2, mI3);
    }

    /**
     * @param mLivePusher
     * @param mI          SDK 提供了六种基础档位，根据我们服务大多数客户的经验进行积累和配置。其中 STANDARD、HIGH、SUPER 适用于直播模式，MAIN_PUBLISHER 和 SUB_PUBLISHER 适用于连麦直播中的大小画面，VIDEOCHAT 用于实时音视频。
     * @param mB          是否开启 Qos 流量控制，开启后SDK 会根据主播上行网络的好坏自动调整视频码率。相应的代价就是，主播如果网络不好，画面会很模糊且有很多马赛克。
     * @param mB1         是否允许动态分辨率，开启后 SDK 会根据当前的视频码率选择相匹配的分辨率，这样能获得更好的清晰度。相应的代价就是，动态分辨率的直播流所录制下来的文件，在很多播放器上会有兼容性问题。
     */
    private void setVideoQuality(TXLivePusher mLivePusher, int mI, boolean mB, boolean mB1) {
        mLivePusher.setVideoQuality(1, true, true);
    }

    @OnClick(R.id.stop_live)
    public void onMStopLiveClicked() {
        mLivePusher.stopCameraPreview(true); //停止摄像头预览
        mLivePusher.stopPusher();            //停止推流
        mLivePusher.setPushListener(null);   //解绑 listener
    }

    @OnClick(R.id.change)
    public void onMChangeClicked() {
        // 默认是前置摄像头
        mLivePusher.switchCamera();
    }

    @OnClick(R.id.luzhi_live)
    public void onMLuzhiLiveClicked() {
    }

    // activity 的 onStop 生命周期函数
    @Override
    public void onStop(){
        super.onStop();
        mCaptview.onPause();  // mCaptureView 是摄像头的图像渲染view
        mLivePusher.pausePusher(); // 通知 SDK 进入“后台推流模式”了
    }
    // activity 的 onStop 生命周期函数
    @Override
    public void onResume() {
        super.onResume();
        mCaptview.onResume();     // mCaptureView 是摄像头的图像渲染view
        mLivePusher.resumePusher();  // 通知 SDK 重回前台推流
    }
    /**
     * 判断Activity是否可旋转。只有在满足以下条件的时候，Activity才是可根据重力感应自动旋转的。
     * 系统“自动旋转”设置项打开；
     * @return false---Activity可根据重力感应自动旋转
     */
    protected boolean isActivityCanRotation() {
        // 判断自动旋转是否打开
        int flag = Settings.System.getInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        if (flag == 0) {
            return false;
        }
        return true;
    }
}
