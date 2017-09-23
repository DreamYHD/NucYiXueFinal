package androidlab.edu.cn.nucyixue.ui.findPack;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseFragment;
import androidlab.edu.cn.nucyixue.data.bean.Book;
import androidlab.edu.cn.nucyixue.data.bean.Live;
import androidlab.edu.cn.nucyixue.data.bean.Subject;
import androidlab.edu.cn.nucyixue.ocr.FileUtil;
import androidlab.edu.cn.nucyixue.ui.findPack.subject.SubjectContentActivity;
import androidlab.edu.cn.nucyixue.ui.findPack.zxing.MipcaActivityCapture;
import androidlab.edu.cn.nucyixue.utils.FlexTextUtil;
import androidlab.edu.cn.nucyixue.utils.config.LCConfig;
import androidlab.edu.cn.nucyixue.utils.config.LiveType;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends BaseFragment {
    private static final String TAG = "FindFragment";

    @BindView(R.id.banner_guide_content)
    BGABanner mBannerGuideContent;
    @BindView(R.id.flexsubject)
    FlexboxLayout mFlexSubject;
    @BindView(R.id.find_live_recycler)
    RecyclerView mFindLiveRecycler;
    @BindView(R.id.find_search_by_text)
    LinearLayout mFindSearchByText;
    @BindView(R.id.type_recycler)
    RecyclerView typeRecycler;
    @BindView(R.id.find_search_by_erwerma)
    ImageView findSearchByErwerma;
    @BindView(R.id.image_left1)
    ImageView imageLeft1;
    @BindView(R.id.image_left2)
    ImageView imageLeft2;

    private static final int DISPLAY_NUM = 7;
    private static final int SCANNIN_GREQUEST_CODE = 1;
    private static final int REQUEST_CODE_GENERAL = 105;

    private boolean hasGotToken = false;


    public static FindFragment getInstance() {
        return new FindFragment();
    }

    @Override
    protected void init(View mView, Bundle mSavedInstanceState) {
        mBannerGuideContent.setAdapter(new BGABanner.Adapter<ImageView, Integer>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, Integer model, int position) {
                Glide.with(getContext())
                        .load(model)
                        .placeholder(R.drawable.hold)
                        .error(R.drawable.hold)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }

        });

        List<LiveType> types = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            types.add(LiveType.toList().get(i));
        }

        CommonAdapter<LiveType> adapter = new CommonAdapter<LiveType>(getContext(), R.layout.item_type, types) {
            @Override
            protected void convert(ViewHolder holder, LiveType liveType, int position) {
                holder.setImageResource(R.id.type_icon, liveType.getIcon());
                holder.setText(R.id.type_name, liveType.getValue());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        };
        typeRecycler.setAdapter(adapter);
        typeRecycler.setLayoutManager(new GridLayoutManager(getContext(), 5));

        initAccessTokenWithAkSk();
    }

    @OnClick(R.id.find_search_by_text)
    public void search() {
        Intent mIntent = new Intent(getContext(), FindSearchActivity.class);
        startActivity(mIntent);
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_find;
    }

    @Override
    protected void logic() {
        displayBanner();
        AVQuery<Live> query = new AVQuery(LCConfig.getLIVE_TABLE());
        query.selectKeys(Arrays.asList(LCConfig.getLIVE_KEYWORD()));
        query.findInBackground(new FindCallback<Live>() {
            @Override
            public void done(List<Live> list, AVException e) {
                if (e != null) {
                    Log.i(TAG, "获取标签为空:" + e);
                } else {
                    if (!list.isEmpty()) {
                        HashMap<String, Integer> map = new HashMap<>();
                        for (Live live : list) {
                            if (live.getKeyword() == null)
                                continue;
                            Log.i(TAG, live.getKeyword().get(0));
                            for (String keyword : live.getKeyword()) {
                                if (map.containsKey(keyword)) {
                                    int times = map.get(keyword);
                                    map.put(keyword, times + 1);
                                } else {
                                    map.put(keyword, 1);
                                }
                            }
                        }

                        List<Map.Entry<String, Integer>> l = new ArrayList<>(map.entrySet());
                        Collections.sort(l, new Comparator<Map.Entry<String, Integer>>() {
                            @Override
                            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> t1) {
                                return o1.getValue().compareTo(t1.getValue()) > 0 ? -1 : 1;
                            }
                        });

                        for (Map.Entry<String, Integer> mapping : l) {
                            System.out.println(mapping.getKey() + ":" + mapping.getValue());
                        }

                        displayFlexSubject(l);
                    } else {
                        Log.i(TAG, "获取标签为空");
                    }
                }
            }
        });


    }

    private void displayBanner() {
        mBannerGuideContent.setData(Arrays.asList(R.drawable.live, R.drawable.xuanshang, R.drawable.xianxia), Arrays.asList("", "", ""));
        mBannerGuideContent.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {

            }
        });
    }

    private void displayFlexSubject(List<Map.Entry<String, Integer>> mapping) {
        //String[] tags = {"Java程序设计", "计算机网络", "英语", "高等数学", "线性代数", "离散数学", "大学计算机基础"};
        for (int i = 0; i < DISPLAY_NUM; i++) {
            Subject model = new Subject();
            model.setId(i);
            model.setName(mapping.get(i).getKey());
            mFlexSubject.addView(createNewFlexItemTextView(model));
        }
    }

    private TextView createNewFlexItemTextView(final Subject book) {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText(book.getName());
        textView.setTextSize(15);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        textView.setBackgroundResource(R.drawable.shape_back);
        textView.setTag(book.getId());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, book.getName());
                Bundle mBundle = new Bundle();
                mBundle.putString("subjectName", book.getName());
                mBundle.putInt("subjectId", book.getId());
                Intent mIntent = new Intent(getContext(), SubjectContentActivity.class);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
            }
        });
        int padding = FlexTextUtil.dpToPixel(getContext(), 3);
        int paddingLeftAndRight = FlexTextUtil.dpToPixel(getContext(), 4);
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight + 4, padding, paddingLeftAndRight + 4, padding);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = FlexTextUtil.dpToPixel(getContext(), 4);
        int marginTop = FlexTextUtil.dpToPixel(getContext(), 8);
        layoutParams.setMargins(margin + 10, marginTop, margin, 0);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    @OnClick(R.id.find_search_by_erwerma)
    public void onViewClicked() {
        getActivity().startActivityForResult(new Intent(getActivity(), MipcaActivityCapture.class), SCANNIN_GREQUEST_CODE);
    }


    private void searchKeyword(Book book) {
        Log.i(TAG, "book : " + book.toString());
        List<Book.Tags> tags = book.getTags();
        List<String> names = new ArrayList<>();
        for (Book.Tags tag : tags) {
            names.add(tag.getName());
            Log.i(TAG, "tag:" + tag.getName());
        }
    }

    @OnClick(R.id.find_select_by_tiaojian)
    public void onViewClickedOCR() {
        if(!checkTokenStatus()){
            return;
        }

        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getContext()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
        getActivity().startActivityForResult(intent, REQUEST_CODE_GENERAL);
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Log.i(TAG, "AK，SK方式获取token失败"+error.getMessage());
            }
        }, getContext(), "AL2QSX22moztT8ir6GsW0cc6", "agAiIP7f4ydgSkpGa92fycEGSe742TG0");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, requestCode + " " + resultCode);
    }
}


