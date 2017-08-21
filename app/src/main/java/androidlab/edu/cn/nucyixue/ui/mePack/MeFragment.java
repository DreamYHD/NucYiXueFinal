package androidlab.edu.cn.nucyixue.ui.mePack;


import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseFragment;
import androidlab.edu.cn.nucyixue.data.bean.UserInfo;
import androidlab.edu.cn.nucyixue.utils.FileUtils;
import androidlab.edu.cn.nucyixue.utils.config.Config;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment {

    private static final String TAG = "MeFragment";

    @BindView(R.id.toolbar_me)
    Toolbar mToolbarMe;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.me_name)
    TextView mMeName;
    @BindView(R.id.me_major)
    TextView mMeMajor;
    @BindView(R.id.me_more)
    ImageView mMeMore;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.my_image_collection)
    ImageView mMyImageCollection;
    @BindView(R.id.my_num_collection)
    TextView mMyNumCollection;
    @BindView(R.id.my_image_love)
    ImageView mMyImageLove;
    @BindView(R.id.my_num_love)
    TextView mMyNumLove;
    @BindView(R.id.my_image_message)
    ImageView mMyImageMessage;
    @BindView(R.id.my_num_message)
    TextView mMyNumMessage;
    @BindView(R.id.my_image_back)
    ImageView mMyImageBack;
    @BindView(R.id.my_image_setting)
    ImageView mMyImageSetting;

    @BindView(R.id.me_image_avatar)
    CircleImageView avatar;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.rl_logout)
    RelativeLayout rlLogout;

    public static MeFragment getInstance() {
        return new MeFragment();
    }

    @Override
    protected void init() {
    }

    @OnClick(R.id.me_image_avatar)
    public void selectImage() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .maxSelectable(1)
                .imageEngine(new GlideEngine())
                .forResult(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri uri = Matisse.obtainResult(data).get(0);
            if (uri != null) {
                Glide.with(getActivity())
                        .load(uri)
                        .into(avatar);

                String path = FileUtils.getFilePahtFromUri(getContext(), uri);
                if (path != null) {
                    try {
                        final AVFile file = AVFile.withAbsoluteLocalPath(FileUtils.getFileName(path), path);
                        file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    mAVUserFinal.put(Config.getUSER_AVATAR(), file.getUrl());
                                    mAVUserFinal.saveInBackground();

                                    AVQuery<UserInfo> query = new AVQuery<>(Config.getUI_TABLE());
                                    query.whereEqualTo(Config.getUI_USER_ID(), AVObject.createWithoutData(Config.getUSER_TABLE(), mAVUserFinal.getObjectId()));
                                    query.whereEqualTo(Config.getUI_USER_NAME(), mAVUserFinal.getUsername());
                                    query.whereEqualTo(Config.getUI_AVATAR(), file.getUrl());
                                    query.findInBackground(new FindCallback<UserInfo>() {
                                        @Override
                                        public void done(List<UserInfo> list, AVException e) {
                                            if (e == null) {
                                                UserInfo info = list.get(0);
                                                info.setAvatar(file.getUrl());
                                                info.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(AVException e) {
                                                        if (e != null) {
                                                            Snackbar.make(avatar, "更新头像成功", Snackbar.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Log.i(TAG, "查询 UserInfo 失败");
                                            }
                                        }
                                    });
                                } else {
                                    Snackbar.make(avatar, "上传头像失败", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_me;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAVUserFinal != null) {
            mMeName.setText(mAVUserFinal.getUsername());
            mMeMajor.setText(mAVUserFinal.get("school").toString() + " " + mAVUserFinal.get("major"));
            String url = (String) mAVUserFinal.get(Config.getUSER_AVATAR());
            if (url != null) {
                Glide.with(getActivity())
                        .load(url)
                        .into(avatar);
            }
        }
    }

    @Override
    protected void logic() {
        //进入注册界面
        RxView.clicks(mMeMore)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object mO) throws Exception {
                        Intent mIntent = new Intent(getContext(), LoginActivity.class);
                        startActivity(mIntent);
                    }
                });
    }

    @OnClick(R.id.rl_logout)
    public void onViewClicked() {
        AVUser.logOut();
        mMeName.setText(getResources().getText(R.string.username_hint));
        mMeMajor.setText(getResources().getText(R.string.me_major_hint));
        Glide.with(getContext())
                .load(R.drawable.hold)
                .into(avatar);
    }
}
