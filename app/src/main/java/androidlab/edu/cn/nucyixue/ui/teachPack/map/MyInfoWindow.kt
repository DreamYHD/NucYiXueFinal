package androidlab.edu.cn.nucyixue.ui.teachPack.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidlab.edu.cn.nucyixue.R
import com.amap.api.maps.AMap
import com.amap.api.maps.model.Marker

/**
 * 自定义 InfoWindow
 * Created by MurphySL on 2017/7/8.
 */
class MyInfoWindow (
        private val context : Context,
        var title : String,
        val snippet : String) : AMap.InfoWindowAdapter{

    //监听自定义 InfoWindow 内容回调
    override fun getInfoContents(p0: Marker?): View? {
        return null
    }

    override fun getInfoWindow(p0: Marker?): View? {
        val infoWindows : View = LayoutInflater.from(context).inflate(R.layout.item_infowindow, null)

        val title_tv : TextView = infoWindows.findViewById(R.id.title)
        val snippet_tv : TextView = infoWindows.findViewById(R.id.snippet)

        title_tv.text = title
        snippet_tv.text = snippet

        return infoWindows
    }

}