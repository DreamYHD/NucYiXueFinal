package androidlab.edu.cn.nucyixue.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by dreamY on 2017/8/21.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private int layoutId;
    private Context mContext;
    private List<T> mTList;
    private OnClickerListener mOnClickerListener;
    public interface OnClickerListener{
        void click(View mView, int position);
    }

    public List<T> getTList() {
        return mTList;
    }

    public void setTList(List<T> mTList) {
        this.mTList = mTList;

    }
    public void setOnClickerListener(OnClickerListener mOnClickerListener){
        this.mOnClickerListener=mOnClickerListener;

    }
    public BaseRecyclerAdapter(int mLayoutId, Context mContext, List<T> mTList) {
        layoutId = mLayoutId;
        this.mContext = mContext;
        this.mTList = mTList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseViewHolder.getHolder(mContext,parent,layoutId);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        onBind(holder,mTList.get(position),position);
        if (mOnClickerListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View mView) {
                    mOnClickerListener.click(holder.itemView,holder.getLayoutPosition());
                }
            });
        }
    }
    protected abstract void onBind(BaseViewHolder mHolder, T mT, int mPosition);

    @Override
    public int getItemCount() {
        return mTList==null?0:mTList.size();
    }
}