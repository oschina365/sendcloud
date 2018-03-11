package com.forever7776.sendcloud.remote.common.vo;

import java.io.Serializable;

/**
 * @author KZ
 * @date 2018-01-03
 */
public class ResultVO implements Serializable {
    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResultVO{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
