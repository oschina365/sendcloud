package com.forever7776.sendcloud.core.constants;

import static com.forever7776.sendcloud.core.util.ConfigTool.getProp;

/**
 * sendcloud配置
 * @author kz
 * @date 2018-03-11
 */
public class Config {

    public static final String CHARSET = "utf-8";

    public static String server = "http://www.sendcloud.net";
    /**
     * 普通邮件发送
     */
    public static String send_api = "http://api.sendcloud.net/apiv2/mail/send";
    /**
     * 地址列表发送
     */
    public static String send_template_api = "http://api.sendcloud.net/apiv2/mail/sendtemplate";
    /**
     * 短信发送
     */
    public static String send_sms_api = "http://www.sendcloud.net/smsapi/send";
    /**
     * 语音发送
     */
    public static String send_voice_api = "http://www.sendcloud.net/smsapi/sendVoice";
    /**
     * 邮件user
     */
    public static String api_user = getProp("sendcloud_api_user");
    /**
     * 邮件key
     */
    public static String api_key = getProp("sendcloud_api_key");
    /**
     * 短信user
     */
    public static String sms_user = getProp("sendcloud_sms_user");
    /**
     * 短信key
     */
    public static String sms_key = getProp("sendcloud_sms_key");
    /**
     * 最大收件人数
     */
    public static final int MAX_RECEIVERS = 100;
    /**
     * 最大地址列表数
     */
    public static final int MAX_MAILLIST = 5;
    /**
     * 邮件内容大小
     */
    public static final int MAX_CONTENT_SIZE = 1024 * 1024;

}