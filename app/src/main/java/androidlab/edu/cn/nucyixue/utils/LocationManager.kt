package androidlab.edu.cn.nucyixue.utils

import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

/**
 * Location Util
 *
 * 初始化!!
 * Created by MurphySL on 2017/7/5.
 */
class LocationManager{

    private lateinit var locationClient : AMapLocationClient
    private lateinit var locationClientOption : AMapLocationClientOption // 定位参数

    constructor(context: Context){
        locationClient = AMapLocationClient(context.applicationContext)
    }


    /**
     * 默认定位参数
     */
    private fun setDefaultLocationClientOption() {
        locationClientOption = AMapLocationClientOption()

        // 高精度
        // 连续定位时间间隔
        // 连续定位
        // 默认连续定位策略
        // 是否需要返回地址描述
        locationClientOption.isWifiScan = true // 强制刷新 WIFI
        // 不允许模拟 GPS 位置结果
        // 定位请求超时时间 30s
        // 网络定位协议 HTTP
        locationClient.setLocationOption(locationClientOption)
    }

    /**
     * 开始定位
     */
    fun openLocation(){
        setDefaultLocationClientOption()
        locationClient.startLocation()
    }

    /**
     * 停止定位
     */
    fun stopLocation(){
        locationClient.stopLocation()
        locationClient.onDestroy()
    }

    /**
     * 定位回调
     */
    interface LocationListener{
        fun onLocationChanged(aMapLocation : AMapLocation?)
    }

    /**
     * 设置回调监听
     */
    fun setLocationListener(locationListener : LocationListener){
        val aMapLoctionListener = AMapLocationListener {
            aMapLocation -> locationListener.onLocationChanged(aMapLocation)
        }
        locationClient.setLocationListener(aMapLoctionListener)
    }

}
