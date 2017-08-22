package androidlab.edu.cn.nucyixue.utils.config

/**
 * LeanCloud Config
 *
 * Created by MurphySL on 2017/8/20.
 */
class LCConfig {

    companion object {
        @JvmStatic
        val USER_TABLE : String= "_User" // 用户表
        @JvmStatic
        val CONVERSATION_TABLE : String = "_Conversation" // 会话表
        @JvmStatic
        val LIVE_TABLE: String = "Live" // Live 表
        @JvmStatic
        val LU_TABLE : String = "LU" // Live - User 关联表
        @JvmStatic
        val UI_TABLE : String = "UserInfo"

        @JvmStatic
        val USER_AVATAR : String = "avatar" // 用户头像

        @JvmStatic
        val LIVE_ID : String = "objectId" // Live ID
        @JvmStatic
        val LIVE_USER_ID: String = "userId" // 文字 Live 用户ID
        @JvmStatic
        val LIVE_USER_NAME : String = "username" // 用户姓名
        @JvmStatic
        val LIVE_CONVERSATION_ID: String = "conversationId" // 文字 Live 会话ID
        @JvmStatic
        val LIVE_NAME: String = "name" // 文字 Live 主题
        @JvmStatic
        val LIVE_STAR: String = "star" // 文字 Live 评价星级
        @JvmStatic
        val LIVE_SUMMARY: String = "summary" // 文字 Live 简介
        @JvmStatic
        val LIVE_PRICE: String = "price" // 文字 Live 价格
        @JvmStatic
        val LIVE_TYPE: String = "type" // 文字 Live 类型
        @JvmStatic
        val LIVE_PIC: String = "pic" // 文字 Live 封面
        @JvmStatic
        val LIVE_START_AT: String = "startAt" // 文字 Live 开始时间
        @JvmStatic
        val LIVE_NUM : String = "num" // 文字 Live 参加人数

        @JvmStatic
        val LU_STAR : String = "star" // 星级
        @JvmStatic
        val LU_COMMENT : String = "comment" // 评论
        @JvmStatic
        val LU_LIVE_ID : String = "liveId" // Live ID
        @JvmStatic
        val LU_USER_ID : String = "userId" // 用户 ID


        @JvmStatic
        val UI_USER_ID : String = "userId" // 用户 ID
        @JvmStatic
        val UI_USER_NAME : String = "username" // 用户姓名
        @JvmStatic
        val UI_AVATAR : String = "avatar" // 用户头像

        @JvmStatic
        val LIVE_SOUNDS_CHANGE = "change"
        @JvmStatic
        val LIVE_SOUNDS_NEXT = "next"
        @JvmStatic
        val LIVE_SOUNDS_PREVIOUS = "previous"

    }
}