package androidlab.edu.cn.nucyixue.utils

/**
 * 敏感词过滤
 *
 * Created by MurphySL on 2017/8/28.
 */
class SensitiveFilter{

    private class Node{
        var  end  = false
        private val subNodes = HashMap<Char , Node>()

        fun addNode(key : Char, node : Node) = subNodes.put(key, node)

        fun getNode(key : Char) : Node? = subNodes[key]
    }

    private val root : Node = Node()

    private fun isSymbol(c: Char): Boolean {
        val ic = c.toInt()
        // 0x2E80-0x9FFF 汉字的unicode码区间
        return ic < 0x2E80 || ic > 0x9FFF
    }

    fun filter(origin : String) : String{
        if(origin.isNotEmpty()){
            val result = StringBuilder()

            var node : Node? = root
            var begin = 0
            var position = 0

            while(position < origin.length){
                val c = origin[position]
                if(isSymbol(c)){
                    if(node == root){
                        result.append(c)
                        ++ begin
                    }

                    ++ position
                    continue
                }

                node = node?.getNode(c)

                // 当前位置的匹配结束
                if (node == null) {
                    // 以begin开始的字符串不存在敏感词
                    result.append(origin[begin])
                    // 跳到下一个字符开始测试
                    position = begin + 1
                    begin = position
                    // 回到树初始节点
                    node = root
                } else if (node.end) {
                    // 发现敏感词， 从begin到position的位置用replacement替换掉
                    result.append("■■")
                    position += 1
                    begin = position
                    node = root
                } else {
                    ++position
                }
            }

            result.append(origin.substring(begin))

            return result.toString()
        }else{
            return origin
        }
    }

    fun addWord(key : String){
        var temp = root

        for(i in 0 until key.length){
            val c = key[i]
            var node = temp.getNode(c)

            if(node == null){
                node = Node()
                temp.addNode(c, node)
            }

            temp = node

            if(i == key.length - 1){
                temp.end = true
            }
        }
    }
}