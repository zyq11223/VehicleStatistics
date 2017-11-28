package com.fhzz.core.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isNullOREmpty(String args) {
        if (args == null || args.trim().equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        }
        Pattern regex = Pattern
                .compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
        Matcher matcher = regex.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是字母和数字的结�?
     *
     * @param name
     * @return
     */
    public static boolean isAsciiOrDigit(String name) {
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (!isAscii(ch))
                return false;
        }
        return true;
    }

    public static boolean isDigit(String name) {
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (!isDigit(ch))
                return false;
        }
        return true;
    }

    public static boolean isAscii(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                || (ch >= '0' && ch <= '9');
    }

    public static boolean isDigit(char ch) {
        return (ch >= '0' && ch <= '9');
    }

    /**
     * 获取字符长度，一个汉字作�? 1 个字�?, �?个英文字母作�? 0.5 个字�?
     *
     * @param text
     * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
     */
    public static int getLength(String text) {
        int textLength = text.length();
        int length = textLength;
        for (int i = 0; i < textLength; i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
                length++;
            }
        }
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;
    }

    public static boolean suffixValidate(String fileName, String[] suffixs) {
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            String extend = fileName.substring(i).toUpperCase();
            StringBuffer suffixsBf = new StringBuffer();
            for (int j = 0; j < suffixs.length; j++) {
                suffixsBf.append(suffixs[j]);
            }
            if (suffixsBf.toString().toUpperCase().indexOf(extend) != -1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 将str转成成数据库字段名如userId-->USER_ID
     *
     * @param str
     * @return
     */
    public static String getColumnByAttr(String str) {
        int u = -1;
        for (int i = 0; i < str.length(); i++) {
            char tempC = str.charAt(i);
            if (String.valueOf(tempC).matches("[A-Z]")) {
                u = i;
                if (i == 0) {
                    continue;
                } else {
                    break;
                }
            }
        }

        if (u > 0) {
            return str.substring(0, u).toUpperCase() + "_"
                    + getColumnByAttr(str.substring(u));
        } else {
            return str.toUpperCase();
        }
    }


    /**
     * 判断第一个字符串是否大于第二个
     *
     * @param str
     * @param otherStr
     * @return
     */
    public static Boolean isSmallThan(String str, String otherStr) {

        // 最小的长度
        int size = str.length() < otherStr.length() ? str.length() : otherStr
                .length();

        for (int i = 0; i < size; i++) {
            char ch = str.charAt(i);
            char chOther = otherStr.charAt(i);
            if (ch < chOther)
                return true;
        }
        if (str.length() > size)
            return false ;
        else if(str.length()>=otherStr.length())
            return false;
        else
            return true;

    }

    /**
     * @param str
     * @param otherStr
     * @return str在otherStr中的数量
     */
    public static int countStrInOtherStr(String str, String otherStr) {
        if (otherStr == null || str == null || str.length() == 0) {
            return 0;
        }
        int count = 0;
        for (int index = otherStr.indexOf(str); index >= 0; count++) {
            index = otherStr.indexOf(str, index + str.length());
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println (isSmallThan("2017-01-201","2017-01-20"));
        System.out.println (isSmallThan("2017-01-20","2017-01-201"));
        System.out.println (isSmallThan("2017-01-20","2017-01-21"));
        System.out.println (isSmallThan("2017-01-21","2017-01-20"));
        System.out.println (isSmallThan("2017-01-20","2017-01-20"));
        System.out.println (isSmallThan("2017-02-20","2017-01-20"));
        System.out.println (isSmallThan("2017-01-20","2017-02-20"));
        System.out.println (isSmallThan("2018-01-20","2017-01-20"));
        System.out.println (isSmallThan("2017-01-20","2018-01-20"));
    }
}
