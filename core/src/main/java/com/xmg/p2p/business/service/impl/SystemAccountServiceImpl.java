package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.SystemAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.business.mapper.SystemAccountMapper;
import com.xmg.p2p.business.service.ISystemAccountService;

@Service
public class SystemAccountServiceImpl implements ISystemAccountService {
	@Autowired
	private SystemAccountMapper systemAccountMapper;

	@Override
	public void initSystemAccount() {
		SystemAccount systemAccount = this.selectCurrent();
		if(systemAccount==null){
			systemAccount=new SystemAccount();
			this.save(systemAccount);
		}
	}

	@Override
	public int save(SystemAccount record) {
		return systemAccountMapper.insert(record);
	}

	@Override
	public SystemAccount selectCurrent() {
		return  systemAccountMapper.selectCurrent();
	}

	@Override
	public int update(SystemAccount record) {
		//乐观锁处理
		int count=systemAccountMapper.updateByPrimaryKey(record);
		if(count==0){//别人修改了这条数据
			throw new  RuntimeException("乐观锁异常");
		}
		return count;
	}
}
