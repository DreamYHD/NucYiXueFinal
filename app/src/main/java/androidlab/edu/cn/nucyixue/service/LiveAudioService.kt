package androidlab.edu.cn.nucyixue.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.support.v7.app.NotificationCompat
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidlab.edu.cn.nucyixue.R
import androidlab.edu.cn.nucyixue.data.bean.Live
import androidlab.edu.cn.nucyixue.data.bean.UserInfo
import androidlab.edu.cn.nucyixue.utils.FileUtils
import androidlab.edu.cn.nucyixue.utils.config.LCConfig
import cn.leancloud.chatkit.cache.LCIMLocalCacheUtils
import cn.leancloud.chatkit.utils.LCIMAudioHelper
import cn.leancloud.chatkit.utils.LCIMPathUtils
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import com.avos.avoscloud.FindCallback
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage
import java.lang.Exception

/**
 * Live Audio Service
 *
 * 待修改 binder service
 * 语音播放
 * 语音消息更新
 *
 * Created by MurphySL on 2017/8/19.
 */
class LiveAudioService : Service() {
    private val TAG : String = this.javaClass.simpleName

    private lateinit var remoteViews : RemoteViews
    private lateinit var manager : NotificationManager

    private val binder = AudioBinder()

    private val audioList = ArrayList<AVIMAudioMessage>()
    private var currentAudio = 0

