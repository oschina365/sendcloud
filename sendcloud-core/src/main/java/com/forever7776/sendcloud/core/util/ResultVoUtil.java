package com.forever7776.sendcloud.core.util;


import com.forever7776.sendcloud.remote.common.enums.ResultEnum;
import com.forever7776.sendcloud.remote.common.vo.ResultVO;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author kz
 * @date 2018-01-18
 */
public class ResultVoUtil {

    public static ResultVO getSuccessResult(String msg) {
        ResultVO result = new ResultVO();
        result.setCode(ResultEnum.STATUS.SUCCESS.getCode());
        result.setMsg(ResultEnum.STATUS.SUCCESS.getDesc());
        if (StringUtils.isNotEmpty(msg)) {
            result.setMsg(msg);
        }

        return result;
    }

    public static ResultVO getFailResult(String msg) {
        ResultVO result = new ResultVO();
        result.setCode(ResultEnum.STATUS.FAIL.getCode());
        result.setMsg(ResultEnum.STATUS.FAIL.getDesc());
        if (StringUtils.isNotEmpty(msg)) {
            result.setMsg(msg);
        }

        return result;
    }
}
