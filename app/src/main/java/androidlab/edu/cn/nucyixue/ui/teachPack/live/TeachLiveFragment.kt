package androidlab.edu.cn.nucyixue.ui.teachPack.live

import android.app.ProgressDialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidlab.edu.cn.nucyixue.R
import androidlab.edu.cn.nucyixue.base.AnimCommonAdapter
import androidlab.edu.cn.nucyixue.data.bean.LU
import androidlab.edu.cn.nucyixue.data.bean.Live
import androidlab.edu.cn.nucyixue.ui.imPack.ConversationActivity
import androidlab.edu.cn.nucyixue.utils.config.LCConfig
import c.b.BP
import c.b.PListener
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
import kotlin.collections.ArrayList

/**
 * Lives order by star and start time
 *
 * Created by MurphySL on 2017/7/24.
 */
object TeachLiveFragment : Fragment(){
    private val TAG : String = this.javaClass.simpleName

    private lateinit var live_list : RecyclerView

    private lateinit var dialog : ProgressDialog

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_live_main, container, false)


        initView(rootView)

        initData()

        return rootView
    }

    private fun initView(rootView : View?) {
        dialog = ProgressDialog(activity)
        dialog.setCancelable(true)

        rootView?.let {

            val add_live = rootView.findViewById<FloatingActionButton>(R.id.add_live)
            add_live.setOnClickListener {
                LCChatKit.getInstance().client?.let {
                    startActivity(Intent(context, CreateLiveActivity::class.java))
                }?:Snackbar.make(add_live, "请先登录", Snackbar.LENGTH_LONG).show()
            }

            live_list = rootView.findViewById(R.id.live_list)
        }
    }

    //查询所有 Live
    private fun initData() {
        val query : AVQuery<Live> = AVQuery(LCConfig.LIVE_TABLE)
        query.addDescendingOrder(LCConfig.LIVE_STAR) // 按星级排序
        query.addDescendingOrder(LCConfig.LIVE_START_AT) // 按时间排序
        query.findInBackground(object : FindCallback<Live>(){
            override fun done(p0: MutableList<Live>?, p1: AVException?) {
                if(p1 != null){
                    toast("Query Live Fail").show()
                    Log.i(TAG, "Query Live Fail: $p1")
                }else{
                    p0?.let {
                        live_list.layoutManager = LinearLayoutManager(context)
                        live_list.adapter = object : AnimCommonAdapter<Live>(context, R.layout.item_live_new, it){
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
                                        pay(t)
                                    }
                                }?: toast("Unknown Error").show()
                            }
                        }
                    }?: Log.i(TAG, "Query Live Fail")
                }
            }
        })

    }

    private fun pay(t : Live){

        if (!checkPackageInstalled("com.eg.android.AlipayGphone", "https://www.alipay.com")) { // 支付宝支付要求用户已经安装支付宝客户端
            Toast.makeText(activity, "请安装支付宝客户端", Toast.LENGTH_SHORT)
                    .show()
            return
        }

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val cn = ComponentName("com.bmob.app.sport", "com.bmob.app.sport.wxapi.BmobActivity")
        intent.component = cn
        startActivity(intent)

        BP.pay(t.objectId, t.name, t.price as Double, true, object : PListener{
            override fun unknow() {
                Toast.makeText(activity, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT).show()
                hideDialog()
            }

            override fun orderId(p0: String?) {
                p0?.let {
                    Log.i(TAG, "orderId : " + it)
                }
                showDialog("获取订单成功，等待跳转支付页面")
            }

            override fun fail(p0: Int, p1: String?) {
                if (p0 == -3) {
                    Toast.makeText(activity,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "支付中断!", Toast.LENGTH_SHORT).show()
                }
                hideDialog()
            }

            override fun succeed() {
                toast("支付成功")
                hideDialog()

                AVUser.getCurrentUser()?.let {
                    uploadInfo(t)
                }?: toast("Please Login First").show()
            }

        })
    }

    private fun checkPackageInstalled(packageName: String, browserUrl: String): Boolean {
        try {
            // 检查是否有支付宝客户端
            activity.packageManager.getPackageInfo(packageName, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            // 没有安装支付宝，跳转到应用市场
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=" + packageName)
                startActivity(intent)
            } catch (ee: Exception) {// 连应用市场都没有，用浏览器去支付宝官网下载
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(browserUrl)
                    startActivity(intent)
                } catch (eee: Exception) {
                    Toast.makeText(activity,
                            "您的手机上没有没有应用市场也没有浏览器，我也是醉了，你去想办法安装支付宝/微信吧",
                            Toast.LENGTH_SHORT).show()
                }

            }

        }

        return false
    }

    private fun showDialog(msg : String){
        dialog.setMessage(msg)
        dialog.show()
    }

    private fun hideDialog(){
        dialog.dismiss()
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

    fun ViewHolder.setImageWithPicasso(viewId : Int, url : String) : ViewHolder{
        val view : ImageView = getView(viewId)
        com.squareup.picasso.Picasso.with(context)
                .load(url)
                .into(view)
        return this
    }

}