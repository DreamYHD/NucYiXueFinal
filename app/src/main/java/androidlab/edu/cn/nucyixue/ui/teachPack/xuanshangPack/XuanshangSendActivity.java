package androidlab.edu.cn.nucyixue.ui.teachPack.xuanshangPack;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseActivity;
import androidlab.edu.cn.nucyixue.base.BaseRecyclerAdapter;
import androidlab.edu.cn.nucyixue.ui.CameraActivity;
import androidlab.edu.cn.nucyixue.utils.FileUtils;
import androidlab.edu.cn.nucyixue.utils.FlexTextUtil;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class XuanshangSendActivity extends BaseActivity {

    private static final String TAG = "XuanshangSendActivity";
    @BindView(R.id.xuanshang_back_img)
    ImageView mXuanshangBackImg;
    @BindView(R.id.xuanshang_send_location_image)
    ImageView mXuanshangSendLocationImage;
    @BindView(R.id.xuanshang_send_btn)
    TextView mXuanshangSendBtn;
    @BindView(R.id.xuanshang_edit_show)
    EditText mXuanshangEditShow;
    @BindView(R.id.xuanshang_image_recycler)
    RecyclerView mXuanshangImageRecycler;
    @BindView(R.id.xuanshang_send_location_text)
    TextView mXuanshangSendLocationText;
    @BindView(R.id.xuanshang_send_money_edit)
    TextView mXuanshangSendMoneyEdit;
    @BindView(R.id.xuanshang_send_tags)
    EditText mXuanshangSendTags;
    @BindView(R.id.xuanshan_send_progressbar)
    ProgressBar mXuanshanSendProgressbar;
    @BindView(R.id.xuanhang_send_add_image)
    ImageView mXuanhangSendAddImage;
    @BindView(R.id.imageView2)
    ImageView mImageView2;
    private XunshangSendImageAdapter mXunshangSendImageAdapter;
    private GridLayoutManager mGridLayoutManager;
    public static final int CAMERA_CODE = 10;
    public static final int SELECT_CODE = 100;
    private static List<String> mStringList = new ArrayList<>();//图片的url
    private static List<String> mFileList = new ArrayList<>();//存到图片路径

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        mGridLayoutManager = new GridLayoutManager(this, 4);
        mXuanshangImageRecycler.setLayoutManager(mGridLayoutManager);
        mXunshangSendImageAdapter = new XunshangSendImageAdapter(R.layout.activity_xuanshang_send_image_item, this, mFileList);
        mXuanshangImageRecycler.setAdapter(mXunshangSendImageAdapter);
        mXunshangSendImageAdapter.setOnClickerListener(new BaseRecyclerAdapter.OnClickerListener() {
            @Override
            public void click(View mView, int position) {

            }
        });
    }

    //通过拍照获取题片
    private void onClickStartCamera(View mView) {
        RxPermissions mRxPermissions = new RxPermissions(this);
        mRxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean mBoolean) throws Exception {
                        if (mBoolean) {
                            Log.i(TAG, "accept: success get camera permission");
                            Intent mIntent = new Intent(XuanshangSendActivity.this, CameraActivity.class);
                            startActivityForResult(mIntent, CAMERA_CODE);
                        }
                    }
                });
    }

    /**
     * 通过图片选择器获取图片
     *
     * @param mView
     */
    private void onClickStartSelectImage(View mView) {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .maxSelectable(9)
                .imageEngine(new GlideEngine())
                .forResult(SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            String path = data.getStringExtra("path");
            Log.i(TAG, "onActivityResult: " + path);
            mFileList.add(path);

            mXunshangSendImageAdapter.notifyDataSetChanged();
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_CODE) {
                Log.i(TAG, "onActivityResult: select_code");
                List<Uri> mList = Matisse.obtainResult(data);
                for (Uri m :
                        mList) {
                    mFileList.add(m.toString());
                    Log.i(TAG, "onActivityResult: " + m.toString());
                    mXunshangSendImageAdapter.notifyDataSetChanged();
                }
            }

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_xuanshang_send;
    }

    @OnClick({R.id.xuanshang_back_img, R.id.xuanshang_send_location_image, R.id.xuanshang_send_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.xuanshang_back_img:
                mActivity.finish();
                break;
            case R.id.xuanshang_send_btn:
                xuanshangSendClick(view);
                break;
            case R.id.xuanshang_send_location_image:
                xuanshangGetLocation();
                break;
        }
    }

    /**
     * 通过高德地图获取位置信息
     */
    private void xuanshangGetLocation() {
        Log.i(TAG, "xuanshangGetLocation: click");
        AMapLocationClient aMapLocationClient = new AMapLocationClient(this);
        aMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        //amapLocation.getCountry();//国家信息
                        //amapLocation.getProvince();//省信息
                        //amapLocation.getCity();//城市信息
                        //amapLocation.getDistrict();//城区信息
                        //街道信息
                        mXuanshangSendLocationText.setText(amapLocation.getStreet());
                    }
                }
            }
        });
    }

    /**
     * 发送悬赏信息
     *
     * @param mView
     */
    private void xuanshangSendClick(final View mView) {
        mXuanshanSendProgressbar.setVisibility(View.VISIBLE);
        for (int j = 0; j < mFileList.size(); j++) {
            final AVFile file;
            try {
                String path = FileUtils.getFilePahtFromUri(this,Uri.parse(mFileList.get(j)));
                file = AVFile.withAbsoluteLocalPath(FileUtils.getFileName(path),path);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException mE) {
                        if (mE == null) {
                            mStringList.add(file.getUrl());
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
        for (int i = 0; i < mStringList.size(); i++) {
            Log.i(TAG, "xuanshangSendClick: " + mStringList.get(i));
        }
        AVObject mAVObject = new AVObject("Xuanshang");
        mAVObject.put("description", mXuanshangEditShow.getText());

        mAVObject.put("images", mStringList);
        final String[] mStrings;
        mStrings = mXuanshangSendTags.getText().toString().split("#");
        mAVObject.put("tags", mStrings);
        Log.i(TAG, "xuanshangSendClick: "+mStrings.length);
        //如果有图片的话把第一张图片作为展示的封面
        if ( mStringList.size() > 0 ){
            mAVObject.put("firstImage", mStringList.get(0));
        }
        mAVObject.put("user", mAVUserFinal.getObjectId());
        if (mXuanshangSendLocationText.getText() != null && !mXuanshangSendLocationText.getText().equals("")) {
            mAVObject.put("loaction", mXuanshangSendLocationText.getText().toString());
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        mAVObject.put("time", df.format(date));
        mAVObject.put("money", mXuanshangSendMoneyEdit.getText().toString());
        mAVObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException mE) {
                if (mE == null) {
                    snackBar(findViewById(R.id.xuanshang_send_btn), "悬赏发布成功", 0);
                    mActivity.finish();
                    mFileList.clear();
                    mStringList.clear();
                } else {
                    Log.e(TAG, "done: " + mE.getMessage());
                }

            }
        });

    }
    /**
     * 增加图片
     */
    @OnClick(R.id.xuanhang_send_add_image)
    public void onViewClicked() {
        //自定义底部弹出dialog
        final Dialog mDialog = new Dialog(XuanshangSendActivity.this, R.style.BottomDialog);
        final View mBottom = LayoutInflater.from(mActivity).inflate(R.layout.dialog_content_circle, null);

        TextView mTextViewCamera = mBottom.findViewById(R.id.xuanshang_sned_bottom_camera);
        TextView mTextViewSelect = mBottom.findViewById(R.id.xuanshang_send_bottom_select);
        mDialog.setContentView(mBottom);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mBottom.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - FlexTextUtil.dp2px(mActivity, 16f);
        params.bottomMargin = FlexTextUtil.dp2px(mActivity, 8f);
        mBottom.setLayoutParams(params);
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
        mDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        mDialog.show();
        mTextViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                onClickStartCamera(mView);
                mDialog.cancel();
            }
        });
        mTextViewSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                onClickStartSelectImage(mView);
                mDialog.cancel();
            }
        });
    }
}
