package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.Account;

public interface IAccountService {
    int insert(Account record);
    Account get(Long id);
    int update(Account record);
}
