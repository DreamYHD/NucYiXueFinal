package androidlab.edu.cn.nucyixue.base

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.CardView
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.item_live.view.*

/**
 * Adapter with animation for item_live
 *
 * Created by MurphySL on 2017/7/27.
 */
abstract class AnimCommonAdapter<T>(val context : Context, layoutId : Int, val data : List<T>) : CommonAdapter<T>(context, layoutId, data){

    private var oldPosition = 0

    override fun onViewAttachedToWindow(holder: ViewHolder?) {
        if(holder?.layoutPosition !! > oldPosition){
            addItemAnimation(holder.itemView.live_info)
            oldPosition = holder.layoutPosition
        }
    }

    private fun addItemAnimation(card : CardView?){
        val translationY = ObjectAnimator.ofFloat(card, "translationY", 500f, 0f)
        translationY.duration = 500
        translationY.start()
    }
}