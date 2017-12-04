package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.domain.VideoAuth;
import com.xmg.p2p.base.mapper.VideoAuthMapper;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.VideoAuthQueryObject;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.service.IVideoAuthService;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VideoAuthServiceImpl implements IVideoAuthService {
	@Autowired
	private VideoAuthMapper videoAuthMapper;
	@Autowired
	IUserinfoService userinfoService;

	@Override
	public PageResult queryPage(VideoAuthQueryObject qo) {
		Long count=videoAuthMapper.queryPageCount(qo);
		if(count<=0){
			return PageResult.empty(qo.getPageSize());
		}
		List<VideoAuth> listData =videoAuthMapper.queryPageData(qo);
		return new PageResult(listData,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
	}

	@Override
	public void audit(Long loginInfoValue, String remark, int state) {
		//根据id查询出出该用户是否进行了视频认证
		Userinfo applyUserInfo=userinfoService.get(loginInfoValue);
		if(applyUserInfo!=null&&!applyUserInfo.getIsVedioAuth()){
			VideoAuth videoAuth = new VideoAuth();
			LoginInfo loginInfo=new LoginInfo();
			loginInfo.setId(loginInfoValue);
			videoAuth.setApplier(loginInfo);
			videoAuth.setApplyTime(new Date());//按道理是前台输入的日期这里只是为了简单
			videoAuth.setAuditor(UserContext.getCurrent());
			videoAuth.setAuditTime(new Date());
			videoAuth.setRemark(remark);
			if(state==VideoAuth.STATE_PASS){
				videoAuth.setState(VideoAuth.STATE_PASS);//设置状态
				//添加状态码
				applyUserInfo.addState(BitStatesUtils.OP_VEDIO_AUTH);
				userinfoService.update(applyUserInfo);
			}else{
				videoAuth.setState(VideoAuth.STATE_REJECT);
			}
			//审核对象保存入库
			videoAuthMapper.insert(videoAuth);
		}
	}

}
