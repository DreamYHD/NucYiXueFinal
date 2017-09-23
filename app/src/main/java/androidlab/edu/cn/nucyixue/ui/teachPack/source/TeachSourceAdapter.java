package androidlab.edu.cn.nucyixue.ui.teachPack.source;

import android.content.Context;

import com.avos.avoscloud.AVObject;

import java.util.List;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseRecyclerAdapter;
import androidlab.edu.cn.nucyixue.base.BaseViewHolder;

/**
 * Created by dreamY on 2017/8/19.
 */
public class TeachSourceAdapter extends BaseRecyclerAdapter<AVObject> {
    public TeachSourceAdapter(int mLayoutId, Context mContext, List<AVObject> mSourceBeen) {
        super(mLayoutId, mContext, mSourceBeen);
    }
    @Override
    protected void onBind(BaseViewHolder mHolder, AVObject mAVObject, int mPosition) {
        mHolder.setText(R.id.source_item_title,mAVObject.get("title").toString());
        mHolder.setText(R.id.source_item_school,mAVObject.get("school").toString());
        mHolder.setText(R.id.source_item_size,mAVObject.get("size")+"M");
        mHolder.setText(R.id.source_item_type,mAVObject.get("type").toString());
        mHolder.setText(R.id.source_item_downnum,mAVObject.get("downnum").toString());
        String [] mStrings = mAVObject.get("time").toString().split("日");
        mHolder.setText(R.id.source_item_time,mStrings[0]+"日");

    }
}
