package androidlab.edu.cn.nucyixue.ui.im

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import androidlab.edu.cn.nucyixue.R
import androidlab.edu.cn.nucyixue.data.bean.Live
import androidlab.edu.cn.nucyixue.service.LiveAudioService
import androidlab.edu.cn.nucyixue.utils.config.Config
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.activity.LCIMConversationFragment
import cn.leancloud.chatkit.cache.LCIMConversationItemCache
import cn.leancloud.chatkit.utils.LCIMConstants
import cn.leancloud.chatkit.utils.LCIMConversationUtils
import cn.leancloud.chatkit.utils.LCIMLogUtils
import com.avos.avoscloud.AVCallback
import com.avos.avoscloud.AVException
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.AVIMMessage
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage
import kotlinx.android.synthetic.main.activity_conversation.*

/**
 * Conversation Activity
 *
 * Created by MurphySL on 2017/8/20.
 */
class ConversationActivity : AppCompatActivity(){
    private val TAG = this.javaClass.simpleName

    private val conversationFragment = LCIMConversationFragment()
    private lateinit var binder : LiveAudioService.AudioBinder // 异步执行
    var live : Live? = null

    private val conn = object : ServiceConnection{
        override fun onServiceDisconnected(p0: ComponentName?) {}

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            binder = p1 as LiveAudioService.AudioBinder
            binder.updateInfo(live)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        live = intent.extras.getParcelable(Config.LIVE_TABLE)
        Log.i(TAG, live?.userId)
        val b = Bundle()
        b.putString("live", live?.userId)
        conversationFragment.arguments = b
        initFragment()

        LCChatKit.getInstance().profileProvider = CustomUserProvider.getInstance() // 设置用户系统

        bindService(Intent(ConversationActivity@this, LiveAudioService::class.java), conn, Context.BIND_AUTO_CREATE)
    }

    override fun onStart() {
        super.onStart()
        initByIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initByIntent(intent)
    }

    private fun initByIntent(intent: Intent?) {
        LCChatKit.getInstance().client?.let {
            intent?.let {
                val bundle = intent.extras
                bundle?.let {

                    when {
                        bundle.containsKey(LCIMConstants.PEER_ID) -> getConversation(bundle.getString(LCIMConstants.PEER_ID))
                        bundle.containsKey(LCIMConstants.CONVERSATION_ID) -> {
                            val conversationId = bundle.getString(LCIMConstants.CONVERSATION_ID)
                            updateConversation(LCChatKit.getInstance().client.getConversation(conversationId))
                        }
                        else -> {
                            Snackbar.make(conversation_layout, "Need Conversation Id", Snackbar.LENGTH_LONG).show()
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun initActionBar(title : String?){
        val actionBar = supportActionBar
        actionBar?.let {
            it.title = title
            it.setDisplayUseLogoEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            finishActivity(Activity.RESULT_OK)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(android.R.id.home == item?.itemId){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateConversation(conversation: AVIMConversation?) {
        conversation?.let {
            conversation.queryMessages(object : AVIMMessagesQueryCallback(){
                override fun done(p0: MutableList<AVIMMessage>?, p1: AVIMException?) {
                    if(p1 == null){
                        p0?.let {
                            if(p0.isNotEmpty()){
                                p0.forEach {
                                    if(it is AVIMAudioMessage){
                                        Log.i(TAG, "get Audio Message")
                                        binder.loadAudio(it)
                                    }
                                }
                            }
                        }
                    }else{
                        Log.i(TAG, "Get Message Fail: $p1")
                    }
                }
            })

            conversationFragment.setConversation(conversation)
            LCIMConversationItemCache.getInstance().insertConversation(conversation.conversationId)
            LCIMConversationUtils.getConversationName(conversation, object : AVCallback<String>(){
                override fun internalDone0(p0: String?, p1: AVException?) {
                    if(p1 != null){
                        LCIMLogUtils.logException(p1)
                    }else{
                        initActionBar(p0)
                    }
                }

            })
        }
    }

    private fun getConversation(memberId: String) {
        LCChatKit.getInstance().client.createConversation(
                arrayListOf(memberId),
                "",
                null,
                false,
                true,
                object : AVIMConversationCreatedCallback(){
                    override fun done(p0: AVIMConversation?, p1: AVIMException?) {
                        if(p1 == null){
                            p0?.let {
                                updateConversation(p0)
                            }
                        }else{
                            Snackbar.make(conversation_layout, p1.message.toString(), Snackbar.LENGTH_SHORT).show()
                        }
                    }

                }
        )
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction().add(R.id.container, conversationFragment).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(conn)
    }

}