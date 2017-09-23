package androidlab.edu.cn.nucyixue.ui.teachPack.live

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidlab.edu.cn.nucyixue.R
import androidlab.edu.cn.nucyixue.base.AnimCommonAdapter
import androidlab.edu.cn.nucyixue.data.bean.LU
import androidlab.edu.cn.nucyixue.data.bean.Live
import androidlab.edu.cn.nucyixue.ui.imPack.ConversationActivity
import androidlab.edu.cn.nucyixue.utils.config.LCConfig
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMConversationsQuery
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * 推荐 Live
 *
 * Created by MurphySL on 2017/8/29.
 */
class CommonLiveFragment : Fragment(){
    private val TAG : String = this.javaClass.simpleName

    private lateinit var live_list : RecyclerView
    private lateinit var toolbar_title : TextView
    private val suggest_list : ArrayList<Live> = ArrayList()
    private lateinit var adapter : AnimCommonAdapter<Live>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_live, container, false)

        initView(rootView)

        initData()

        return rootView
    }

    private fun initView(rootView : View?) {
        rootView?.let {
            live_list = rootView.findViewById(R.id.live_list)
            toolbar_title = rootView.findViewById(R.id.toolbar_title)
            toolbar_title.text = "推荐"

            live_list.layoutManager = LinearLayoutManager(context)
            adapter = object : AnimCommonAdapter<Live>(context, R.layout.item_live, suggest_list){
                override fun convert(holder: ViewHolder?, t: Live?, position: Int) {
                    t?.let {
                        holder?.setText(R.id.live_name, it.name)
                        holder?.setText(R.id.live_type, it.type)
                        holder?.setRating(R.id.live_star, it.star.toFloat())
                        holder?.setText(R.id.live_join_num, it.num.toString())
                        holder?.setText(R.id.live_speaker, it.username)
                        holder?.setImageWithPicasso(R.id.live_pic, it.pic)

                        holder?.setOnClickListener(R.id.live_info) {
                            // 待进行后台认证
                            // 付费

                            AVUser.getCurrentUser()?.let {
                                uploadInfo(t)
                            }?: toast("Please Login First").show()
                        }
                    }?: toast("Unknown Error").show()
                }
            }

            live_list.adapter = adapter
        }
    }

    private fun initData() {
        val keys : ArrayList<String> = arguments.get("keys") as ArrayList<String>

        for(key in keys){
            val query : AVQuery<Live> = AVQuery(LCConfig.LIVE_TABLE)
            query.whereContains(LCConfig.LIVE_KEYWORD, key)
            query.findInBackground(object : FindCallback<Live>(){
                override fun done(p0: MutableList<Live>?, p1: AVException?) {
                    if(p1 != null){
                        toast("Query Live Fail").show()
                        Log.i(TAG, "Query Live Fail : $p1")
                    }else{
                        p0?.let {
                            it.forEach {
                                if(!suggest_list.contains(it)){
                                    suggest_list += it
                                }
                            }
                            adapter.notifyDataSetChanged()
                        }?:Log.i(TAG, "Live is null")
                    }
                }

            })
        }

    }

    private fun uploadInfo(live : Live){
        // 查询是否已购买
        val query = AVQuery<LU>("LU")
        query.whereEqualTo(LCConfig.LU_USER_ID, AVObject.createWithoutData(LCConfig.USER_TABLE, AVUser.getCurrentUser().objectId))
        query.whereEqualTo(LCConfig.LU_LIVE_ID, AVObject.createWithoutData(LCConfig.LIVE_TABLE, live.objectId))
        query.findInBackground(object : FindCallback<LU>(){
            override fun done(p0: MutableList<LU>?, p1: AVException?) {
                if(p1 == null){
                    p0?.let {
                        if(p0.isEmpty()){ // 新购买
                            val lu = LU()
                            lu.liveId = live.objectId
                            lu.userId = AVUser.getCurrentUser().objectId
                            lu.saveInBackground(object : SaveCallback(){
                                override fun done(p0: AVException?) {
                                    p0?.let {
                                        toast("Unknown Error")
                                        Log.i(TAG, "Create LU Fail：$p0")
                                    }?:enterLive(live)
                                }
                            })
                        }else{ // 已购买
                            val lu = it[0]
                            if(lu.comment == null && lu.star == 0){ // 未评价
                                enterComment(live, p0[0])
                            }else{
                                enterLive(live)
                            }
                        }
                    }
                }else{
                    toast("Unknown Error")
                    Log.i(TAG, "Query LU Fail: $p1")
                }
            }
        })
    }

    private fun enterComment(live: Live, lu : LU){
        val intent = Intent(context, LiveCommentActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(LCConfig.LU_TABLE, lu)
        bundle.putParcelable(LCConfig.LIVE_TABLE, live)
        intent.putExtra(LCConfig.LIVE_TABLE, bundle)
        startActivity(intent)
    }

    private fun  enterLive(live : Live) {
        val conversationQuery : AVIMConversationsQuery = AVIMClient.getInstance(AVUser.getCurrentUser().objectId).conversationsQuery
        conversationQuery.let {
            it.whereEqualTo("objectId", live.conversationId)
            conversationQuery.findInBackground(object : AVIMConversationQueryCallback(){
                override fun done(p0: MutableList<AVIMConversation>?, p1: AVIMException?) {
                    if(p1 != null){
                        toast("Query Conversation Fail").show()
                        Log.i(TAG, "Query Conversation Fail: $p1")
                        return
                    }

                    p0?.let {
                        val conversation = p0[0]
                        val member = ArrayList<String>()
                        member.addAll(conversation.members)
                        member += AVUser.getCurrentUser().objectId
                        conversation.addMembers(member, object : AVIMConversationCallback(){
                            override fun done(p0: AVIMException?) {
                                if(p0 == null){
                                    Log.i(TAG, "Get Conversation Success")
                                    val intent = Intent(context, ConversationActivity::class.java)
                                    intent.putExtra(LCConfig.LIVE_TABLE, live)
                                    intent.putExtra(LCIMConstants.CONVERSATION_ID, live.conversationId)
                                    startActivity(intent)
                                }else{
                                    toast("Enter Conversation Fail").show()
                                    Log.i(TAG, "Enter Conversation Fail: $p0")
                                }
                            }
                        })
                    }?: toast("未知的错误").show()
                }
            })
        }
    }

    private fun toast(msg : String) : Snackbar = Snackbar.make(live_list, msg, Snackbar.LENGTH_LONG)

    fun ViewHolder.setImageWithPicasso(viewId : Int, url : String) : ViewHolder {
        val view : ImageView = getView(viewId)
        com.squareup.picasso.Picasso.with(context)
                .load(url)
                .into(view)
        return this
    }

    override fun onStop() {
        super.onStop()
        suggest_list.clear()
    }

}