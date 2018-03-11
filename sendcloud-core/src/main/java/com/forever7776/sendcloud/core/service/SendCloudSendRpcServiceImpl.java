package com.forever7776.sendcloud.core.service;

import com.forever7776.sendcloud.core.util.ResultVoUtil;
import com.forever7776.sendcloud.remote.common.vo.ResultVO;
import com.forever7776.sendcloud.remote.common.vo.SendCloudEmailParamVO;
import com.forever7776.sendcloud.remote.rpc.SendCloudSendRpcService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * sendcloud发送邮件实现
 *
 * @author kz
 * @date 2018-03-11
 */
public class SendCloudSendRpcServiceImpl implements SendCloudSendRpcService {

    @Autowired
    private SendCloudService sendCloudService;

    /**
     * 发送邮件
     *
     * @param vo
     * @return
     */
    public ResultVO sendEmail(SendCloudEmailParamVO vo) {
        try {
            sendCloudService.sendMail(vo);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return ResultVoUtil.getSuccessResult("发送邮件成功");
    }
}
