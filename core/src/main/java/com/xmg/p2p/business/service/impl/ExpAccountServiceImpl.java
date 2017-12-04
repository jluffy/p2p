package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.ExpAccount;
import com.xmg.p2p.business.domain.ExpAccountFlow;
import com.xmg.p2p.business.domain.ExpAccountGrantRecord;
import com.xmg.p2p.business.mapper.ExpAccountMapper;
import com.xmg.p2p.business.service.IExpAccountFlowService;
import com.xmg.p2p.business.service.IExpAccountGrantRecordService;
import com.xmg.p2p.business.service.IExpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class ExpAccountServiceImpl implements IExpAccountService {
	@Autowired
	private ExpAccountMapper expAccountMapper;
	@Autowired
	private IExpAccountGrantRecordService expAccountGrantRecordService;
	@Autowired
	private IExpAccountFlowService expAccountFlowService;

	@Override
	public void save(ExpAccount expAccount) {
		expAccountMapper.insert(expAccount);

	}

	@Override
	public void update(ExpAccount expAccount) {
		expAccountMapper.updateByPrimaryKey(expAccount);
	}


	@Override
	public void grantExpMoney(Long id, LastTime lastTime, BigDecimal registerGrantExpmoney, int expmoneyTypeRegister) {
		//创建一个发放回收的记录对象
		ExpAccountGrantRecord record=new ExpAccountGrantRecord();
		record.setAmount(registerGrantExpmoney);
		record.setGrantDate(new Date());
		record.setGrantType(expmoneyTypeRegister);
		record.setGrantUserId(id);
		record.setNote("注册发放体验金");
		record.setReturnDate(lastTime.getReturnTime(new Date()));
		record.setState(ExpAccountGrantRecord.STATE_NORMAL);
		expAccountGrantRecordService.save(record);
		//修改体验金账户
		ExpAccount account=this.expAccountMapper.selectByPrimaryKey(id);
		account.setUsableAmount(account.getUsableAmount().add(registerGrantExpmoney));
		this.update(account);
		//增加一条体验金流水
		ExpAccountFlow flow=new ExpAccountFlow();
		flow.setActionTime(new Date());
		flow.setActionType(expmoneyTypeRegister);
		flow.setAmount(registerGrantExpmoney);
		flow.setExpAccountId(id);
		flow.setFreezedAmount(account.getFreezedAmount());
		flow.setNote("注册发放体验金");
		flow.setUsableAmount(account.getUsableAmount());
		this.expAccountFlowService.save(flow);

	}

	@Override
	public ExpAccount get(Long id) {
		return expAccountMapper.selectByPrimaryKey(id);
	}


}
