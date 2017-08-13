package androidlab.edu.cn.nucyixue.ui.findPack;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;

import java.util.Arrays;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseFragment;
import androidlab.edu.cn.nucyixue.data.bean.Subject;
import androidlab.edu.cn.nucyixue.utils.FlexTextUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends BaseFragment {
    private static final String TAG = "FindFragment";


    @BindView(R.id.banner_guide_content)
    BGABanner mBannerGuideContent;
    @BindView(R.id.flexsubject)
    FlexboxLayout mFlexsubject;
    @BindView(R.id.find_live_recycler)
    RecyclerView mFindLiveRecycler;


    public static FindFragment getInstance() {
        // Required empty public constructor
        return new FindFragment();
    }

    @Override
    protected void init() {
        mBannerGuideContent.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(getContext())
                        .load(model)
                        .placeholder(R.drawable.hold)
                        .error(R.drawable.hold)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });


    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_find;
    }

    @Override
    protected void logic() {
        String[] tags = {"婚姻育儿", "散文", "设计", "影视天堂", "大学生活", "美人说", "生活家"};
        for (int i = 0; i < tags.length; i++) {
            Subject model = new Subject();
            model.setId(i);
            model.setName(tags[i]);
            mFlexsubject.addView(createNewFlexItemTextView(model));
        }
        mBannerGuideContent.setData(Arrays.asList("网络图片路径1", "网络图片路径2", "网络图片路径2", "网络图片路径3"), Arrays.asList("提示文字1", "提示文字2", "提示文字3"));
        mBannerGuideContent.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {

            }
        });


    }

    private TextView createNewFlexItemTextView(final Subject book) {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText(book.getName());
        textView.setTextSize(15);
        textView.setTextColor(getResources().getColor(R.color.colorAll));
        textView.setBackgroundResource(R.drawable.shape_back);
        textView.setTag(book.getId());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, book.getName());
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


}


