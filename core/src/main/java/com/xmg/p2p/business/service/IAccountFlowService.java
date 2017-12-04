package com.xmg.p2p.business.service;
import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.business.domain.AccountFlow;

import java.math.BigDecimal;

public interface IAccountFlowService {
    void createRechargeFlow(Account applierAccount, BigDecimal amount);
    void save(AccountFlow accountFlow);

    /**
     * 创建投标流水
     * @param account
     * @param amount
     */
    void createBidFlow(Account account, BigDecimal amount);

    /**
     * 满标一审失败的流水
     * @param account
     * @param amount
     */
    void createBidFailedFlow(Account account, BigDecimal amount);

    /**
     * 二审通过生成的借款人的进账流水
     * @param account
     * @param amount
     */
    void createBorrowSuccessFlow(Account account, BigDecimal amount);

    /**
     * 投标成功流水
     * @param account
     * @param amount
     */
    void createSuccessBidFlow(Account account, BigDecimal amount);
}
