package androidlab.edu.cn.nucyixue.ui.findPack;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidlab.edu.cn.nucyixue.R;
import androidlab.edu.cn.nucyixue.base.RecyclerOnClickListener;
import androidlab.edu.cn.nucyixue.data.bean.LiveBean;
import butterknife.ButterKnife;

/**
 * Created by dreamY on 2017/7/27.
 */

public class FindLiveAdapter extends RecyclerView.Adapter<FindLiveAdapter.MyHolder> {

    private Context mContext;
    private List<LiveBean>mLiveBeen = new ArrayList<>();
    private RecyclerOnClickListener mRecyclerOnClickListener;

    public FindLiveAdapter(Context mContext, List<LiveBean> mLiveBeen) {
        this.mContext = mContext;
        this.mLiveBeen = mLiveBeen;
    }
    public void setRecyclerOnClickListener(RecyclerOnClickListener mRecyclerOnClickListener){
        this.mRecyclerOnClickListener = mRecyclerOnClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_find_live_item,parent,false);
        return new MyHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        if (mRecyclerOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View mView) {
                    int position = holder.getLayoutPosition();
                    mRecyclerOnClickListener.click(mView,position);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return mLiveBeen.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
