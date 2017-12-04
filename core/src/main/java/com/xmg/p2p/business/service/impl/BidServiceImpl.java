package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.Bid;
import com.xmg.p2p.business.mapper.BidMapper;
import com.xmg.p2p.business.service.IBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidServiceImpl implements IBidService {

	@Autowired
	private BidMapper bidMapper;

	@Override
	public void save(Bid bid) {
		bidMapper.insert(bid);
	}

	@Override
	public void updateState(Long bidRequestId,int state) {
		bidMapper.updateState(bidRequestId,state);
	}

}
