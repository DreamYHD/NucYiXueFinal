package androidlab.edu.cn.nucyixue.ui.teachPack.live

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.util.Log
import android.widget.ProgressBar
import androidlab.edu.cn.nucyixue.R
import androidlab.edu.cn.nucyixue.data.bean.Live
import androidlab.edu.cn.nucyixue.ui.imPack.ConversationActivity
import androidlab.edu.cn.nucyixue.utils.FileUtils
import androidlab.edu.cn.nucyixue.utils.config.LCConfig
import androidlab.edu.cn.nucyixue.utils.config.LiveType
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.LCChatKitUser
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import com.bigkoo.pickerview.OptionsPickerView
import com.bigkoo.pickerview.TimePickerView
import com.bumptech.glide.Glide

import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine

import kotlinx.android.synthetic.main.activity_create_live.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * 创建 Live
 *
 * 待修改：
 * 2. 多人主讲
 * 3. 流程：开通主讲人身份 -> 实名认证 -> 交纳保证金
 * 5.创建live完成后流程
 * 7.官方用户修改
 * 8.官方用户订阅信息更新
 *
 * Created by MurphySL on 2017/7/24.
 */
class CreateLiveActivity : AppCompatActivity(){
    private val TAG : String = this.javaClass.simpleName

    //private val context : Context = this

    private var select_time: Calendar = Calendar.getInstance() // Live 开始时间
    private var select_uri: Uri? = null // Live 封面
    private var select_type : String = ""

