package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.service.IVerifyCodeService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.DateUtils;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.base.vo.VerifyCodeVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService {

    @Override
    public void sendVerifyCode(String phoneNumber) {
        //判断之前是否已经发送过短信
        VerifyCodeVo vo = UserContext.getVerifyCodeVo();
        if (vo == null || DateUtils.getBetweenTime(vo.getSendTime(), new Date()) > 90) {//仅仅当是第一次发送或者时间超过90s才可以继续发送短信  后者是在前者不为空的基础上取值尅拿到

            //生成随机验证码
            String verifyCode = UUID.randomUUID().toString().substring(0, 4);//前闭后开
            StringBuilder message = new StringBuilder(50);
            message.append("你的验证码是:")
                    .append(verifyCode)
                    .append(",有效期为")
                    .append(BidConst.MESSAGE_VALID_TIME)
                    .append("分钟,请在有效期内使用");
            System.out.println(message);
            //建一个vo对象将验证码发送时间 手机号还有验证码存储起来 和用户输入的作对比 验证结果
            vo = new VerifyCodeVo();
            vo.setPhoneNUmber(phoneNumber);
            vo.setSendTime(new Date());
            vo.setVerifyCode(verifyCode);
            //将vo保存进session
            UserContext.setVerifyCode(vo);
        } else {//其余情况抛出异常
            throw new RuntimeException("你的验证码发送过于频繁,请稍后再试");

        }
    }

    @Override
    public boolean validate(String phoneNumber, String verifyCode) {
        VerifyCodeVo vo = UserContext.getVerifyCodeVo();
        if (vo != null &&
                vo.getPhoneNUmber().equals(phoneNumber) &&
                vo.getVerifyCode().equals(verifyCode) &&
                DateUtils.getBetweenTime(vo.getSendTime(), new Date()) <= BidConst.MESSAGE_VALID_TIME * 60
                ) {

            return true;
        }
        return false;
    }
}
