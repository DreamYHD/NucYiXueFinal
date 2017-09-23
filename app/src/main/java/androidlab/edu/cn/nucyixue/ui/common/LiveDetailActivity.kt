package androidlab.edu.cn.nucyixue.ui.common

import android.app.ProgressDialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidlab.edu.cn.nucyixue.R
import androidlab.edu.cn.nucyixue.data.bean.Live
import androidlab.edu.cn.nucyixue.data.bean.Subject
import androidlab.edu.cn.nucyixue.ui.findPack.subject.SubjectContentActivity
import androidlab.edu.cn.nucyixue.utils.FlexTextUtil
import androidlab.edu.cn.nucyixue.utils.config.LCConfig
import c.b.BP
import c.b.PListener
import com.avos.avoscloud.AVUser
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.activity_live_detail.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * LiveDetailActivity
 *
 * Created by MurphySL on 2017/9/23.
 */
class LiveDetailActivity : AppCompatActivity(){
    private val TAG : String = javaClass.simpleName

    private var live : Live? = null

    //待修改
    private lateinit var dialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_detail)

        live = intent.extras.getParcelable(LCConfig.LIVE_TABLE)
        Log.i(TAG, live.toString())

        initView()
    }

    private fun initView() {
        //待修改
        dialog = ProgressDialog(this)
        dialog.setCancelable(true)

        (this as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = title
        toolbar.navigationIcon = resources.getDrawable(R.drawable.back)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        live?.let {
            live_name.text = it.name
            live_star.rating = it.star.toFloat()
            live_join.text = "立即参加 (${it.price})"
            live_speaker.text = it.username
            live_summary.text = it.summary
            val keys : MutableList<String> = ArrayList()
            it.keyword?.forEach { keys += it }
            val format = SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
            val startAt = format.format(it.startAt)
            keys += startAt
            keys += if (it.startAt < Date(System.currentTimeMillis())) "已完结" else "未开始"

            for (i in 0 until keys.size) {
                val model = Subject()
                model.id = i
                model.name = keys[i]
                live_key.addView(createNewFlexItemTextView(model))
            }

            live_join.setOnClickListener {
                //pay()
            }
        }
    }

    private fun createNewFlexItemTextView(book: Subject): TextView {
        val textView = TextView(this)
        textView.gravity = Gravity.CENTER
        textView.text = book.name
        textView.textSize = 15f
        textView.textSize = 10f
        textView.setTextColor(resources.getColor(R.color.colorPrimary))
        textView.setBackgroundResource(R.drawable.shape_back)
        textView.tag = book.id
        val padding = FlexTextUtil.dpToPixel(this, 3)
        val paddingLeftAndRight = FlexTextUtil.dpToPixel(this, 4)
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight + 4, padding, paddingLeftAndRight + 4, padding)
        val layoutParams = FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val margin = FlexTextUtil.dpToPixel(this, 4)
        val marginTop = FlexTextUtil.dpToPixel(this, 8)
        layoutParams.setMargins(margin + 10, marginTop, margin, 0)
        textView.layoutParams = layoutParams
        return textView
    }

    private fun pay(t : Live){
        if (!checkPackageInstalled("com.eg.android.AlipayGphone", "https://www.alipay.com")) { // 支付宝支付要求用户已经安装支付宝客户端
            toast("请安装支付宝客户端")
            return
        }

        try {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val cn = ComponentName("com.bmob.app.sport", "com.bmob.app.sport.wxapi.BmobActivity")
            intent.component = cn
            this.startActivity(intent)
        }catch (e : Throwable){
            e.printStackTrace()
        }


        BP.pay(t.objectId, t.name, t.price.toDouble(), true, object : PListener {
            override fun unknow() {
                //Toast.makeText( , "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT).show()
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
                    //Toast.makeText(this,
                   //         "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                      //      Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(this, "支付中断!", Toast.LENGTH_SHORT).show()
                }
                hideDialog()
            }

            override fun succeed() {
                toast("支付成功")
                hideDialog()

                AVUser.getCurrentUser()?.let {
                    //uploadInfo(t)
                }?: toast("Please Login First").show()
            }

        })
    }

    private fun showDialog(msg : String){
        dialog.setMessage(msg)
        dialog.show()
    }

    private fun hideDialog(){
        dialog.dismiss()
    }

    private fun checkPackageInstalled(packageName: String, browserUrl: String): Boolean {
        try {
            // 检查是否有支付宝客户端
            packageManager.getPackageInfo(packageName, 0)
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
                    Toast.makeText(this,
                            "您的手机上没有没有应用市场也没有浏览器，我也是醉了，你去想办法安装支付宝/微信吧",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
        return false
    }

    private fun toast(msg : String) : Snackbar = Snackbar.make(live_key, msg, Snackbar.LENGTH_LONG)


}