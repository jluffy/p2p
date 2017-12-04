package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.business.domain.PlatformBankInfo;
import com.xmg.p2p.business.mapper.PlatformBankInfoMapper;
import com.xmg.p2p.business.query.PlatformBankInfoQueryObject;
import com.xmg.p2p.business.service.IPlatformBankInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformBankInfoServiceImpl implements IPlatformBankInfoService {
	@Autowired
	private PlatformBankInfoMapper platformBankInfoMapper;

	@Override
	public int save(PlatformBankInfo record) {
		return platformBankInfoMapper.insert(record);
	}

	@Override
	public int update(PlatformBankInfo record) {
		return platformBankInfoMapper.updateByPrimaryKey(record);
	}

	@Override
	public PageResult queryForPage(PlatformBankInfoQueryObject qo) {
		Long count = platformBankInfoMapper.queryPageCount(qo);
		if(count<=0){
			return PageResult.empty(qo.getPageSize());
		}
		List<PlatformBankInfo> data=platformBankInfoMapper.queryPageData(qo);

		return new PageResult(data,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
	}

	@Override
	public void saveOrUpdate(PlatformBankInfo platformBankInfo) {
		if(platformBankInfo.getId()!=null){
			this.update(platformBankInfo);
		}else{
			this.save(platformBankInfo);
		}
	}

	@Override
	public List<PlatformBankInfo> selectAll() {
		return platformBankInfoMapper.selectAll();
	}
}
