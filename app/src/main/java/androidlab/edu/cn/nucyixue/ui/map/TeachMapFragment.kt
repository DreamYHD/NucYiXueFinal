package androidlab.edu.cn.nucyixue.ui.map

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidlab.edu.cn.nucyixue.R
import androidlab.edu.cn.nucyixue.utils.LocationManager
import androidlab.edu.cn.nucyixue.utils.config.MTConfig
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.*
import com.amap.api.maps.model.animation.Animation
import com.amap.api.maps.model.animation.ScaleAnimation
import com.avos.avoscloud.*
import com.bigkoo.pickerview.TimePickerView
import java.text.SimpleDateFormat
import java.util.*

/**
 * MapFragment
 *
 * 自定义 InfoWindows 内容
 *
 * Created by MurphySL on 2017/7/7. Show All points by Einsame 2017/9/9
 */
object TeachMapFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName
    private val TEAM_FINISH : String = "finish"
    private val TEAM_ON : String = "on"


    private var select_time: Calendar = Calendar.getInstance() // Live 开始时间

    private lateinit var map: MapView
    private lateinit var aMap: AMap
    private lateinit var add: Button
    private lateinit var uiSettings: UiSettings
    private lateinit var searchEdit :EditText
    private lateinit var searchButton : Button

    private var locationManager: LocationManager? = null

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        map = view!!.findViewById(R.id.map)
        map.onCreate(savedInstanceState)
        add = view!!.findViewById(R.id.map_add_circle)
        aMap = map.map
        //初始化地图图层配置
        initMap()
        //初始化位置信息
        initLocation()
        //服务器所有节点查找
        queryAllLatlng()
        //设置mark点击事件
        clickerListener()
    }

    private fun clickerListener() {
        aMap.setOnMarkerClickListener {
            marker->
            if (marker.snippet.endsWith(TEAM_FINISH)){
                createDialog(marker, TEAM_FINISH)
            }else{
                createDialog(marker, TEAM_ON)
            }
            val myInfo = MyInfoWindow(context, marker.title, "Android 实验室")
            aMap.setInfoWindowAdapter(myInfo)
            marker.showInfoWindow()
            true
        }
        aMap.setOnInfoWindowClickListener { arg0 ->
            Snackbar.make(map, "已点击", Snackbar.LENGTH_SHORT).show()
        }
        searchButton.setOnClickListener{
            view ->

        }
    }
    //根据完成状态获取不同的dialogFragment
    private fun createDialog(marker: Marker, teaM_FINISH: String) {
        val s = marker.snippet.split("\"")
        if (teaM_FINISH.equals(TEAM_FINISH)){
            val m = DialogShowResultFragment.getInstance(s[0])
            m.show(fragmentManager,"showInvite")
        }else{
            val m = DialogShowInviteFragment.getInstance(s[0])
            m.show(fragmentManager,"showInvite")
        }
    }
    private fun queryAllLatlng() {
        val query: AVQuery<AVObject> = AVQuery(MTConfig.TABLE_NAME)
        query.findInBackground(object : FindCallback<AVObject>() {
            override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                if (null != p1) {
                    Toast.makeText(context, "query fail ", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "something error :$p1")
                } else {
                    p0?.let {
                        for (mTeam in p0) {
                            mTeam?.let {
                                val latitude = mTeam.getDouble(MTConfig.TEAM_LOCATION_LAT)
                                val longitude = mTeam.getDouble(MTConfig.TEAM_LOCATION_LONG)
                                val location = LatLng(latitude, longitude)
                                val teamId = mTeam.objectId
                                initMarks(location, teamId)
                                Log.d(TAG, "${latitude}${longitude} id is ${teamId}")
                            }
                        }
                    }
                }
            }
        })
    }
    private fun initMarks(latLng: LatLng, teamId: String) {
        val query: AVQuery<AVObject> = AVQuery(MTConfig.TABLE_NAME)
        query.whereEqualTo(MTConfig.TABLE_ID, teamId)
        query.getFirstInBackground(object : GetCallback<AVObject>() {
            override fun done(p0: AVObject?, p1: AVException?) {
                p0?.let {
                    Log.d(TAG, p0.getString(MTConfig.TEAM_NAME))
                    val location: LatLng = latLng
                    // Marker
                    val markerOption = MarkerOptions()
                    markerOption.position(location)
                    markerOption.title(p0.getString(MTConfig.TEAM_NAME)).snippet(p0.objectId)
                    markerOption.draggable(false)//设置Marker可拖动
                    markerOption.setFlat(false)//设置marker平贴地图效果
                    //判断是否已经完成
                    if (p0.get(MTConfig.TEAM_IS_START) as Boolean){
                        markerOption.title(p0.getString(MTConfig.TEAM_NAME)).snippet(p0.objectId+"\""+"finish")
                        mark(markerOption, R.drawable.zhuhe);
                    }else{
                        markerOption.title(p0.getString(MTConfig.TEAM_NAME)).snippet(p0.objectId+"\""+"end")
                        mark(markerOption, R.drawable.team)
                    }
                    val marker: Marker = aMap.addMarker(markerOption)
                    val anim: Animation = ScaleAnimation(0f, 1.0f, 0f, 1.0f)
                    val duration: Long = 1000L
                    anim.setDuration(duration)
                    anim.setInterpolator(BounceInterpolator())
                    marker.setAnimation(anim)
                    marker.startAnimation()
                    val myInfo = MyInfoWindow(context, marker.title, "yixuenuc")
                    aMap.setInfoWindowAdapter(myInfo)
                    marker.showInfoWindow()
                }
            }
        })
    }
    /**
     * 绘制图标
     */
    private fun mark(team: MarkerOptions, icon: Int) {
        team.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), icon)))
    }
    private fun initMap() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18.0F))
        //定位蓝点
        val myLocationStyle = MyLocationStyle()
        //myLocationStyle.radiusFillColor(resources.getColor(R.color.colorTheme)) // 圆框颜色
        //myLocationStyle.strokeColor(resources.getColor(R.color.colorTheme))
        myLocationStyle.interval(3000)
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE)
        aMap.myLocationStyle = myLocationStyle
        aMap.isMyLocationEnabled = true
        uiSettings = aMap.uiSettings
        uiSettings.isZoomControlsEnabled = false
    }
    private fun initLocation() {
        locationManager = LocationManager(context)
        if (locationManager != null) {
            locationManager?.setLocationListener(object : LocationManager.LocationListener {
                override fun onLocationChanged(aMapLocation: AMapLocation?) {
                    if (aMapLocation != null && aMapLocation.errorCode == 0) {
                        val location: LatLng = LatLng(aMapLocation.latitude, aMapLocation.longitude)
                        //Log.i(TAG, "${aMapLocation.latitude} ${aMapLocation.longitude} ${aMapLocation.address}")
                        add.setOnClickListener { view ->
                            Toast.makeText(context, "select start time ", Toast.LENGTH_SHORT).show()
                            val mTimePickerView = TimePickerView.Builder(context, TimePickerView.OnTimeSelectListener { date, v ->
                                val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                select_time.setTime(date)
                                //创建小组dialog
                                val mCreateTeamTime: String = (mSimpleDateFormat.format(date))
                                val createDialog = DialogCreateTeamFragment.getInstance(location, mCreateTeamTime)
                                createDialog.show(fragmentManager, "create")
                            }).isCenterLabel(false)
                                    .setRangDate(Calendar.getInstance(), null)
                                    .setSubmitColor(resources.getColor(R.color.colorPrimary))
                                    .setCancelColor(resources.getColor(R.color.colorPrimary))
                                    .setContentSize(14)
                                    .build()
                            mTimePickerView.setDate(select_time)
                            mTimePickerView.show()
                        }
                    } else {
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
        if (locationManager != null)
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