    inner class AudioBinder : Binder(){

        fun loadAudio(audioMessage : AVIMAudioMessage){
            Log.i("AudioBinder", "LOAD MESSAGE ${audioList.size}")
            audioList.add(audioMessage)
        }

        fun updateInfo(live : Live?){
            live?.let {
                Log.i("AudioBinder", "Get Live Info Success: ${live.name}")
                updateNotificationLiveInfo(live)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val action = intent.action
            action?.let {
                when(it){
                    LCConfig.LIVE_SOUNDS_CHANGE -> {
                        updateNotificationMsgInfo()
                        changeAudio()
                    }
                    LCConfig.LIVE_SOUNDS_NEXT -> {
                        nextAudio()
                    }
                    LCConfig.LIVE_SOUNDS_PREVIOUS -> {
                        previousAudio()
                    }
                    else -> Unit
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder {
        initNotification()
        return binder
    }

    private fun initNotification() {
        remoteViews = RemoteViews(packageName, R.layout.item_live_sounds)

        val intent_change = Intent(this, LiveAudioService::class.java)
        intent_change.action = LCConfig.LIVE_SOUNDS_CHANGE
        val pending_change : PendingIntent = PendingIntent.getService(this, 1, intent_change, PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.change, pending_change)

        val intent_previous = Intent(this, LiveAudioService::class.java)
        intent_previous.action = LCConfig.LIVE_SOUNDS_PREVIOUS
        val pending_previous : PendingIntent = PendingIntent.getService(this, 1, intent_previous, PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.previous, pending_previous)

        val intent_next = Intent(this, LiveAudioService::class.java)
        intent_next.action = LCConfig.LIVE_SOUNDS_NEXT
        val pending_next : PendingIntent = PendingIntent.getService(this, 1, intent_next , PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.next, pending_next)

        manager =  getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat
                .Builder(this)
                .setCustomBigContentView(remoteViews)
                .setContentText("Live 主题")
                .setContentTitle("Live 主讲人")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .build()

        manager.notify(1, notification)
    }

    private fun isPlaying() : Boolean = LCIMAudioHelper.getInstance().isPlaying

    private fun playAudio(){
        val msg = audioList[currentAudio]
        Log.i(TAG, audioList[currentAudio].duration.toString())

        val localFilePath = msg.localFilePath
        var playPath = localFilePath
        if(localFilePath == null){
            playPath = LCIMPathUtils.getAudioCachePath(applicationContext, msg.messageId)
        }
        Log.i(TAG, "audio path : $playPath")
        LCIMLocalCacheUtils.downloadFile(msg.fileUrl, playPath, false, object : LCIMLocalCacheUtils.DownLoadCallback(){
            override fun done(e: Exception?) {
                if(e == null){
                    LCIMAudioHelper.getInstance().playAudio(playPath)
                }else{
                    Log.i(TAG, "Download File Fail: $e")
                }
            }
        })

    }

    private fun pauseAudio() = LCIMAudioHelper.getInstance().pausePlayer()

    fun changeAudio(){
        if(isPlaying() && audioList.isNotEmpty()){
            pauseAudio()
        }else{
            playAudio()
        }
        Log.i(TAG, "change ${isPlaying()}")
    }

    fun nextAudio(){
        Log.i(TAG, "next")
        if(audioList.isNotEmpty()){
            currentAudio ++
            if(currentAudio == audioList.size){
                currentAudio = 0
            }
            playAudio()
        }

    }

    fun previousAudio(){
        Log.i(TAG, "previous")
        if(audioList.isNotEmpty()){
            currentAudio --
            if(currentAudio == -1){
                currentAudio = audioList.size - 1
            }
            playAudio()
        }
    }

    private fun downloadCover(info : UserInfo, live : Live){
        val avatar_path = info.avatar
        avatar_path?.let {
            val path = LCIMPathUtils.getAudioCachePath(applicationContext, FileUtils.getFileName(avatar_path))
            Log.i(TAG, "Avatar Path: $path")
            LCIMLocalCacheUtils.downloadFile(avatar_path, path, true, object : LCIMLocalCacheUtils.DownLoadCallback() {
                override fun done(e: Exception?) {
                    if (e == null) {
                        val bitmap = BitmapFactory.decodeFile(path)
                        remoteViews.setImageViewBitmap(R.id.live_cover, bitmap)
                        remoteViews.setTextViewText(R.id.live_name, live.name)
                        remoteViews.setTextViewText(R.id.live_speaker, live.username)

                        val notification = NotificationCompat
                                .Builder(applicationContext)
                                .setCustomBigContentView(remoteViews)
                                .setContentText(live.name)
                                .setContentTitle(live.username)
                                .setSmallIcon(R.mipmap.ic_launcher) // 待修改
                                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                                .build()

                        manager.notify(1, notification)
                        Log.i(TAG, "download success")
                    }else{
                        Log.i(TAG, "download fail: $e")
                        remoteViews.setTextViewText(R.id.live_name, live.name)
                        remoteViews.setTextViewText(R.id.live_speaker, live.username)

                        val notification = NotificationCompat
                                .Builder(applicationContext)
                                .setCustomBigContentView(remoteViews)
                                .setContentText(live.name)
                                .setContentTitle(live.username)
                                .setSmallIcon(R.mipmap.ic_launcher) // 待修改
                                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                                .build()

                        manager.notify(1, notification)
                    }
                }
            })
        }

    }

    /**
     * 更新 Notification Live 信息
     */
    fun updateNotificationLiveInfo(live : Live){
        val query = AVQuery<UserInfo>(LCConfig.UI_TABLE)
        query.whereEqualTo(LCConfig.UI_USER_ID, AVObject.createWithoutData(LCConfig.USER_TABLE, live.userId))
        query.findInBackground(object : FindCallback<UserInfo>(){
            override fun done(p0: MutableList<UserInfo>?, p1: AVException?) {
                if(p1 != null){
                    Log.i(TAG, "Get Avatar Fail: $p1")
                }else{
                    p0?.let {
                        if(p0.isNotEmpty()){
                            val info = p0[0]
                            Log.i(TAG, "Get Avatar: ${info.avatar}")
                            downloadCover(info, live)
                        }
                    }
                }
            }

        })
    }

    /**
     * 更新 Notification Msg 进度
     */
    private fun updateNotificationMsgInfo() {
        Log.i(TAG,"updateNotification: ${isPlaying()}")
        if(isPlaying()){
            remoteViews.setImageViewResource(R.id.change, R.drawable.ic_play_circle_outline_black_24dp)
        }else{
            remoteViews.setViewVisibility(R.id.progress_layout, View.VISIBLE)
            remoteViews.setImageViewResource(R.id.change, R.drawable.ic_pause_circle_outline_black_24dp)
        }

        Log.i(TAG, audioList[currentAudio].duration.toString())
        remoteViews.setTextViewText(R.id.duration, audioList[currentAudio].duration.toString())

        val notification = NotificationCompat
                .Builder(this)
                .setCustomBigContentView(remoteViews)
                .setContentText("Live 主题")
                .setContentTitle("Live 主讲人")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .build()

        manager.notify(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}