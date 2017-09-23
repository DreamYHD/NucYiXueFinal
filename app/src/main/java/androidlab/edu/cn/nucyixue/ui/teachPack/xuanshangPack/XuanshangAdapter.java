package androidlab.edu.cn.nucyixue.ui.teachPack.xuanshangPack;

import android.content.Context;

import com.avos.avoscloud.AVObject;

import java.util.List;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseRecyclerAdapter;
import androidlab.edu.cn.nucyixue.base.BaseViewHolder;

/**
 * Created by dreamY on 2017/9/2.
 */

public class XuanshangAdapter extends BaseRecyclerAdapter<AVObject>{
    public XuanshangAdapter(int mLayoutId, Context mContext, List<AVObject> mAVObjects) {
        super(mLayoutId, mContext, mAVObjects);
    }
    @Override
    protected void onBind(BaseViewHolder mHolder, AVObject mAVObject, int mPosition) {
        mHolder.setText(R.id.fragment_xuanshang_item_description,mAVObject.get("description").toString());
        mHolder.setImage(R.id.fragment_xuanshang_item_image,mAVObject.get("firstImage").toString());
        if (null != mAVObject.get("location")  && !mAVObject.get("location").equals("")){
            mHolder.setText(R.id.fragment_xuanshang_item_school,mAVObject.get("location").toString());
        }else {
            mHolder.setText(R.id.fragment_xuanshang_item_school,"来自火星");
        }
        mHolder.setText(R.id.fragment_xuanshang_item_time,mAVObject.get("time").toString());
        mHolder.setText(R.id.fragment_xuanshang_item_money,mAVObject.get("money").toString());
    }
}