    private val REQUEST_CODE_CHOOSE = 0x110

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_live)

        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            select_uri = Matisse.obtainResult(data)[0]
            select_uri?.let {
                Glide.with(this)
                        .load(it)
                        .into(live_img)

                val bitmap = BitmapFactory.decodeFile(FileUtils.getFilePahtFromUri(this, it))

                Palette.from(bitmap).generate {
                    it.darkVibrantSwatch?.let {
                        nestedScrollView.setBackgroundColor(it.rgb)
                    }?:it.darkMutedSwatch?.let {
                        nestedScrollView.setBackgroundColor(it.rgb)
                    }?:it.vibrantSwatch?.let {
                        nestedScrollView.setBackgroundColor(it.rgb)
                    }
                }
            }

        }
    }

    private fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        bt_create.setOnClickListener {
            confirmInfo()
        }

        card_live_time.setOnClickListener {
            val pvTime = TimePickerView.Builder(this, TimePickerView.OnTimeSelectListener {
                date, _ ->
                val sdf  = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                select_time.time = date
                live_time.text = sdf.format(date)
            })
                    .isCenterLabel(false)
                    .setRangDate(Calendar.getInstance(), null)
                    .setSubmitColor(resources.getColor(R.color.colorPrimary))
                    .setCancelColor(resources.getColor(R.color.colorPrimary))
                    .setContentSize(14)
                    .build()
            pvTime.setDate(select_time)
            pvTime.show()
        }

        card_live_type.setOnClickListener {
            val types = LiveType.toValueList()
            val pvOptions = OptionsPickerView.Builder(this, OptionsPickerView.OnOptionsSelectListener {
                options1, _, _, _ ->
                live_type.text = types[options1]
                select_type = types[options1]
            })
                    .setSubmitColor(resources.getColor(R.color.colorPrimary))
                    .setCancelColor(resources.getColor(R.color.colorPrimary))
                    .build()
            pvOptions.setPicker(types)
            pvOptions.show()
        }

        live_img.setOnClickListener {
            Matisse.from(this)
                    .choose(MimeType.allOf())
                    .maxSelectable(1)
                    .thumbnailScale(0.85f)
                    .imageEngine(GlideEngine())
                    .forResult(REQUEST_CODE_CHOOSE)
        }
    }

    private fun confirmInfo(){
        val live_name : String = live_name.text.toString()
        val live_price : String = live_price.text.toString()
        val live_time : String = live_time.text.toString()
        val live_summary : String = live_summary.text.toString()
        val live_type : String = live_type.text.toString()
        when {
            live_name.isEmpty() -> Snackbar.make(img_live_name, "请输入 Live 主题！", Snackbar.LENGTH_LONG).show()
            live_price.isEmpty() -> Snackbar.make(img_live_name, "请输入 Live 价格！", Snackbar.LENGTH_LONG).show()
            live_time.isEmpty() -> Snackbar.make(img_live_name, "请选择开始时间！", Snackbar.LENGTH_LONG).show()
            live_summary.isEmpty() -> Snackbar.make(img_live_name, "请输入 Live 简介！", Snackbar.LENGTH_LONG).show()
            live_type.isEmpty() -> Snackbar.make(img_live_name, "请选择 Live 种类！", Snackbar.LENGTH_LONG).show()
            select_uri == null -> Snackbar.make(img_live_name, "请选择 Live 封面！", Snackbar.LENGTH_LONG).show()
            else -> {
                mProgress.visibility = ProgressBar.VISIBLE

                val path = FileUtils.getFilePahtFromUri(this, select_uri!!)
                path?.let {
                    Log.i(TAG, "live_name: $live_name \n live_price: $live_price \n live_time: $live_time \n live_summary: $live_summary \n live_type: $live_type \n live_pic: $path")
                    createLiveWithPic(live_name, select_time.time, live_summary, live_price.toInt(), select_type, it)
                }?: Log.i(TAG, "解析图片出错")
            }
        }
    }

    private fun createLiveWithPic(live_name : String, live_time : Date, live_summary : String, live_price : Int, live_type : String, path : String){
        val fileName = FileUtils.getFileName(path)
        val file : AVFile = AVFile.withAbsoluteLocalPath(fileName, path)

        file.saveInBackground(object : SaveCallback(){
            override fun done(p0: AVException?) {
                if(p0 == null){

                    val userId = AVUser.getCurrentUser().objectId // 用户ID
                    val username = AVUser.getCurrentUser().username
                    val audiences : ArrayList<LCChatKitUser> = ArrayList() // 主讲人及测试用户
                    val test_audience = LCChatKitUser("599ac99a570c35006089bd47", "一学", "http://ac-O5aEuqAR.clouddn.com/owJk3eFkfNcDceaDUYWjz0bfDzJb8ag2r8Y8Ua70.jpg") // 官方测试用户
                    audiences.add(test_audience)
                    //其他主讲人：

                    val audiences_clientId : ArrayList<String> = ArrayList() // 所有听众 clientId
                    audiences.mapTo(audiences_clientId) { it.userId }

                    // 群聊
                    LCChatKit.getInstance().client.createConversation(audiences_clientId, live_name, null, false, true, object : AVIMConversationCreatedCallback(){
                        override fun done(p0: AVIMConversation?, p1: AVIMException?) {
                            p0?.let {
                                val live  = Live()
                                live.userId = userId
                                live.username = username
                                live.conversationId = it.conversationId
                                live.name = live_name
                                live.summary = live_summary
                                live.startAt = live_time
                                live.price = live_price
                                live.type = live_type
                                live.pic = file.url

                                live.put(LCConfig.LIVE_USER_ID, AVObject.createWithoutData(LCConfig.USER_TABLE, userId))
                                live.put(LCConfig.LIVE_USER_NAME, username)
                                live.put(LCConfig.LIVE_CONVERSATION_ID, AVObject.createWithoutData(LCConfig.CONVERSATION_TABLE, it.conversationId))
                                live.put(LCConfig.LIVE_NAME, live_name)
                                live.put(LCConfig.LIVE_SUMMARY, live_summary)
                                live.put(LCConfig.LIVE_START_AT, live_time)
                                live.put(LCConfig.LIVE_PRICE, live_price)
                                live.put(LCConfig.LIVE_TYPE, live_type)
                                live.put(LCConfig.LIVE_PIC, file.url)

                                live.saveInBackground(object : SaveCallback(){
                                    override fun done(p0: AVException?) {
                                        if(p0 != null){
                                            mProgress.visibility = ProgressBar.GONE
                                            Snackbar.make(img_live_name, "创建 Live 信息失败！$p0", Snackbar.LENGTH_SHORT).show()
                                            Log.i(TAG, "创建 Live 信息失败！$p0")
                                        }else{
                                            mProgress.visibility = ProgressBar.GONE
                                            Snackbar.make(img_live_name, "创建Live成功", Snackbar.LENGTH_SHORT).show()
                                            val intent = Intent(this@CreateLiveActivity, ConversationActivity::class.java)
                                            intent.putExtra(LCConfig.LIVE_TABLE, live)
                                            intent.putExtra(LCIMConstants.CONVERSATION_ID, it.conversationId)
                                            startActivity(intent)
                                        }
                                    }
                                })

                            }?: Snackbar.make(img_live_name, "创建 Live 失败！$p0", Snackbar.LENGTH_SHORT).show()

                            Log.i(TAG, p1.toString())
                        }
                    })
                }else{
                    mProgress.visibility = ProgressBar.GONE
                    Snackbar.make(live_img, "图片上传失败", Snackbar.LENGTH_LONG).show()
                }
            }
        })

    }

}
