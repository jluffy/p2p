package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.VideoAuth;
import com.xmg.p2p.base.query.QueryObject;

import java.util.List;

public interface VideoAuthMapper {
    int insert(VideoAuth record);
    VideoAuth selectByPrimaryKey(Long id);
    int updateByPrimaryKey(VideoAuth record);
	Long queryPageCount(QueryObject qo);
	List<VideoAuth> queryPageData(QueryObject qo);

}