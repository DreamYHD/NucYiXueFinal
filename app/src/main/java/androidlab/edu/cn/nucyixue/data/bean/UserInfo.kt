package androidlab.edu.cn.nucyixue.data.bean

import androidlab.edu.cn.nucyixue.utils.config.Config
import com.avos.avoscloud.AVClassName
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVUser

/**
 * Created by MurphySL on 2017/8/21.
 */
@AVClassName("UserInfo")
class UserInfo : AVObject(){

    companion object {
        val CREATOR : AVObjectCreator = AVObjectCreator.instance
    }

    var userId : String
        get() {
            return getAVObject<AVUser>(Config.UI_USER_ID).objectId
        }
        set(value) = put(Config.UI_USER_ID, AVObject.createWithoutData(Config.USER_TABLE, value))

    var username : String
        get() = getString(Config.UI_USER_NAME)
        set(value) = put(Config.UI_USER_NAME, value)

    var avatar : String
        get() = getString(Config.UI_AVATAR)
        set(value) = put(Config.UI_AVATAR, value)

    override fun toString(): String {
        return """
            userId : $userId,
            username : $username
            avatar : $avatar
        """
    }

}