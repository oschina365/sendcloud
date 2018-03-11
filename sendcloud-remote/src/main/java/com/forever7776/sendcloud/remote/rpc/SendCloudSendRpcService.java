package com.forever7776.sendcloud.remote.rpc;


import com.forever7776.sendcloud.remote.common.vo.ResultVO;
import com.forever7776.sendcloud.remote.common.vo.SendCloudEmailParamVO;

/**
 * sendcloud发送邮件接口
 *
 * @author kz
 * @date 2018-03-11
 */
public interface SendCloudSendRpcService {

    /**
     * 发送邮件
     *
     * @param vo
     * @return
     */
    ResultVO sendEmail(SendCloudEmailParamVO vo);
}
