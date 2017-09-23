package androidlab.edu.cn.nucyixue.utils.config

/**
 * Created by dreamY on 2017/9/20.
 */
class MTConfig{

    companion object {
        @JvmStatic
        val TABLE_NAME : String = "MapTeam" // 表名称

        @JvmStatic
        val TABLE_ID : String = "objectId" // 小组 ID
        @JvmStatic
        val TEAM_LOCATION_LAT = "latitude"// 位置经度
        @JvmStatic
        val TEAM_LOCATION_LONG ="longitude" //位置纬度

        @JvmStatic
        val TEAM_NAME : String = "name" // 小组名称
        @JvmStatic
        val TEAM_NUM : String = "num" // 小组人数
        @JvmStatic
        val TEAM_START_TIME : String = "startTime" // 小组开始时间
        @JvmStatic
        val TEAM_DESCRIPTION : String = "description" // 小组目标
        @JvmStatic
        val TEAM_PEOPLE : String = "people" // 小组成员列表
        @JvmStatic
        val TEAM_LOCATION : String = "location" // 小组成立位置
        @JvmStatic
        val TEAM_LEADER: String = "leader" //小组组长
        @JvmStatic
        val TEAM_END_TIME : String = "endTime"  // 小组结束时间
        @JvmStatic
        val TEAM_IMAGES : String = "images"  //小组活动结束上传的照片
        @JvmStatic
        val TEAM_IS_START: String = "isStart" //小组是否开始



    }


}