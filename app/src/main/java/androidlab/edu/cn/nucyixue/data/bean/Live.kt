package androidlab.edu.cn.nucyixue.data.bean

import androidlab.edu.cn.nucyixue.utils.config.LCConfig
import com.avos.avoscloud.AVClassName
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVUser
import java.util.*

/**
 * Live
 *
 * Created by MurphySL on 2017/7/26.
 */
@AVClassName("Live")
class Live : AVObject(){

    companion object {
        val CREATOR : AVObjectCreator = AVObjectCreator.instance
    }

    var name : String
        get() = getString(LCConfig.LIVE_NAME)
        set(value) = put(LCConfig.LIVE_NAME, value)

    var num : Int
        get() = getInt(LCConfig.LIVE_NUM)
        set(value) = put(LCConfig.LIVE_NUM, value)

    var type : String
        get() = getString(LCConfig.LIVE_TYPE)
        set(value) = put(LCConfig.LIVE_TYPE, value)

    var star : Int
        get() = getInt(LCConfig.LIVE_STAR)
        set(value) = put(LCConfig.LIVE_STAR, value)

    var summary : String
        get() = getString(LCConfig.LIVE_SUMMARY)
        set(value) = put(LCConfig.LIVE_SUMMARY, value)

    var userId : String
        get() = getAVObject<AVUser>(LCConfig.LIVE_USER_ID).objectId
        set(value) = put(LCConfig.LIVE_USER_ID, AVObject.createWithoutData(LCConfig.USER_TABLE, value))

    var username : String
        get() = getString(LCConfig.LIVE_USER_NAME)
        set(value) = put(LCConfig.LIVE_USER_NAME, value)

    var pic : String
        get() = getString(LCConfig.LIVE_PIC)
        set(value) = put(LCConfig.LIVE_PIC, value)

    var startAt : Date
        get() = getDate(LCConfig.LIVE_START_AT)
        set(value) = put(LCConfig.LIVE_START_AT, value)

    var price : Int
        get() = getInt(LCConfig.LIVE_PRICE)
        set(value) = put(LCConfig.LIVE_PRICE, value)

    var conversationId : String
        get() = getAVObject<AVObject>(LCConfig.LIVE_CONVERSATION_ID).objectId
        set(value) = put(LCConfig.LIVE_CONVERSATION_ID, value)

    override fun toString(): String {
        return """
            {
              objectId : $objectId ,
              name : $name ,
              type : $type ,
              star : $star ,
              num  : $num,
              summary : $summary ,
              userId : $userId ,
              pic : $pic ,
              price : $price ,
              startAt : $startAt ,
              conversationId : $conversationId
            }
        """
    }

}
