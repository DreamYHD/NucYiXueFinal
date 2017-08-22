package androidlab.edu.cn.nucyixue.utils.config

import android.support.annotation.IdRes
import androidlab.edu.cn.nucyixue.R

/**
 * Live Type
 *
 * Created by MurphySL on 2017/8/13.
 */
enum class LiveType(@IdRes val  icon : Int, val value : String){

    CS(R.drawable.live_type_cs_icon, "计算机"),
    EM(R.drawable.live_type_em_icon, "经济管理"),
    L(R.drawable.live_type_l_icon, "外语"),
    LAW(R.drawable.live_type_law_icon, "法学"),
    LS(R.drawable.live_type_ls_icon, "生命科学"),
    TECH(R.drawable.live_type_tech_icon, "工学"),
    SC(R.drawable.live_type_sc_icon, "理学"),
    HIS(R.drawable.live_type_his_icon, "文学历史"),
    ART(R.drawable.live_type_art_icon, "艺术设计"),
    MORE(R.drawable.more_icon, "其他"),
    PSY(R.drawable.hold, "心理学"),
    PH(R.drawable.hold, "哲学");

    companion object {
        @JvmStatic
        fun toList() : List<LiveType>{
            val list = ArrayList<LiveType>()
            LiveType.values().forEach {
                list += it
            }
            return list
        }

        @JvmStatic
        fun toValueList() : List<String>{
            val list = ArrayList<String>()
            LiveType.values().forEach {
                list += it.value
            }
            return list
        }
    }

}