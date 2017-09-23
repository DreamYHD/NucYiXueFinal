package androidlab.edu.cn.nucyixue.ui.teachPack.source;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.leon.lfilepickerlibrary.LFilePicker;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseActivity;
import androidlab.edu.cn.nucyixue.utils.FileUtils;
import androidlab.edu.cn.nucyixue.utils.InternetUtils;
import butterknife.BindView;
import butterknife.OnClick;

public class TeachSourceFileUpdataActivity extends BaseActivity {
    private static final String TAG = "TeacherSourceFileActivity";
    @BindView(R.id.source_file_name)
    EditText mSourceFileName;
    @BindView(R.id.source_file_descript)
    EditText mSourceFileDescription;
    @BindView(R.id.select_source)
    ImageView mSelectSource;
    @BindView(R.id.file_lujing)
    TextView mFileLujing;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.update_file)
    Button mUpdateFile;
    @BindView(R.id.source_type)
    ImageView mSourceType;
    @BindView(R.id.file_updata_bar)
    Toolbar mFileUpdataBar;
    private List<String> mStringList = null;

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        setSupportActionBar(mFileUpdataBar);
        mFileUpdataBar.setNavigationIcon(R.drawable.back);
        mFileUpdataBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                mActivity.finish();
            }
        });

    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_teacher_source_file;
    }

    @OnClick({R.id.select_source, R.id.update_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_source:
                new LFilePicker()
                        .withActivity(this)
                        .withRequestCode(100)
                        .start();
                break;
            case R.id.update_file:
                if (!InternetUtils.isUserfulNet(this)){
                    snackBar(findViewById(R.id.update_file), "请检查网络设置", 0);
                }else {
                    snackBar(findViewById(R.id.update_file), "开始上传", 0);
                    if (mStringList != null && mStringList.size() > 0) {
                        for (final  String s : mStringList) {
                            try {
                                final AVFile file = AVFile.withAbsoluteLocalPath(FileUtils.getFileName(s), s);
                                file.saveInBackground(new SaveCallback() {
                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void done(AVException mE) {
                                        if (mE == null) {
                                            snackBar(findViewById(R.id.update_file), "文件上传成功", 1);
                                            AVObject mObject = new AVObject("FileInfo");
                                            mObject.put("fileId",AVObject.createWithoutData("_File",file.getObjectId()));
                                            mObject.put("size",(file.getSize())/(1024*1024));
                                            mObject.put("title",mSourceFileName.getText().toString());
                                            mObject.put("user", AVUser.getCurrentUser().get("username"));
                                            mObject.put("downnum",0);
                                            mObject.put("school",mAVUserFinal.get("school"));
                                            mObject.put("description",mSourceFileDescription.getText().toString());
                                            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
                                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                                            String str = formatter.format(curDate);
                                            mObject.put("time",str);
                                            Log.i(TAG, "done: ");
                                            String m[] = new String[2];
                                            m= FileUtils.getFileName(s).split("\\.");
                                            Log.i(TAG, "done: "+m.length+file.getName());
                                            mObject.put("type",m[1]);
                                            mSourceFileName.setText("");
                                            mSourceFileDescription.setText("");
                                            mSourceType.setVisibility(View.GONE);
                                            mFileLujing.setText("");
                                            mProgressBar.setProgress(0);
                                            mObject.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(AVException mE) {
                                                    if ( mE == null){
                                                        Log.i(TAG, "done: 文件上传成功");

                                                    }else {
                                                        Log.e(TAG, "done: "+mE.toString() );
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }, new ProgressCallback() {
                                    @Override
                                    public void done(Integer mInteger) {
                                        mProgressBar.setProgress(mInteger);
                                    }
                                });


                            } catch (FileNotFoundException mE) {
                                mE.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                mStringList = data.getStringArrayListExtra("paths");
                String show = "";
                for (int i = 0; i < mStringList.size(); i++) {
                    show += FileUtils.getFileName(mStringList.get(i) + "\n");
                    if (FileUtils.getFileName(mStringList.get(i)).endsWith(".txt")) {
                        Log.i(TAG, "onViewClicked: txt");
                        mSourceType.setVisibility(View.VISIBLE);
                        mSourceType.setImageResource(R.drawable.txt_icon);
                    } else {
                        mSourceType.setVisibility(View.VISIBLE);
                        mSourceType.setImageResource(R.drawable.video_icon);
                    }
                }
                mFileLujing.setText(show);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

