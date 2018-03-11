package com.forever7776.sendcloud.core.service;

import com.forever7776.sendcloud.core.constants.Config;
import com.forever7776.sendcloud.core.constants.Credential;
import com.forever7776.sendcloud.core.exception.SmsException;
import com.forever7776.sendcloud.core.exception.VoiceException;
import com.forever7776.sendcloud.core.util.Md5Util;
import com.forever7776.sendcloud.core.util.ResponseData;
import com.forever7776.sendcloud.remote.common.enums.ContentTypeEnum;
import com.forever7776.sendcloud.remote.common.vo.Attachment;
import com.forever7776.sendcloud.remote.common.vo.SendCloudEmailParamVO;
import com.forever7776.sendcloud.remote.common.vo.SendCloudSmsParamVO;
import com.forever7776.sendcloud.remote.common.vo.SendCloudVoiceParamVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * sendcloud发送api
 *
 * @author kz
 * @date 2018-03-11
 */
public class SendCloudService {

    private static final Logger logger = LoggerFactory.getLogger(SendCloudService.class);

    /**
     * 发送邮件
     */
    public ResponseData sendMail(SendCloudEmailParamVO vo) throws Throwable {
        Credential credential = new Credential(Config.api_user, Config.api_key);
        if (CollectionUtils.isEmpty(vo.getAttachments())) {
            return post(credential, vo);
        } else {
            return multipartPost(credential, vo);
        }
    }

    /**
     * 普通方式发送
     *
     * @param credential
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    private ResponseData post(Credential credential, SendCloudEmailParamVO vo) throws ClientProtocolException, IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("apiUser", credential.getApiUser()));
        params.add(new BasicNameValuePair("apiKey", credential.getApiKey()));
        params.add(new BasicNameValuePair("from", vo.getFrom()));
        params.add(new BasicNameValuePair("fromName", vo.getFromName()));
        params.add(new BasicNameValuePair("subject", vo.getSubject()));
        params.add(new BasicNameValuePair("replyTo", vo.getReplyTo()));
        if (vo.getLabelId() != null) {
            params.add(new BasicNameValuePair("labelId", vo.getLabelId().toString()));
        }


        /**
         * 是否使用模版发送
         */
        if (vo.isUserTemplate()) {
            params.add(new BasicNameValuePair("templateInvokeName", vo.getTemplateInvokeName()));
        } else {
            if (vo.getContentType().equals(ContentTypeEnum.html)) {
                params.add(new BasicNameValuePair("html", vo.getContent()));
            } else {
                params.add(new BasicNameValuePair("plain", vo.getContent()));
            }
        }
        /**
         * 是否使用地址列表
         */
        if (vo.getTo() != null) {
            if (vo.isUseAddressList()) {
                params.add(new BasicNameValuePair("useAddressList", "true"));
                params.add(new BasicNameValuePair("to", vo.getToString()));
            } else {
                if (!vo.isUserTemplate() && vo.broadcastSend) {
                    params.add(new BasicNameValuePair("to", vo.getToString()));
                    params.add(new BasicNameValuePair("cc", vo.getCcString()));
                    params.add(new BasicNameValuePair("bcc", vo.getBccString()));
                } else {
                    if (vo.getXsmtpapi() != null && !vo.getXsmtpapi().containsKey("to")) {
                        vo.addXsmtpapi("to", JSONArray.fromObject(vo.getTo()));
                    }
                }
            }
        }
        if (MapUtils.isNotEmpty(vo.getHeaders())) {
            params.add(new BasicNameValuePair("headers", vo.getHeadersString()));
        }

