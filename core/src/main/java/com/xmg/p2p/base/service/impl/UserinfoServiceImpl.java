package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.MailVerify;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.mapper.MailVerifyMapper;
import com.xmg.p2p.base.mapper.UserinfoMapper;
import com.xmg.p2p.base.service.ISystemDictionaryItemService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.service.IVerifyCodeService;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.DateUtils;
import com.xmg.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserinfoServiceImpl implements IUserinfoService {
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    IVerifyCodeService verifyCodeService;
    @Autowired
    private MailVerifyMapper mailVerifyMapper;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;

    @Override
    public int insert(Userinfo record) {
        return userinfoMapper.insert(record);
    }

    @Override
    public Userinfo get(Long id) {

        return userinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Userinfo record) {
        //更新数据添加乐观锁的处理
        int count = userinfoMapper.updateByPrimaryKey(record);
        if (count <= 0) {
            throw new RuntimeException("乐观锁处理出现异常");
        }
        return count;
    }

    @Override
    public void bindPhone(String phoneNumber, String verifyCode) {
        //先判断用户是否有绑定手机
        Userinfo userinfo = this.get(UserContext.getCurrent().getId());
        if (userinfo.getIsBindPhone()) {
            throw new RuntimeException("手机已经绑定,请不要重复绑定");
        }
        //校验手机是否和验证码相匹配
        boolean valid = verifyCodeService.validate(phoneNumber, verifyCode);
        if (valid) {
            //给用户手机绑定,更改认证的状态码
            userinfo.setPhoneNumber(phoneNumber);
            userinfo.addState(BitStatesUtils.OP_BIND_PHONE);
            this.update(userinfo);
        } else {
            throw new RuntimeException("手机验证证失败");
        }

    }

    @Override
    public void bindEamil(String key) {
        MailVerify mailVerify = mailVerifyMapper.selectByUUID(key);
        if (mailVerify == null) {
            throw new RuntimeException("邮件绑定的地址有误,请重新发送邮箱验证");
        } else if (DateUtils.getBetweenTime(mailVerify.getSendTime(), new Date()) > 5 * 24 * 60 * 60) {
            throw new RuntimeException("邮件验证已经超过有效期,请重新进行验证");
        }
        //无异常时把邮箱绑定到用户中
        //获取到发送邮件对应的用户
        Userinfo userinfo = this.get(mailVerify.getUserInfoId());
        if (userinfo.getIsBindEmail()) {
            throw new RuntimeException("邮箱已经绑定,请不要重复绑定");
        }
        userinfo.setEmail(mailVerify.getEmail());
        userinfo.addState(BitStatesUtils.OP_BIND_EMAIL);
        this.update(userinfo);
    }

    @Override
    public Userinfo getCurrent() {
        return userinfoMapper.selectByPrimaryKey(UserContext.getCurrent().getId());
    }

    @Override
    public void basicInfoSave(Userinfo userinfo) {
        //先获取到当前登录用户 对应于userinfo
        Userinfo current=this.get(UserContext.getCurrent().getId());

        /*
        * 没有更新手机号 没有更新手机号没有更新手机号  比特么乱整了
        * */
        current.setEducationBackground(userinfo.getEducationBackground());
        current.setHouseCondition(userinfo.getHouseCondition());
        current.setKidCount(userinfo.getKidCount());
        current.setIncomeGrade(userinfo.getIncomeGrade());
        current.setMarriage(userinfo.getMarriage());
        //System.out.println(current.getPhoneNumber());
        //更改userinfo的状态码
        if(!current.getIsBasicInfo()){//判断一下用户是都加了状态码
           current.addState(BitStatesUtils.OP_BASIC_INFO);
        }
        //更新基本信息
        userinfoMapper.updateByPrimaryKey(current);
    }

}

