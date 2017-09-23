package androidlab.edu.cn.nucyixue.ui.common

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidlab.edu.cn.nucyixue.R
import androidlab.edu.cn.nucyixue.base.AnimCommonAdapter
import androidlab.edu.cn.nucyixue.data.bean.LU
import androidlab.edu.cn.nucyixue.data.bean.Live
import androidlab.edu.cn.nucyixue.net.AVService
import androidlab.edu.cn.nucyixue.ui.imPack.ConversationActivity
import androidlab.edu.cn.nucyixue.ui.teachPack.live.CreateLiveActivity
import androidlab.edu.cn.nucyixue.ui.teachPack.live.LiveCommentActivity
import androidlab.edu.cn.nucyixue.ui.teachPack.live.PlayActivity
import androidlab.edu.cn.nucyixue.utils.config.LCConfig
import androidlab.edu.cn.nucyixue.utils.config.LiveFragmentType
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMConversationsQuery
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.fragment_live.*

/**
 * LiveFragment
 *
 * 待修改：
 * 工厂模式
 *
 * Created by MurphySL on 2017/9/23.
 */
class LiveFragment : Fragment(){
    private val TAG = this.javaClass.simpleName

    private var type : String? = null
    private val list : MutableList<Live> = ArrayList()
    private lateinit var adapter : AnimCommonAdapter<Live>
    private var user : AVUser? = null

    companion object {
        @JvmStatic
        val instance : LiveFragment by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LiveFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments.get(LiveFragmentType.LIVE_FRAGMENT_TYPE) as String?
        Log.i(TAG, "type:$type")
        user = AVUser.getCurrentUser()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_live, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        fetchData()
    }

    override fun onStart() {
        super.onStart()
        list.clear()
        fetchData()
    }

    private fun initView() {
        initRecyclerView()

        type?.let {
            when(type){
                LiveFragmentType.JOINED ->{ // 参加
                    initToolbar(getString(R.string.joined_title))
                }
                LiveFragmentType.RECOMMEND -> { // 推荐
                    add_live.visibility = View.VISIBLE
                    add_live.setOnClickListener {
                        LCChatKit.getInstance().client?.let {
                            startActivity(Intent(context, CreateLiveActivity::class.java))
                        }?:Snackbar.make(add_live, getString(R.string.warn_login), Snackbar.LENGTH_LONG).show()
                    }
                    bar_layout.visibility = View.GONE
                }
                LiveFragmentType.CREATED ->{ // 主讲
                    initToolbar(getString(R.string.created_title))
                }
                else ->{} // 其他
            }
        }
    }

    private fun initRecyclerView(){
        live_list.layoutManager = LinearLayoutManager(context)
        adapter = object : AnimCommonAdapter<Live>(context, R.layout.item_live, list){
            override fun convert(holder: ViewHolder?, t: Live?, position: Int) {
                t?.let {
                    holder?.setText(R.id.live_name, it.name)
                    holder?.setText(R.id.live_type, it.type)
                    holder?.setRating(R.id.live_star, it.star.toFloat())
                    holder?.setText(R.id.live_join_num, it.num.toString())
                    holder?.setText(R.id.live_speaker, it.username)
                    holder?.setImageWithPicasso(R.id.live_pic, it.pic)

                    holder?.setOnClickListener(R.id.live_info) {
                        user?.let {
                            AVService.queryJoinedByLUId(t.objectId, it.objectId).subscribe( // 是否已参加
                                    {
                                        lu ->
                                        Log.i(TAG, lu.toString())
                                        if(lu.comment == null && lu.star == 0){ // 未评价
                                            enterComment(t, lu)
                                        }else{
                                            if(t.isText == LCConfig.LIVE_TEXT) enterLive(t) else enterVideo(t)
                                        }
                                    },
                                    {  //购买页面
                                        onError ->
                                        Log.i(TAG, onError.toString())
                                        val intent = Intent(activity, LiveDetailActivity::class.java)
                                        val bundle = Bundle()
                                        bundle.putParcelable(LCConfig.LIVE_TABLE, t)
                                        intent.putExtras(bundle)
                                        startActivity(intent)
                                    })
                        }?:toast(getString(R.string.warn_login))
                    }
                }?: toast("Unknown Error").show()
            }
        }
        live_list.adapter = adapter
    }

    private fun initToolbar(title : String){
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true) // 设置 Fragment 标题
        toolbar.title = title
    }

    private fun fetchData() {
        type?.let {
            when(type){
                LiveFragmentType.JOINED -> fetchJoinedLive()

                LiveFragmentType.RECOMMEND -> fetchRecommendLive()

                LiveFragmentType.CREATED -> fetchCreatedLive()

                else ->{} // 其他
            }
        }
    }

    /**
     * 获取已参加 Live
     */
    private fun fetchJoinedLive(){
        user?.let {
            AVService.queryJoinedByUId(it.objectId).subscribe(
                    {
                        onNext ->
                        list += onNext
                    },
                    {
                        onError ->
                        Log.i(TAG, "onError : $onError")
                        toast("获取 Live 失败！")
                    },
                    {
                        Log.i(TAG, "onComplete")
                    }
            )
        }
    }

    /**
     * 获取推荐 Live
     */
    private fun fetchRecommendLive(){
        AVService.queryAllLive().subscribe(
                {
                    onNext ->
                    list += onNext
                    adapter.notifyDataSetChanged()
                },
                {
                    onError ->
                    Log.i(TAG, "onError : $onError")
                    toast("获取 Live 失败！")
                },
                {
                    Log.i(TAG, "onComplete")
                }
        )
    }

    /**
     * 获取主讲 Live
     */
    private fun fetchCreatedLive(){
        user?.let {
            AVService.queryCreatedLive(it.objectId).subscribe(
                    {
                        onNext ->
                        list += onNext
                        adapter.notifyDataSetChanged()
                    },
                    {
                        onError ->
                        Log.i(TAG, "onError : $onError")
                        toast("获取 Live 失败！")
                    },
                    {
                        Log.i(TAG, "onComplete")
                    }
            )
        }
    }

    //评论页面
    private fun enterComment(live: Live, lu : LU){
        val intent = Intent(context, LiveCommentActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(LCConfig.LU_TABLE, lu)
        bundle.putParcelable(LCConfig.LIVE_TABLE, live)
        intent.putExtra(LCConfig.LIVE_TABLE, bundle)
        startActivity(intent)
    }

    //文字 Live 页面
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

    //直播 Live 页面
    private fun enterVideo(live: Live) {
        val intent = Intent(activity, PlayActivity::class.java)
        intent.putExtra(LCConfig.LIVE_TABLE, live)
        startActivity(intent)
    }

    private fun toast(msg : String) : Snackbar = Snackbar.make(live_list, msg, Snackbar.LENGTH_LONG)

    fun ViewHolder.setImageWithPicasso(viewId : Int, url : String) : ViewHolder{
        val view : ImageView = getView(viewId)
        com.squareup.picasso.Picasso.with(context)
                .load(url)
                .into(view)
        return this
    }


}