package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.ExpAccountFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.business.mapper.ExpAccountFlowMapper;
import com.xmg.p2p.business.service.IExpAccountFlowService;

@Service
public class ExpAccountFlowServiceImpl implements IExpAccountFlowService {
	@Autowired
	private ExpAccountFlowMapper expAccountFlowMapper;

	@Override
	public void save(ExpAccountFlow expAccountFlow) {
		expAccountFlowMapper.insert(expAccountFlow);
	}

}
