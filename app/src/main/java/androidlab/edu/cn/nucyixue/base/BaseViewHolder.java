package androidlab.edu.cn.nucyixue.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidlab.edu.cn.nucyixue.R;

/**
 * Created by dreamY on 2017/8/21.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    protected SparseArray<View> mViews;
    View itemView;
    public static Context mcontext;
    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView=itemView;
        mViews=new SparseArray<>();
    }

    public static <T extends BaseViewHolder>T getHolder(Context mContext, ViewGroup mParent, int mLayoutId) {

        mcontext = mContext;
        return (T) new BaseViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId,mParent,false));
    }
    public <T extends View>T getView(int viewId) {
        View childView = mViews.get(viewId);
        if (childView == null) {
            childView = itemView.findViewById(viewId);
            mViews.put(viewId, childView);
        }
        return (T) childView;

    }
    public void setText(int viewId,String text){
        View mView=getView(viewId);
        if (mView instanceof TextView) {
            ((TextView) mView).setText(text);
        }
    }
    public void setImage(int viewId,String Url){
        View mView = getView(viewId);
        if (mView instanceof ImageView){
            Glide.with(mcontext)
                    .load(Url)
                    .placeholder(R.drawable.hold)
                    .into((ImageView) mView);
        }
    }
}