package androidlab.edu.cn.nucyixue.data.bean

import androidlab.edu.cn.nucyixue.utils.config.Config
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
        get() = getString(Config.LIVE_NAME)
        set(value) = put(Config.LIVE_NAME, value)

    var type : String
        get() = getString(Config.LIVE_TYPE)
        set(value) = put(Config.LIVE_TYPE, value)

    var star : Int
        get() = getInt(Config.LIVE_STAR)
        set(value) = put(Config.LIVE_STAR, value)

    var summary : String
        get() = getString(Config.LIVE_SUMMARY)
        set(value) = put(Config.LIVE_SUMMARY, value)

    var userId : String
        get() {
            return getAVObject<AVUser>(Config.LIVE_USER_ID).objectId
        }
        set(value) = put(Config.LIVE_USER_ID, AVObject.createWithoutData(Config.USER_TABLE, value))

    var username : String
        get() = getString(Config.LIVE_USER_NAME)
        set(value) = put(Config.LIVE_USER_NAME, value)

    var pic : String
        get() = getString(Config.LIVE_PIC)
        set(value) = put(Config.LIVE_PIC, value)

    var startAt : Date
        get() = getDate(Config.LIVE_START_AT)
        set(value) = put(Config.LIVE_START_AT, value)

    var price : Int
        get() = getInt(Config.LIVE_PRICE)
        set(value) = put(Config.LIVE_PRICE, value)

    var conversationId : String
        get() {
            return getAVObject<AVObject>(Config.LIVE_CONVERSATION_ID).objectId
        }
        set(value) = put(Config.LIVE_CONVERSATION_ID, value)

    override fun toString(): String {
        return """
            {
              objectId : $objectId ,
              name : $name ,
              type : $type ,
              star : $star ,
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
