package androidlab.edu.cn.nucyixue.ui.teachPack.source;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class TeachSourceFileDownActivity extends BaseActivity {
    public String url = null;
    public String type =null;
    public String name =null;
    public int downNum;
    private static final String TAG = "TeachSourceFileDownActi";
    @BindView(R.id.source_file_down_toolbar)
    Toolbar mSourceFileDownToolbar;
    @BindView(R.id.source_down_title)
    TextView mSourceDownTitle;
    @BindView(R.id.source_down_school)
    TextView mSourceDownSchool;
    @BindView(R.id.source_down_user)
    TextView mSourceDownUser;
    @BindView(R.id.source_down_time)
    TextView mSourceDownTime;
    @BindView(R.id.source_text_show)
    TextView mSourceTextShow;
    @BindView(R.id.source_down_showFileName)
    TextView mSourceDownShowFileName;
    @BindView(R.id.source_down_btn)
    ImageView mSourceDownBtn;
    @BindView(R.id.source_down_progress_bar)
    ProgressBar mSourceDownProgressBar;
    private AVObject mAVObject;
    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {


        setSupportActionBar(mSourceFileDownToolbar);
        mSourceFileDownToolbar.setNavigationIcon(R.drawable.back);
        mSourceFileDownToolbar.setTitle("");
        mSourceFileDownToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mSourceFileDownToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                mActivity.finish();
            }
        });
        Bundle mBundle = getIntent().getExtras();
        mAVObject = AVObject.createWithoutData("FileInfo",mBundle.getString("fileId"));
        mAVObject.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject mAVObject, AVException mE) {
                if ( mE == null){
                    mSourceDownTitle.setText(mAVObject.get("title").toString());
                    name = mAVObject.get("title").toString();
                    mSourceDownSchool.setText(mAVObject.get("school").toString());
                    mSourceDownUser.setText("上传用户 "+mAVObject.get("user").toString());
                    mSourceDownTime.setText("上传时间 "+mAVObject.get("time").toString());
                    type = mAVObject.get("type").toString();
                    mSourceDownShowFileName.setText(name+"."+type);
                    mSourceTextShow.setText(mAVObject.get("description").toString());
                    String []m = mAVObject.get("fileId").toString().split("\"");
                    Log.i(TAG, "done: success "+m[7]);
                    AVQuery<AVObject> query = new AVQuery<>("_File");
                    query.getInBackground(m[7], new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (e == null) {
                                Log.d("saved", "文件找到了");
                                url = avObject.getString("url");

                            } else {
                                Log.d("saved", "出错了" + e.getMessage());
                            }
                        }
                    });

                }
                downNum = mAVObject.getInt("downnun");
            }
        });
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_teach_source_file_down2;
    }

    @OnClick(R.id.source_down_btn)
    public void onViewClicked() {
        mSourceDownProgressBar.setVisibility(View.VISIBLE);
        Log.i(TAG, "onViewClicked: "+url);
        AVFile file = new AVFile("资源文件", url, new HashMap<String, Object>());
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, AVException e) {
                if (e == null) {
                    Log.d("saved", "文件大小" + bytes.length);
                } else {
                    Log.d("saved", "出错了" + e.getMessage());
                }
                File downloadedFile = new File("/storage/emulated/0/" + name+"."+type);
                FileOutputStream fout = null;

                try {
                    fout = new FileOutputStream(downloadedFile);
                    fout.write(bytes);
                    snackBar(findViewById(R.id.source_down_btn), "文件下载成功" + downloadedFile.getPath().toString(), 1);
                    Log.d("saved", "文件写入成功."+downloadedFile.getPath().toString());
                    mAVObject.put("downnum",++downNum);
                    mAVObject.saveInBackground();
                    fout.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                    Log.d("saved", "文件找不到.." + e1.getMessage()+e.toString());
                } catch (IOException e1) {
                    Log.d("saved", "文件读取异常.");
                }

            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer integer) {
                mSourceDownProgressBar.setProgress(integer);
                Log.d("saved", "文件下载进度" + integer);
            }
        });
    }
}
