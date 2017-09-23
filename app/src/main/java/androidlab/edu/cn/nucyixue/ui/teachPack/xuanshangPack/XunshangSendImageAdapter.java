package androidlab.edu.cn.nucyixue.ui.teachPack.xuanshangPack;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;

import java.util.List;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.BaseRecyclerAdapter;
import androidlab.edu.cn.nucyixue.base.BaseViewHolder;

/**
 * Created by dreamY on 2017/8/31.
 */

public class XunshangSendImageAdapter extends BaseRecyclerAdapter<String> {

    private static final String TAG = "XunshangSendImageAdapte";
    private Context mContext;
    public XunshangSendImageAdapter(int mLayoutId, Context mContext, List<String> mStrings) {
        super(mLayoutId, mContext, mStrings);
        this.mContext = mContext;
    }
    @Override
    protected void onBind(BaseViewHolder mHolder, String mS, int mPosition) {
        Log.i(TAG, "onBind: "+mS);
        Glide.with(mContext)
                .load(mS)
                .into((ImageView) mHolder.getView(R.id.xuanshang_send_image_item));
    }
}
