package androidlab.edu.cn.nucyixue.ui.mePack;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseFragment;
import androidlab.edu.cn.nucyixue.ui.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment {


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

    @BindView(R.id.me_iamge_touxiang)
    ImageView mMeIamgeTouxiang;

    public static MeFragment getInstance() {
        return new MeFragment();
        // Required empty public constructor
    }


    @Override
    protected void init() {

    }
    @OnClick(R.id.me_iamge_touxiang)
    public void selectImage(){
        Matisse.from(this)
                .choose(MimeType.allOf())
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .maxSelectable(9)
                .imageEngine(new GlideEngine())
                .forResult(0);
    }

    List<Uri>mList;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            mList = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mList);
        }
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_me;
    }

    @Override
    protected void logic() {
        RxView.clicks(mMeMore)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object mO) throws Exception {
                        Intent mIntent = new Intent(getContext(),LoginActivity.class);
                        startActivity(mIntent);
                    }
                });
    }


}
