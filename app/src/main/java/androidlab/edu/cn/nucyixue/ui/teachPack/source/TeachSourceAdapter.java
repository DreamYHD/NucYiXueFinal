package androidlab.edu.cn.nucyixue.ui.teachPack.source;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.RecyclerOnClickListener;
import butterknife.ButterKnife;

/**
 * Created by dreamY on 2017/8/19.
 */

public class TeachSourceAdapter extends RecyclerView.Adapter<TeachSourceAdapter.ViewHolder> {
    private Context mContext;
    private  RecyclerOnClickListener mRecyclerOnClickListener;

    public TeachSourceAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setOnItemClickListener(RecyclerOnClickListener mOnItemClickListener){
        this.mRecyclerOnClickListener = mOnItemClickListener;
    }
    @Override
    public TeachSourceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_teach_source_item,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final TeachSourceAdapter.ViewHolder holder, int position) {
        if (mRecyclerOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View mView) {
                    mRecyclerOnClickListener.click(holder.itemView,holder.getLayoutPosition());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
