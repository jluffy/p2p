package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.SystemAccount;
import com.xmg.p2p.business.domain.SystemAccountFlow;
import com.xmg.p2p.business.mapper.SystemAccountFlowMapper;
import com.xmg.p2p.business.service.ISystemAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class SystemAccountFlowServiceImpl implements ISystemAccountFlowService {
	@Autowired
	private SystemAccountFlowMapper systemAccountFlowMapper;

	@Override
	public int save(SystemAccountFlow record) {
		return systemAccountFlowMapper.insert(record);
	}

	@Override
	public void createReceiveManagementChargeFlow(SystemAccount account, BigDecimal amount) {
		createSystemFlow(BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE,"贷款成功后收取借款用户的服务费"+amount+"元",account,amount);
	}
	public void createSystemFlow(int actionType,String remark,SystemAccount systemAccount,BigDecimal managementCharge){
		SystemAccountFlow systemAccountFlow=new SystemAccountFlow();
		systemAccountFlow.setActionTime(new Date());
		systemAccountFlow.setActionType(actionType);
		systemAccountFlow.setFreezedAmount(systemAccount.getFreezedAmount());
		systemAccountFlow.setAmount(systemAccount.getUsableAmount().add(managementCharge));
		systemAccountFlow.setRemark(remark);
		systemAccountFlow.setUsableAmount(systemAccount.getUsableAmount().add(managementCharge));
		this.save(systemAccountFlow);
	}
}
