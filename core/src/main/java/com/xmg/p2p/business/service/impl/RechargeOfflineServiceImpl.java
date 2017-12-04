package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.RechargeOffline;
import com.xmg.p2p.business.mapper.RechargeOfflineMapper;
import com.xmg.p2p.business.query.RechargeOfflineQueryObject;
import com.xmg.p2p.business.service.IAccountFlowService;
import com.xmg.p2p.business.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RechargeOfflineServiceImpl implements IRechargeOfflineService {
    @Autowired
    private RechargeOfflineMapper rechargeOfflineMapper;
    @Autowired
    private IAccountService accountService;
@Autowired
private IAccountFlowService accountFlowService;
    @Override
    public int insert(RechargeOffline record) {
        return rechargeOfflineMapper.insert(record);
    }

    @Override
    public int updateByPrimaryKey(RechargeOffline record) {
        return rechargeOfflineMapper.updateByPrimaryKey(record);
    }

    @Override
    public void apply(RechargeOffline rechargeOffline) {
        RechargeOffline rco = new RechargeOffline();
        rco.setApplier(UserContext.getCurrent());
        rco.setApplyTime(new Date());
        rco.setAmount(rechargeOffline.getAmount());
        rco.setBankInfo(rechargeOffline.getBankInfo());
        rco.setTradeTime(new Date());
        rco.setNote(rechargeOffline.getNote());
        rco.setTradeCode(rechargeOffline.getTradeCode());
        rco.setState(RechargeOffline.STATE_NORMAL);
        this.insert(rco);//充值不需要那么严格的判断
    }

    @Override
    public PageResult queryPage(RechargeOfflineQueryObject qo) {
        Long count = rechargeOfflineMapper.queryPageCount(qo);
        if (count <= 0) {
            return PageResult.empty(qo.getPageSize());
        }
        List<RechargeOffline> listData = rechargeOfflineMapper.queryPageData(qo);

        return new PageResult(listData, count.intValue(), qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public void audit(Long id, String remark, int state) {
        //审核逻辑
        RechargeOffline ro = this.rechargeOfflineMapper.selectByPrimaryKey(id);
        System.out.println(ro.toString());
        if (ro != null && ro.getState() == RechargeOffline.STATE_NORMAL) {
            //设置相关审核信息
            ro.setAuditor(UserContext.getCurrent());
            ro.setAuditTime(new Date());
            ro.setRemark(remark);
            System.out.println(ro.getBankInfo());
            if (state == RechargeOffline.STATE_PASS) {
                //点击审核通过
                ro.setState(RechargeOffline.STATE_PASS);
                //创建流水
                Account  applierAccount=accountService.get(ro.getApplier().getId());
                applierAccount.setUsableAmount(applierAccount.getUsableAmount().add(ro.getAmount()));//用户可用余额增加
                accountService.update(applierAccount);
                accountFlowService.createRechargeFlow(applierAccount,ro.getAmount());
            } else {//审核失败
				ro.setState(RechargeOffline.STATE_REJECT);
            }
            this.rechargeOfflineMapper.updateByPrimaryKey(ro);
        }

    }
}
