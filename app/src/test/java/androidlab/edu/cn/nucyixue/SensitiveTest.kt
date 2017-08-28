package androidlab.edu.cn.nucyixue

import androidlab.edu.cn.nucyixue.utils.SensitiveFilter
import org.junit.Test

/**
 * 敏感词测试
 *
 * Created by MurphySL on 2017/8/28.
 */
class SensitiveTest{

    @Test
    fun filerTest(){
        val filter = SensitiveFilter()
        filter.addWord("算法")

        println(filter.filter("我爱算法"))
    }
}