package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.SystemAccount;

public interface ISystemAccountService {
    void initSystemAccount();
    int save(SystemAccount record);
    SystemAccount selectCurrent();
    int update(SystemAccount record);
}
