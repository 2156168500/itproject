package com.fjh.commity.util;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static  final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    //用来替换敏感词汇的的字符串
    private static  final  String REPLACE = "***";
    //前缀树的根节点
    private TireNode rootNode = new TireNode();
    //在这个类被加载在Spring容器之后执行的方法，用来初始化前缀树
    @PostConstruct
    private void init()  {
        //首先获取敏感词文件的输入流
        //利用class文件的路径
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
        if(is == null){
            logger.error("is == " + is);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String keyword = "";
        try {
            while ((keyword = bufferedReader.readLine()) != null) {
                //将敏感词汇放入前缀树中
                putWord(keyword);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用来生成过滤之后的文本信息
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        //需要三个指针，
        //第一个指针用来表示当前来到的前缀树的节点
        TireNode moveNode = rootNode;//默认是在前缀树的根节点
        //第二个指针表示，要判断的字符串的开始的位置
        int begin = 0;
        //第三个指针表示，要判断的字符串的结束的位置
        int end = 0;
        //用一个可变的字符串表示 过滤之后的字符串
        StringBuilder stringBuilder = new StringBuilder("");
        while(end < text.length()){//没有来到最后一个字符之前
            char ch = text.charAt(end);
            //对于任意一个字符来说，可能是一些符号，也可能不是符号，那么如果遇到的符号就跳过
            if(isSymbol(ch)){//如果是符号位就直接跳过
                if(moveNode == rootNode){
                    stringBuilder.append(ch);
                    begin++;
                    end = begin;
                }else {
                    end++;
                }
                continue;
            }
            //来到这就不是一个字符
            TireNode subNode = moveNode.getSubNode(ch);
           // moveNode = subNode;
            if(subNode == null){//说明这个字符不是敏感词的部分
                stringBuilder.append(ch);
                begin++;
                end = begin;
                moveNode = rootNode;
            }else if(subNode.isEnd()){//说明这个词汇是一个敏感词汇
                stringBuilder.append(REPLACE);
                moveNode = rootNode;
                end++;
                begin = end;
            }else {//说明当前字符是敏感字符的部分，但是不确实是不敏感字符
                end++;
                moveNode = subNode;
            }
        }
        return stringBuilder.toString();

    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //将敏感词汇放入前缀树中
    private void putWord(String keyWord){
        TireNode tempNode = rootNode;
        for(int i = 0; i < keyWord.length() ; i++){
            char ch = keyWord.charAt(i);
            TireNode subNode = tempNode.getSubNode(ch);
            if(subNode == null){//说明当前字符没有被加入到前缀树里去
                subNode = new TireNode();
                tempNode.addSubNode(ch,subNode);
            }
            tempNode = subNode;
            if(i == keyWord.length() -1) {
                //如果是字符串的结束，就设为true
                tempNode.setEnd(true);
            }
        }
    }

    /**
     * 用来存储敏感词的前缀树
     */
    private static class TireNode{
        //当前的字符是不是一个字符串的结尾
        private boolean isEnd;
        //它的子节点是什么
        private Map<Character,TireNode> subNodes = new HashMap<>();
        boolean isEnd() {
            return isEnd;
        }

        void setEnd(boolean end) {
            isEnd = end;
        }
        //在前缀树中添加一个节点
        void addSubNode(Character ch, TireNode tireNode){
            this.subNodes.put(ch,tireNode);
        }
        //在前缀树中获取一个节点
         TireNode getSubNode(Character ch){
            return subNodes.get(ch);
        }
    }
}
