package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.SystemAccount;
import com.xmg.p2p.business.domain.SystemAccountFlow;

import java.math.BigDecimal;

public interface ISystemAccountFlowService {
    int save(SystemAccountFlow record);

    /**
     * 系统账户流水
     * @param amount
     * @param account
     */
    void createReceiveManagementChargeFlow(SystemAccount amount, BigDecimal account);
}
