package com.forever7776.sendcloud.remote.common.vo;

import com.forever7776.sendcloud.remote.common.enums.ContentTypeEnum;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sendcloud发送邮件参数VO
 *
 * @author kz
 * @date 2018-03-11
 */
public class SendCloudEmailParamVO {

    /**
     * 发件人
     */
    private String from;

    private String fromName;

    /**
     * 收件人列表
     */
    private List<String> to;

    /**
     * 抄送人列表
     */
    private List<String> cc;

    /**
     * 密送
     */
    private List<String> bcc;

    /**
     * 收件人回复邮件地址
     */
    private String replyTo;

    /**
     * 邮件标题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 本次发送所使用的标签ID
     */
    private Integer labelId;
    /**
     * 邮件头部信息
     */
    private Map<String, String> headers;
    /**
     * 邮件附件
     */
    private List<Object> attachments;

    /**
     * <pre>
     * SMTP 扩展字段 X-SMTPAPI 是 SendCloud 为开发者提供的邮件个性化定制的处理方式, 开发者通过这个特殊的 信头扩展字段,
     * 可以设置邮件处理方式的很多参数.
     *
     * SMTP 调用时, 开发者可以在邮件中自行插入各种头域信息, 这是 SMTP 协议所允许的. 而 SendCloud 会检索 key 为
     * X-SMTPAPI 的头域信息, 如果发现含有此头域, 则其 value 的值可以被解析, 用来改变邮件的处理方式.
     * </pre>
     */
    private Map<String, Object> xsmtpapi;

    /**
     * 是否使用模板
     */
    private boolean userTemplate;

    /**
     * 模板名称
     */
    private String templateInvokeName;

    /**
     * 邮件格式：text/html或者text/plain
     * <p>
     * 默认text/html
     * text/plain页面上原样显示这段代码
     */
    private ContentTypeEnum contentType = ContentTypeEnum.html;

    private boolean useAddressList = false;

    /**
     * 广播发送(收件人会全部显示)
     */
    public boolean broadcastSend = true;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }

    public Map<String, Object> getXsmtpapi() {
        return xsmtpapi;
    }

    public void setXsmtpapi(Map<String, Object> xsmtpapi) {
        this.xsmtpapi = xsmtpapi;
    }

    public boolean isUserTemplate() {
        return userTemplate;
    }

    public void setUserTemplate(boolean userTemplate) {
        this.userTemplate = userTemplate;
    }

    public String getTemplateInvokeName() {
        return templateInvokeName;
    }

    public void setTemplateInvokeName(String templateInvokeName) {
        this.templateInvokeName = templateInvokeName;
    }

    public ContentTypeEnum getContentType() {
        return contentType;
    }

    public void setContentType(ContentTypeEnum contentType) {
        this.contentType = contentType;
    }

    public boolean isUseAddressList() {
        return useAddressList;
    }

    public void setUseAddressList(boolean useAddressList) {
        this.useAddressList = useAddressList;
    }

    public boolean isBroadcastSend() {
        return broadcastSend;
    }

    public void setBroadcastSend(boolean broadcastSend) {
        this.broadcastSend = broadcastSend;
    }

    /**
     * 添加邮件头部信息
     *
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put(key, value);
    }

    /**
     * 添加附件
     *
     * @param file
     */
    public void addAttachments(File file) {
        if (attachments == null) {
            attachments = new ArrayList<Object>();
        }
        attachments.add(file);
    }

    /**
     * 添加附件
     *
     * @param urlPath 网络附近路径
     * @param name    附近名称
     */
    public void addAttachments(String urlPath, String name) throws Exception {
        if (attachments == null) {
            attachments = new ArrayList<Object>();
        }
        URL url = new URL(urlPath);
        BufferedInputStream stream = new BufferedInputStream(url.openStream());
        attachments.add(new Attachment(stream, name));
    }

    /**
     * 添加xsmtpapi
     *
     * @param key
     * @param value
     */
    public void addXsmtpapi(String key, Object value) {
        if (xsmtpapi == null) {
            xsmtpapi = new HashMap<String, Object>();
        }
        xsmtpapi.put(key, value);
    }

    public String getXsmtpapiString() {
        return JSONObject.fromObject(xsmtpapi).toString();
    }


    public String getHeadersString() {
        return JSONObject.fromObject(headers).toString();
    }

    public String getToString() {
        StringBuilder sb = new StringBuilder();
        for (String address : to) {
            sb.append(";");
            sb.append(address);
        }
        return sb.toString();
    }

    public String getCcString() {
        if (CollectionUtils.isEmpty(cc)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (String address : cc) {
            sb.append(";");
            sb.append(address);
        }
        return sb.toString();
    }

    public String getBccString() {
        if (CollectionUtils.isEmpty(bcc))
            return null;
        StringBuilder sb = new StringBuilder();
        for (String address : bcc) {
            sb.append(";");
            sb.append(address);
        }
        return sb.toString();
    }


}
