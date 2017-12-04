package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.IpLog;
import com.xmg.p2p.base.mapper.IpLogMapper;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.IpLogQueryObject;
import com.xmg.p2p.base.service.IIpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpLogServiceImpl implements IIpLogService {
	@Autowired
	private IpLogMapper ipLogMapper;

	@Override
	public int insert(IpLog record) {
		return ipLogMapper.insert(record);
	}

	@Override
	public PageResult queryPage(IpLogQueryObject qo) {
		Long count = ipLogMapper.queryPageCount(qo);
		if(count<=0){
			return PageResult.empty(qo.getPageSize());
		}
		List<IpLog> data=ipLogMapper.queryPageData(qo);

		return new PageResult(data,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
	}

}
