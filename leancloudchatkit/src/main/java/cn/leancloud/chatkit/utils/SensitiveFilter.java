package cn.leancloud.chatkit.utils;

import com.leon.lfilepickerlibrary.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by MurphySL on 2017/8/28.
 */

public class SensitiveFilter {// 前赘树

    /**
     * 默认敏感词替换符
     */
    private static final String DEFAULT_REPLACEMENT = "敏感词";


    private class TrieNode {

        /**
         * true 关键词的终结 false 继续
         */
        private boolean end = false;

        /**
         * key下一个字符，value是对应的节点
         */
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        /**
         * 向指定位置添加节点树
         */
        void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        /**
         * 获取下个节点
         */
        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        boolean isKeyWordEnd() {
            return end;
        }

        void setKeyWordEnd(boolean end) {
            this.end = end;
        }

    }


    /**
     * 根节点
     */
    private TrieNode rootNode = new TrieNode();


    /**
     * 判断是否是一个符号
     */
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 汉字的unicode码区间
        return ic < 0x2E80 || ic > 0x9FFF;
    }


    /**
     * 过滤敏感词
     */
    public String filter(String text) {
        if (Objects.equals(text, " ")) {
            return text;
        }
        String replacement = DEFAULT_REPLACEMENT;
        StringBuilder result = new StringBuilder();

        TrieNode tempNode = rootNode;// 树指针，根节点
        int begin = 0; // 回滚数，字符定位结点
        int position = 0; // 子字符，当前比较的位置

        while (position < text.length()) {
            char c = text.charAt(position);
            // 符号直接跳过
            if (isSymbol(c)) {
                if (tempNode == rootNode) {// 如果是根节点，即不在过滤节点树下
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            // Map代表树。TrieNode为节点
            tempNode = tempNode.getSubNode(c);// 底层调用Map对象来查询是否有这个字符节点

            // 当前位置的匹配结束
            if (tempNode == null) {
                // 以begin开始的字符串不存在敏感词
                result.append(text.charAt(begin));
                // 跳到下一个字符开始测试
                position = begin + 1;
                begin = position;
                // 回到树初始节点
                tempNode = rootNode;
            } else if (tempNode.isKeyWordEnd()) {
                // 发现敏感词， 从begin到position的位置用replacement替换掉
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }

        result.append(text.substring(begin));// 加上末尾

        return result.toString();
    }

    // 增加关键词 色情
    public void addWord(String lineTxt) {
        TrieNode tempNode = rootNode;
        // 循环每个字符
        for (int i = 0; i < lineTxt.length(); ++i) {
            Character c = lineTxt.charAt(i);
            // 过滤符号
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);

            if (node == null) { // 没初始化
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }

            tempNode = node;// 移位当前节点

            if (i == lineTxt.length() - 1) {
                // 关键词结束，设置结束标志
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    public static void main(String[] argv) {
        SensitiveFilter s = new SensitiveFilter();
        s.addWord("色情");
        s.addWord("好色");
    }
}
