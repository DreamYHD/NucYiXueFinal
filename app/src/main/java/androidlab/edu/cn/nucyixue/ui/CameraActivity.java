package androidlab.edu.cn.nucyixue.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.lisenter.ErrorLisenter;
import com.cjt2325.cameralibrary.lisenter.JCameraLisenter;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.utils.FileUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    @BindView(R.id.jcameraview)
    JCameraView mJcameraview;

    @Override
    protected void onResume() {
        super.onResume();
        mJcameraview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mJcameraview.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        //设置相机全屏
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }

        //JCameraView监听
        mJcameraview.setErrorLisenter(new ErrorLisenter() {
            @Override
            public void onError() {
                //打开Camera失败回调
                Log.i(TAG, "open camera error");
            }
            @Override
            public void AudioPermissionError() {
                //没有录取权限回调，没有用到
                Log.i(TAG, "AudioPermissionError");
            }
        });

        mJcameraview.setJCameraLisenter(new JCameraLisenter() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                Log.i(TAG, "bitmap = " + bitmap.getWidth());
                String path = FileUtils.saveBitmap("JCamera", bitmap);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                Log.i(TAG, "captureSuccess: "+path);
                setResult(101, intent);
                finish();
            }
            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                Log.i(TAG, "url = " + url);
            }
            @Override
            public void quit() {
                //退出按钮
                CameraActivity.this.finish();
            }
        });
    }
}
