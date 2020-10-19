package pro.chenggang.project.exclusive_toolkit.common_support.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @classDesc:
 * @author: chenggang
 */
@Slf4j
public class ArchitectUtil {

    private static final Pattern UNDER_LINE_PATTERN = Pattern.compile("_(\\w)");

    private static final Pattern CAMEL_PATTERN = Pattern.compile("[A-Z]");

    /**
     * 手机号格式校验正则
     */
    public static final String PHONE_REGEX = "^1\\d{10}$";

    /**
     * 手机号脱敏筛选正则
     */
    public static final String PHONE_BLUR_REGEX = "(\\d{3})\\d{4}(\\d{4})";

    /**
     * 手机号脱敏替换正则
     */
    public static final String PHONE_BLUR_REPLACE_REGEX = "$1****$2";

    /**
     * 下划线转驼峰
     * @param str
     * @return
     */
    public static String camel(String str) {
        if(StringUtils.isBlank(str)){
            return str;
        }
        Matcher matcher = UNDER_LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if(matcher.find()) {
            sb = new StringBuffer();
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            matcher.appendTail(sb);
        }else {
            return sb.toString();
        }
        return camel(sb.toString());
    }


    /**
     * 驼峰转下划线
     * @param str
     * @return
     */
    public static String underline(String str) {
        if(StringUtils.isBlank(str)){
            return str;
        }
        Matcher matcher = CAMEL_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if(matcher.find()) {
            sb = new StringBuffer();
            matcher.appendReplacement(sb,"_"+matcher.group(0).toLowerCase());
            matcher.appendTail(sb);
        }else {
            return sb.toString();
        }
        return underline(sb.toString());
    }

    /**
     * 检查手机号格式
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        return phone.matches(PHONE_REGEX);
    }

    /**
     * 手机号加密
     * @param phone
     * @return
     */
    public static String phoneEncrypt(String phone) {
        boolean checkFlag = checkPhone(phone);
        if (!checkFlag) {
            return phone;
        }
        return phone.replaceAll(PHONE_BLUR_REGEX, PHONE_BLUR_REPLACE_REGEX);
    }

}
