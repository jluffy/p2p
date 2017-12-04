package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.SystemDictionary;
import com.xmg.p2p.base.mapper.SystemDictionaryMapper;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryQueryObject;
import com.xmg.p2p.base.service.ISystemDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemDictionaryServiceImpl implements ISystemDictionaryService {
	@Autowired
	private SystemDictionaryMapper systemDictionaryMapper;

	@Override
	public List<SystemDictionary> selectAll() {
		return systemDictionaryMapper.selectAll();
	}

	@Override
	public PageResult queryPage(SystemDictionaryQueryObject qo) {
		Long count =systemDictionaryMapper.queryPageCount(qo);
		if(count<=0){
			return PageResult.empty(qo.getPageSize());
		}
		List<SystemDictionary> data = systemDictionaryMapper.queryPageData(qo);

		return new PageResult(data,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
	}

	@Override
	public void save(SystemDictionary dictionary) {
		systemDictionaryMapper.insert(dictionary);
	}

	@Override
	public void update(SystemDictionary dictionary) {
		systemDictionaryMapper.updateByPrimaryKey(dictionary);
	}

	@Override
	public void saveOrUpdate(SystemDictionary dictionary) {
		if(dictionary.getId()==null){
			this.save(dictionary);
		}else{
			this.update(dictionary);
		}
	}


}
