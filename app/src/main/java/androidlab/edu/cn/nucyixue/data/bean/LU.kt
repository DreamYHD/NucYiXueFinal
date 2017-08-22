package androidlab.edu.cn.nucyixue.data.bean

import androidlab.edu.cn.nucyixue.utils.config.LCConfig
import com.avos.avoscloud.AVClassName
import com.avos.avoscloud.AVObject

/**
 * LU
 *
 * Created by MurphySL on 2017/7/27.
 */
@AVClassName("LU")
class LU : AVObject(){
    companion object {
        val CREATOR : AVObjectCreator = AVObjectCreator.instance
    }

    var star : Int
        get() = getInt(LCConfig.LU_STAR)
        set(value) = put(LCConfig.LU_STAR, value)

    var comment : String?
        get() = getString(LCConfig.LU_COMMENT)
        set(value) = put(LCConfig.LU_COMMENT, value)

    var liveId : String
        get() {
            return getAVObject<AVObject>(LCConfig.LU_LIVE_ID).objectId
        }
        set(value) = put(LCConfig.LU_LIVE_ID, AVObject.createWithoutData(LCConfig.LIVE_TABLE, value))

    var userId : String
        get() {
            return getAVObject<AVObject>(LCConfig.LU_USER_ID).objectId
        }
        set(value) = put(LCConfig.LU_USER_ID, AVObject.createWithoutData(LCConfig.USER_TABLE, value))

    override fun toString(): String {
        return """
            {
              objectId : $objectId ,
              liveId : $liveId ,
              userId : $userId ,
              name : $comment ,
              star : $star
            }
        """
    }
}