package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.AccountFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.business.mapper.AccountFlowMapper;
import com.xmg.p2p.business.service.IAccountFlowService;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AccountFlowServiceImpl implements IAccountFlowService {
	@Autowired
	private AccountFlowMapper accountFlowMapper;

	@Override
	public void createRechargeFlow(Account account, BigDecimal amount) {
		this.createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE,"线下充值"+amount+"元");
	}

	@Override
	public void save(AccountFlow accountFlow) {
		accountFlowMapper.insert(accountFlow);
	}

	@Override
	public void createBidFlow(Account account, BigDecimal amount) {
	this.createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED,"投标冻结金额流水:"+amount+"元");//冻结流水
	}

	@Override
	public void createBidFailedFlow(Account account, BigDecimal amount) {
		this.createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED,"取消投标冻结金额流水:"+amount+"元");//取消冻结流水(满标一审拒绝)
	}

	@Override
	public void createBorrowSuccessFlow(Account account, BigDecimal amount) {
		this.createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL,"审核完毕用户增加余额流水:"+amount+"元");//成功借款流水
	}

	@Override
	public void createSuccessBidFlow(Account account, BigDecimal amount) {
		this.createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL,"成功投标解冻金额流水:"+amount+"元");//成功投标流水

	}

	public void createFlow(Account account, BigDecimal amount,int actionType,String remark){
		AccountFlow accountFlow=new AccountFlow();
		accountFlow.setAccountId(account.getId());
		accountFlow.setActionType(actionType);//线下充值流水变化
		accountFlow.setTradeTime(new Date());
		accountFlow.setAmount(amount);
		accountFlow.setUsableAmount(account.getUsableAmount());
		accountFlow.setFreezedAmount(account.getFreezedAmount());
		accountFlow.setRemark(remark);
		this.save(accountFlow);
	}
}
