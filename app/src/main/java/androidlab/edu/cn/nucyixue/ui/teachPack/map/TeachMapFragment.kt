package androidlab.edu.cn.nucyixue.ui.teachPack.map

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidlab.edu.cn.nucyixue.R
import androidlab.edu.cn.nucyixue.utils.LocationManager
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.maps.model.animation.Animation
import com.amap.api.maps.model.animation.ScaleAnimation

/**
 * MapFragment
 *
 * 自定义 InfoWindows 内容
 *
 * Created by MurphySL on 2017/7/7.
 */
object TeachMapFragment : Fragment() {

    private val TAG = "MapFragment"

    private lateinit var map : MapView
    private lateinit var aMap : AMap
    private lateinit var uiSettings : UiSettings

    private var locationManager : LocationManager?= null

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        map = view!!.findViewById(R.id.map)

        map.onCreate(savedInstanceState)
        aMap = map.map

        initMap()
        initLocation()
        initMarks()
    }

    private fun initMarks() {

        aMap.setInfoWindowAdapter(MyInfoWindow(context, "中北大学", "Android 实验室"))
        aMap.setOnInfoWindowClickListener {
            arg0 -> Snackbar.make(map, "已点击", Snackbar.LENGTH_SHORT).show()
        }

        val location : LatLng = LatLng(38.014836, 112.449396)
        // Marker
        val marker : Marker = aMap.addMarker(MarkerOptions().position(location))

        val anim : Animation = ScaleAnimation(0f, 1.0f, 0f, 1.0f)
        val duration : Long = 1000L
        anim.setDuration(duration)
        anim.setInterpolator(BounceInterpolator())
        marker.setAnimation(anim)
        marker.showInfoWindow()
        marker.startAnimation()
    }

    private fun initMap() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18.0F))
        //定位蓝点
        val myLocationStyle = MyLocationStyle()
        //myLocationStyle.radiusFillColor(resources.getColor(R.color.colorTheme)) // 圆框颜色
        //myLocationStyle.strokeColor(resources.getColor(R.color.colorTheme))
        myLocationStyle.interval(2000)
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE)
        aMap.myLocationStyle = myLocationStyle
        aMap.isMyLocationEnabled = true

        uiSettings = aMap.uiSettings
        uiSettings.isZoomControlsEnabled = false
    }

    private fun initLocation() {
        locationManager = LocationManager(context)
        if(locationManager != null){
            locationManager?.setLocationListener(object : LocationManager.LocationListener {
                override fun onLocationChanged(aMapLocation: AMapLocation?) {
                    if(aMapLocation != null && aMapLocation.errorCode == 0){
                        val location : LatLng = LatLng(aMapLocation.latitude, aMapLocation.longitude)
                        //Log.i(TAG, "${aMapLocation.latitude} ${aMapLocation.longitude} ${aMapLocation.address}")
                    }else {
                        Log.i(TAG, "定位失败，")
                    }
                }
            })

            locationManager?.openLocation()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_map, container, false)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(locationManager != null)
            locationManager?.stopLocation()
        locationManager = null
        map.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }


}