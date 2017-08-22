package androidlab.edu.cn.nucyixue.ui.teachPack.live

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log

import androidlab.edu.cn.nucyixue.R
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
import kotlinx.android.synthetic.main.activity_live_comment.*

/**
 * Live star 待修改
 */
class LiveCommentActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_comment)

        comment()
    }

    private fun comment() {
        val bundle : Bundle = intent.extras.get(LCConfig.LIVE_TABLE) as Bundle
        val live : Live = bundle.getParcelable(LCConfig.LIVE_TABLE)
        val lu : LU = bundle.getParcelable(LCConfig.LU_TABLE)

        live_name.text = live.name

        toolbar.setNavigationOnClickListener {
            finish()
        }

        skip.setOnClickListener {
            enterLive(live)
            finish()
        }

        bt_create.setOnClickListener{
            val rating = live_star.rating
            val comment = live_summary.text.toString()

            if(comment.isNotEmpty()){
                lu.comment = comment
            }
            lu.star = rating.toInt()
            lu.saveInBackground(object : SaveCallback(){
                override fun done(p0: AVException?) {
                    if(p0 == null){
                        val query = AVQuery<Live>(LCConfig.LIVE_NAME)
                        query.whereEqualTo(LCConfig.LIVE_ID, live.objectId)
                        query.findInBackground(object : FindCallback<Live>(){
                            override fun done(p0: MutableList<Live>?, p1: AVException?) {
                                if(p1 == null){
                                    p0?.let {
                                        if(p0.isNotEmpty()){
                                            val l = p0[0]
                                            l.star = rating.toInt()
                                            l.saveInBackground(object : SaveCallback(){
                                                override fun done(p0: AVException?) {
                                                    if(p0 == null){
                                                        toast("评价成功").show()
                                                    }else{
                                                        toast("评价失败").show()
                                                        Log.i(TAG, "评价失败 LV：$p0")
                                                    }
                                                    enterLive(live)
                                                }
                                            })
                                        }else{
                                            toast("评价失败").show()
                                            Log.i(TAG, "评价失败 li")
                                        }
                                    }?: Log.i(TAG, "评价失败 li")
                                }else{
                                    toast("评价失败").show()
                                    Log.i(TAG, "评价失败 qu：$p1")
                                }
                            }
                        })

                    }else{
                        toast("评价失败").show()
                        Log.i(TAG, "评价失败：$p0")
                        enterLive(live)
                    }
                }

            })
        }
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
                                if(p0 != null){
                                    toast("Enter Conversation Fail").show()
                                    Log.i(TAG, "Enter Conversation Fail: $p0")
                                }else{
                                    Log.i(TAG, "Get Conversation Success")
                                }

                                val intent = Intent(this@LiveCommentActivity, ConversationActivity::class.java)
                                intent.putExtra(LCConfig.LIVE_TABLE, live)
                                intent.putExtra(LCIMConstants.CONVERSATION_ID, live.conversationId)
                                startActivity(intent)
                                finish()
                            }
                        })
                    }?: toast("未知的错误").show()
                }
            })
        }
    }

    private fun toast(msg : String) : Snackbar = Snackbar.make(skip, msg, Snackbar.LENGTH_LONG)

}
