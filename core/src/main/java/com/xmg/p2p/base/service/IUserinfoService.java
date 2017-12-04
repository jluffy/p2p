package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.Userinfo;

public interface IUserinfoService {
    int insert(Userinfo record);
    Userinfo get(Long id);
    int update(Userinfo record);
    /**
     * 手机绑定
     * @param phoneNumber
     * @param verifyCode
     */
    void bindPhone(String phoneNumber, String verifyCode);

    void bindEamil(String key);

    Userinfo getCurrent();

    void basicInfoSave(Userinfo userinfo);
}
