package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.IpLog;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.mapper.LoginInfoMapper;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IIpLogService;
import com.xmg.p2p.base.service.ILoginInfoService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.MD5;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.ExpAccount;
import com.xmg.p2p.business.service.IExpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoginInfoServiceImpl implements ILoginInfoService {
    @Autowired
    LoginInfoMapper loginInfoMapper;
    @Autowired
    IUserinfoService userinfoService;
    @Autowired
    IAccountService accountService;
    @Autowired
    IIpLogService ipLogService;
    @Autowired
    IExpAccountService expAccountService;

    public void register(String username, String password) {
        //1先根据用户查询数据库 能获取到数据则说明用户存在
        int count = loginInfoMapper.queryByUsername(username);
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }
        //2若查询不到数据则将传入的数据保存入库
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setUserType(LoginInfo.USERTYPE_USER);
        loginInfo.setPassword(MD5.encode(password));
        loginInfo.setState(LoginInfo.STATE_NORMAL);
        loginInfoMapper.insert(loginInfo);

        //注册完事之后初始化userinfo和account
        Userinfo userinfo = new Userinfo();
        //用户基本信息的id 来自于注册用户
        userinfo.setId(loginInfo.getId());
        userinfoService.insert(userinfo);

        //创建体验金用户
        ExpAccount expAccount=new ExpAccount();
        expAccount.setId(loginInfo.getId());
        expAccountService.save(expAccount);
        //发放体验金
        this.expAccountService.grantExpMoney(expAccount.getId(),new IExpAccountService.LastTime(1,IExpAccountService.LastTimeUnit.MONTH),
                BidConst.REGISTER_GRANT_EXPMONEY,BidConst.EXPMONEY_TYPE_REGISTER );

        Account account = new Account();
        account.setId(loginInfo.getId());
        accountService.insert(account);

    }

    public boolean checkUsername(String username) {
        int count = loginInfoMapper.queryByUsername(username);
        System.out.println(count);
        if (count > 0) {
            return false;
        }
        return true;
    }

    //需要返回一个LoginInfo对象
    //public void login(String username, String password) {

    public LoginInfo login(String username, String password,int userType) {
        //根据用户名和密码查询数据 如果有数据则登陆成功
        LoginInfo loginInfo = loginInfoMapper.selectByNameAndPassword(username, MD5.encode(password),userType);
        UserContext.setCurrent(loginInfo);
        IpLog ipLog = new IpLog();
        ipLog.setUsername(username);
        ipLog.setLoginTime(new Date());
        ipLog.setIp(UserContext.getIp());
        ipLog.setUserType(userType);
        if (loginInfo!=null){
            ipLog.setState(IpLog.LOGIN_SUCCESS);
        }else {
            ipLog.setState(IpLog.LOGIN_FAILED);
        }
        ipLogService.insert(ipLog);
        return loginInfo;
    }

    @Override
    public void initAdmin() {
        //1判断数据库中是否有一个管理员 没有则创建
        int count =loginInfoMapper.selectByType(LoginInfo.USERTYPE_MANAGER);
        if(count<=0){
            LoginInfo loginInfo=new LoginInfo();
            loginInfo.setUserType(LoginInfo.USERTYPE_MANAGER);
            loginInfo.setState(LoginInfo.STATE_NORMAL);
            loginInfo.setUsername("admin");
            loginInfo.setPassword(MD5.encode("1111"));
            loginInfoMapper.insert(loginInfo);
        }
    }

    @Override
    public List<LoginInfo> autoComplate(String keyword) {
        return  loginInfoMapper.autoComplate(keyword);
    }
}
