package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.base.mapper.AccountMapper;
import com.xmg.p2p.base.service.IAccountService;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public int insert(Account record) {
        return accountMapper.insert(record);
    }

    @Override
    public Account get(Long id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Account record) {
        //做乐观锁处理
        int count = accountMapper.updateByPrimaryKey(record);
        if (count <= 0) {
            throw new RuntimeException("乐观锁出现异常");
        }
        return count;
    }
}