        if (MapUtils.isNotEmpty(vo.getXsmtpapi())) {
            params.add(new BasicNameValuePair("xsmtpapi", vo.getXsmtpapiString()));
            params.add(new BasicNameValuePair("respEmailId", "true"));
            params.add(new BasicNameValuePair("useNotification", "false"));
        }


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(vo.isUserTemplate() ? Config.send_template_api : Config.send_api);
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        ResponseData result = validate(response);
        httpPost.releaseConnection();
        httpClient.close();
        logger.info("发送普通邮件成功，收件人：{}", vo.getToString());
        return result;
    }

    /**
     * multipart方式发送
     *
     * @param credential
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    private ResponseData multipartPost(Credential credential, SendCloudEmailParamVO vo)
            throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(vo.isUserTemplate() ? Config.send_template_api : Config.send_api);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.setCharset(Charset.forName("UTF-8"));
        ContentType TEXT_PLAIN = ContentType.create("text/plain", Charset.forName("UTF-8"));
        entity.addTextBody("apiUser", credential.getApiUser(), TEXT_PLAIN);
        entity.addTextBody("apiKey", credential.getApiKey(), TEXT_PLAIN);
        entity.addTextBody("from", vo.getFrom(), TEXT_PLAIN);
        if (StringUtils.isNotEmpty(vo.getFromName())) {
            entity.addTextBody("fromName", vo.getFromName(), TEXT_PLAIN);
        }
        entity.addTextBody("subject", vo.getSubject(), TEXT_PLAIN);
        if (StringUtils.isNotEmpty(vo.getReplyTo())) {
            entity.addTextBody("replyTo", vo.getReplyTo(), TEXT_PLAIN);
        }

        if (vo.getLabelId() != null) {
            entity.addTextBody("labelId", vo.getLabelId().toString(), TEXT_PLAIN);
        }

        /**
         * 是否使用模版发送
         */
        if (vo.isUserTemplate()) {
            entity.addTextBody("templateInvokeName", vo.getTemplateInvokeName(), TEXT_PLAIN);
        } else {
            if (vo.getContentType().equals(ContentTypeEnum.html)) {
                entity.addTextBody("html", vo.getContent(), TEXT_PLAIN);
            } else {
                entity.addTextBody("plain", vo.getContent(), TEXT_PLAIN);
            }
        }
        /**
         * 是否使用地址列表
         */
        if (vo.isUseAddressList()) {
            entity.addTextBody("useAddressList", "true", TEXT_PLAIN);
            entity.addTextBody("to", vo.getToString(), TEXT_PLAIN);
        } else {
            if (!vo.isUserTemplate() && vo.isBroadcastSend()) {
                entity.addTextBody("to", vo.getToString(), TEXT_PLAIN);
                if (StringUtils.isNotEmpty(vo.getCcString())) {
                    entity.addTextBody("cc", vo.getCcString(), TEXT_PLAIN);
                }

                if (StringUtils.isNotEmpty(vo.getBccString())) {
                    entity.addTextBody("bcc", vo.getBccString(), TEXT_PLAIN);
                }

            } else {
                if (vo.getXsmtpapi() == null || !vo.getXsmtpapi().containsKey("to")) {
                    vo.addXsmtpapi("to", JSONArray.fromObject(vo.getTo()));
                }
            }
        }
        if (MapUtils.isNotEmpty(vo.getHeaders())) {
            entity.addTextBody("headers", vo.getHeadersString(), TEXT_PLAIN);
        }

        if (MapUtils.isNotEmpty(vo.getXsmtpapi())) {
            entity.addTextBody("xsmtpapi", vo.getXsmtpapiString(), TEXT_PLAIN);
        }

        entity.addTextBody("respEmailId", "true", TEXT_PLAIN);
        entity.addTextBody("useNotification", "false", TEXT_PLAIN);

        ContentType OCTEC_STREAM = ContentType.create("application/octet-stream", Charset.forName("UTF-8"));
        for (Object o : vo.getAttachments()) {
            if (o instanceof File) {
                entity.addBinaryBody("attachments", (File) o, OCTEC_STREAM, ((File) o).getName());
            } else if (o instanceof Attachment) {
                entity.addBinaryBody("attachments", ((Attachment) o).getContent(), OCTEC_STREAM,
                        ((Attachment) o).getName());
            } else {
                entity.addBinaryBody("attachments", (InputStream) o, OCTEC_STREAM, UUID.randomUUID().toString());
            }
        }
        httpPost.setEntity(entity.build());
        HttpResponse response = httpClient.execute(httpPost);
        ResponseData result = validate(response);
        httpPost.releaseConnection();
        httpClient.close();
        logger.info("发送带附件邮件成功，收件人：{}", vo.getToString());
        return result;
    }

    /**
     * 发送短信
     *
     * @param vo
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws SmsException
     */
    public ResponseData sendSms(SendCloudSmsParamVO vo) throws ClientProtocolException, IOException, SmsException {
        Credential credential = new Credential(Config.sms_user, Config.sms_key);
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("smsUser", credential.getApiUser());
        treeMap.put("msgType", vo.getMsgType().toString());
        treeMap.put("phone", vo.getPhoneString());
        treeMap.put("templateId", vo.getTemplateId().toString());
        treeMap.put("timestamp", String.valueOf((new Date()).getTime()));
        if (MapUtils.isNotEmpty(vo.getVars())) {
            treeMap.put("vars", vo.getVarsString());
        }

        String signature = Md5Util.md5Signature(treeMap, credential.getApiKey());
        treeMap.put("signature", signature);
        Iterator<String> iterator = treeMap.keySet().iterator();
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        while (iterator.hasNext()) {
            String key = iterator.next();
            params.add(new BasicNameValuePair(key, treeMap.get(key)));
        }

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(Config.send_sms_api);
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpResponse response = httpclient.execute(httpPost);
        ResponseData result = validate(response);
        httpPost.releaseConnection();
        httpclient.close();
        logger.info("发送短信成功，收件人：{}", vo.getPhoneString());
        return result;
    }

    /**
     * 发送语音
     *
     * @param voice
     * @return
     * @throws VoiceException
     * @throws ParseException
     * @throws IOException
     */
    public ResponseData sendVoice(SendCloudVoiceParamVO voice) throws VoiceException, ParseException, IOException {
        Credential credential = new Credential(Config.sms_user, Config.sms_key);
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("smsUser", credential.getApiUser());
        treeMap.put("phone", voice.getPhone());
        treeMap.put("code", voice.getCode());
        treeMap.put("timestamp", String.valueOf((new Date()).getTime()));
        String signature = Md5Util.md5Signature(treeMap, credential.getApiKey());
        treeMap.put("signature", signature);
        Iterator<String> iterator = treeMap.keySet().iterator();
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        while (iterator.hasNext()) {
            String key = iterator.next();
            params.add(new BasicNameValuePair(key, treeMap.get(key)));
        }

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(Config.send_voice_api);
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        ResponseData result = validate(response);
        httpPost.releaseConnection();
        httpClient.close();
        logger.info("拨打语音短信成功，手机号码：{}，内容：{}", voice.getPhone(), voice.getCode());
        return result;
    }

    /**
     * 解析返回结果
     *
     * @param response
     * @return
     * @throws ParseException
     * @throws IOException
     */
    private ResponseData validate(HttpResponse response) throws ParseException, IOException {
        String s = EntityUtils.toString(response.getEntity());
        ResponseData result = new ResponseData();
        if (JSONUtils.mayBeJSON(s)) {
            JSONObject json = JSONObject.fromObject(s);
            if (json.containsKey("statusCode")) {
                result.setStatusCode(json.getInt("statusCode"));
                result.setMessage(json.getString("message"));
                result.setResult(json.getBoolean("result"));
                result.setInfo(json.getJSONObject("info").toString());
            } else {
                result.setStatusCode(500);
                result.setMessage(json.toString());
            }
        } else {
            result.setStatusCode(response.getStatusLine().getStatusCode());
            result.setMessage("发送失败");
            result.setResult(false);
        }
        return result;
    }
}