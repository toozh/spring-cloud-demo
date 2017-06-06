package com.ifitmix.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * CJK中文汉字正则表达式
     */
    public static final String  CHINESE_CJK_REG = "[\\u4E00-\\u9FBF]+";

    /**
     * 将指定字符串从源编码转到新编码
     *
     * @param text           字符串
     * @param charsetName    原编码
     * @param newCharsetName 新编码
     * @return 新字符串
     */
    public static String newString(String text, String charsetName, String newCharsetName) {
        if (text == null) {
            return null;
        }
        try {
            return new String(text.getBytes(charsetName), newCharsetName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检测是否有emoji字符
     *
     * @param source 字符串
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (StringUtils.isBlank(source)) {
            return false;
        }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) { //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }
        return false;
    }

    /**
     * 检测字符是不是Emoji
     *
     * @param codePoint 字符
     * @return 是否
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source 字符串
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source) {
        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        //到这里铁定包含
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            }
        }
        if (buf == null) {
            return source;//如果没有找到 emoji表情，则返回源字符串
        } else {
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                return source;
            } else {
                return buf.toString();
            }
        }
    }

    /**
     * 截取字符串
     * @param content 要截取的内容
     * @param offset 起始index
     * @param maxWidth 截取的长度
     * @return 截取后的内容
     */
    public static String abbreviate(String content, int offset, int maxWidth) {
        return  StringUtils.abbreviate(content, offset, maxWidth);
    }

    /**
     * 截取字符串
     * @param content 要截取的内容
     * @param maxWidth 截取的长度
     * @return 截取后的内容
     */
    public static String abbreviate(String content, int maxWidth) {
        return  StringUtils.abbreviate(content, maxWidth);
    }

    /**
     * 字符串是否为空
     * 默认情况下使用这个
     * 当需要空格也过滤掉时，调用方法isEmpty("字符串", true);
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return isEmpty(str, false);
    }

    /**
     * 字符串是否为空
     *
     * @param str 字符串
     * @param ignoreWhitespace 是否认为空格不算字符。比如, "   "也算空串
     * @return 是否空串
     */
    public static boolean isEmpty(CharSequence str, boolean ignoreWhitespace) {
        return ignoreWhitespace ? !org.springframework.util.StringUtils.hasText(str)
                : StringUtils.isEmpty(str);
    }

    /**
     * 校验字符是否为中文字符<br />
     *      1.支持检验：中文汉字<br />
     *      2.支持检验：中文符号
     *
     * @param c 字符
     * @return  turn - 是中文字符
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    /**
     *  校验字符串是否为中文字符串<br />
     *      1.支持检验：中文汉字<br />
     *      2.支持检验：中文符号
     *
     * @param str 字符串
     * @return TRUE - 该字符串包含中文汉字及符号
     */
    public static boolean isChinese(String str) {
        char[] ch = str.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     *  校验字符串是否为中文字符串<br />
     *      1.校验部分CJK字符<br />
     *      2.cjk代号意思是：汉语（Chinese）、日语（Japanese）、韩语（Korean）<br />
     *      3.支持校验：中文汉字<br />
     *      4.不支持校验：中文符号
     *
     * @param str 字符串
     * @return
     */
    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(CHINESE_CJK_REG);
        return pattern.matcher(str.trim()).find();
    }


    ///
    /// String Regex

    public static List<String> regex(String context, String regex) {
        List regexList = Lists.newArrayList();
        Matcher matcher = getMatcher(context, regex);
        while (matcher.find()) {
            regexList.add(matcher.group());
        }
        return regexList;
    }


    public static String regexFirst(String context, String regex) {
        Matcher matcher = getMatcher(context, regex);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static Matcher getMatcher(String context, String regex) {
        return Pattern.compile(regex).matcher(context);
    }

}
