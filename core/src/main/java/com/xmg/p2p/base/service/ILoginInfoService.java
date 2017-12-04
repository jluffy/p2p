package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.LoginInfo;

import java.util.List;

public interface ILoginInfoService {
    public void register(String username, String password);

    boolean checkUsername(String username);

    public LoginInfo login(String username, String password, int userType);

    void initAdmin();

    List<LoginInfo> autoComplate(String keyword);
}
