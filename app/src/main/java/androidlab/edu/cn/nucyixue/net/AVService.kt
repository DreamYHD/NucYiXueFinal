package androidlab.edu.cn.nucyixue.net

import android.util.Log
import androidlab.edu.cn.nucyixue.data.bean.LU
import androidlab.edu.cn.nucyixue.data.bean.Live
import androidlab.edu.cn.nucyixue.utils.config.LCConfig
import androidlab.edu.cn.nucyixue.utils.rx.RxSchedulerHelper
import com.avos.avoscloud.*
import io.reactivex.Observable


/**
 * AVService
 *
 * Created by MurphySL on 2017/9/23.
 */
object AVService{
    private val TAG : String = javaClass.simpleName

    private fun <T : AVObject> queryAV(query : AVQuery<T>) : Observable<T> =
                Observable.create<T> {
                    e ->
                    query.findInBackground(object : FindCallback<T>(){
                        override fun done(p0: MutableList<T>?, p1: AVException?) {
                            if(p1 == null){
                                if(p0 == null || p0.size == 0){
                                    e.onError(Throwable("NullPointerException : Query Live Fail"))
                                    Log.i(TAG, "NullPointerException : Query Live Fail")
                                }else{
                                    p0.forEach {
                                        e.onNext(it)
                                    }
                                    e.onComplete()
                                }
                            }else{
                                e.onError(p1)
                                Log.i(TAG, p1.toString())
                            }
                        }
                    })
                }.compose(RxSchedulerHelper.io_main())

    fun queryAllLive() : Observable<Live> {
        val query : AVQuery<Live> = AVQuery(LCConfig.LIVE_TABLE)
        query.addDescendingOrder(LCConfig.LIVE_STAR) // 按星级排序
        query.addDescendingOrder(LCConfig.LIVE_START_AT) // 按时间排序
        return queryAV(query)
    }

    private fun queryLiveByLId(liveId : String) : Observable<Live> {
        val query : AVQuery<Live> = AVQuery(LCConfig.LIVE_TABLE)
        query.whereEqualTo(LCConfig.LIVE_ID, liveId)
        return queryAV(query)
    }

    fun queryJoinedByUId(objectId : String) : Observable<Live>{
        val query = AVQuery<LU>("LU")
        query.whereEqualTo(LCConfig.LU_USER_ID, AVObject.createWithoutData(LCConfig.USER_TABLE, objectId))
        return queryAV(query)
                .flatMap {
                    queryLiveByLId(it.liveId)
                }
    }

    fun queryCreatedLive(objectId: String) : Observable<Live>{
        val query : AVQuery<Live> = AVQuery(LCConfig.LIVE_TABLE)
        query.whereEqualTo(LCConfig.LIVE_USER_ID, objectId)
        return queryAV(query)
    }

    fun queryJoinedByLUId(liveId : String, objectId : String) : Observable<LU>{
        Log.i(TAG, "queryByLUId")
        val query = AVQuery<LU>("LU")
        query.whereEqualTo(LCConfig.LU_USER_ID, AVObject.createWithoutData(LCConfig.USER_TABLE, objectId))
        query.whereEqualTo(LCConfig.LU_LIVE_ID, AVObject.createWithoutData(LCConfig.LIVE_TABLE, liveId))
        return queryAV(query)
    }
}