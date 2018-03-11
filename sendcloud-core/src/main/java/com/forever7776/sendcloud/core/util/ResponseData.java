package com.forever7776.sendcloud.core.util;

import net.sf.json.JSONObject;

/**
 * 返回数据
 *
 * @author kz
 * @date 2018-03-11
 */
public class ResponseData {

    public boolean result;
    public int statusCode;
    public String message;
    public String info;

    public String toString() {
        return JSONObject.fromObject(this).toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}