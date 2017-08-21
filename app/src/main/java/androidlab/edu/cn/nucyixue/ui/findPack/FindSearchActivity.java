package androidlab.edu.cn.nucyixue.ui.findPack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseActivity;
import androidlab.edu.cn.nucyixue.data.bean.Subject;
import androidlab.edu.cn.nucyixue.ui.findPack.subject.SubjectContentActivity;
import androidlab.edu.cn.nucyixue.utils.FlexTextUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindSearchActivity extends BaseActivity {


    private static final String TAG = "FindSearchActivity";
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.camera_search)
    ImageView mCameraSearch;
    @BindView(R.id.flexsubject_search)
    FlexboxLayout mFlexsubjectSearch;

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {

        String[] tags = {"Java程序设计", "计算机网络", "英语", "高等数学", "线性代数", "离散数学", "大学计算机基础"};
        for (int i = 0; i < tags.length; i++) {
            Subject model = new Subject();
            model.setId(i);
            model.setName(tags[i]);
            mFlexsubjectSearch.addView(createNewFlexItemTextView(model));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_search;
    }



    @OnClick(R.id.camera_search)
    public void onViewClicked() {
        Intent mIntent = new Intent();

    }


    private TextView createNewFlexItemTextView(final Subject book) {
        TextView textView = new TextView(this);
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
                mBundle.putString("subjectName",book.getName());
                mBundle.putInt("subjectId",book.getId());
                Intent mIntent = new Intent(FindSearchActivity.this, SubjectContentActivity.class);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
            }
        });
        int padding = FlexTextUtil.dpToPixel(this, 3);
        int paddingLeftAndRight = FlexTextUtil.dpToPixel(this, 4);
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight + 4, padding, paddingLeftAndRight + 4, padding);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = FlexTextUtil.dpToPixel(this, 4);
        int marginTop = FlexTextUtil.dpToPixel(this, 8);
        layoutParams.setMargins(margin + 10, marginTop, margin, 0);
        textView.setLayoutParams(layoutParams);
        return textView;
    }
}
