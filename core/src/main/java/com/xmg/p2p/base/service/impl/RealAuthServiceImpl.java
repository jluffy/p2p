package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.mapper.RealAuthMapper;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.RealAuthQueryObject;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RealAuthServiceImpl implements IRealAuthService {
	@Autowired
	private RealAuthMapper realAuthMapper;
	@Autowired
	private IUserinfoService userinfoService;

	@Override
	public RealAuth get(Long id) {
		return realAuthMapper.selectByPrimaryKey(id);
	}

	@Override
	public void apply(RealAuth record) {
		//申请实名认证的逻辑完成
		//userinfo对象在用户注册的时候已经初始化
		Userinfo userinfo = userinfoService.getCurrent();
		//1根据用户id查询数据库获得realAuthId 为未认证状态则进入下一步
		if(!userinfo.getIsRealAuth()&&userinfo.getRealAuthId()==null){
			//设置属性
			RealAuth realAuth = new RealAuth();
			realAuth.setAddress(record.getAddress());
			realAuth.setIdNumber(record.getIdNumber());
			realAuth.setRealName(record.getRealName());
			realAuth.setSex(record.getSex());
			realAuth.setBornDate(record.getBornDate());
			realAuth.setImage1(record.getImage1());
			realAuth.setImage2(record.getImage2());

			realAuth.setApplier(UserContext.getCurrent());
			realAuth.setApplyTime(new Date());
			realAuth.setState(RealAuth.STATE_NORMAL);

			realAuthMapper.insert(realAuth);

			//添加userinfo的realAuthId 并更行数据库
			userinfo.setRealAuthId(realAuth.getId());
			userinfoService.update(userinfo);//userinfo是查询出来的执行更新操作未设置的值还是原样
		}
	}

	@Override
	public PageResult queryPage(RealAuthQueryObject qo) {
		Long count=realAuthMapper.queryPageCount(qo);
		if(count<=0){
			return PageResult.empty(qo.getPageSize());
		}
		List<RealAuth> listData =realAuthMapper.queryPageData(qo);
		return new PageResult(listData,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
	}

	@Override
	public void audit(Long id, String remark, int state) {
		//审核的逻辑
		//根据传入的id查询需要认证的审核对象
		RealAuth realAuth=this.get(id);
		//判断对象的审核状态
		if(realAuth!=null&&realAuth.getState()==RealAuth.STATE_NORMAL){
			//审核开始 添加公共属性
			realAuth.setAuditor(UserContext.getCurrent());
			realAuth.setAuditTime(new Date());
			realAuth.setRemark(remark);
			Userinfo applyUserinfo=userinfoService.get(realAuth.getApplier().getId());
			if(state==RealAuth.STATE_PASS){//点击审核通过
				//更新审核状态
				realAuth.setState(RealAuth.STATE_PASS);

				//添加状态码 更新userinfo
				applyUserinfo.addState(BitStatesUtils.OP_REAL_AUTH);
				applyUserinfo.setRealName(realAuth.getRealName());//审核完毕设置userinfo的名字
				applyUserinfo.setIdNumber(realAuth.getIdNumber());
			}else{
				//拒绝审核
				realAuth.setState(RealAuth.STATE_REJECT);
				applyUserinfo.setRealAuthId(null);
			}
			realAuthMapper.updateByPrimaryKey(realAuth);
			userinfoService.update(applyUserinfo);

		}
	}
}
