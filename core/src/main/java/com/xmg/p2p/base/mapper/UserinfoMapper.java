package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.query.QueryObject;

import java.util.List;

public interface UserinfoMapper {
    int insert(Userinfo record);
    Userinfo selectByPrimaryKey(Long id);
    int updateByPrimaryKey(Userinfo record);
	Long queryPageCount(QueryObject qo);
	List<Userinfo> queryPageData(QueryObject qo);

}