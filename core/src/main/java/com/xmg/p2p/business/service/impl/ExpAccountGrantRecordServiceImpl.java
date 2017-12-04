package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.ExpAccountGrantRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.business.mapper.ExpAccountGrantRecordMapper;
import com.xmg.p2p.business.service.IExpAccountGrantRecordService;

@Service
public class ExpAccountGrantRecordServiceImpl implements IExpAccountGrantRecordService {
	@Autowired
	private ExpAccountGrantRecordMapper expAccountGrantRecordMapper;

	@Override
	public void save(ExpAccountGrantRecord expAccountGrantRecord) {
		expAccountGrantRecordMapper.insert(expAccountGrantRecord);
	}

	@Override
	public void update(ExpAccountGrantRecord expAccountGrantRecord) {
		expAccountGrantRecordMapper.updateByPrimaryKey(expAccountGrantRecord);
	}
}
