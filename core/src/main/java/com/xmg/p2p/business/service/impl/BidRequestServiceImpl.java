package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.*;
import com.xmg.p2p.business.mapper.BidRequestMapper;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.service.*;
import com.xmg.p2p.business.util.CalculatetUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BidRequestServiceImpl implements IBidRequestService {
    @Autowired
    IAccountService accountService;
    @Autowired
    private BidRequestMapper bidRequestMapper;
    @Autowired
    private IUserinfoService userinfoService;

    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Autowired
    private IBidService bidService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;
    @Autowired
    IPaymentScheduleService paymentScheduleService;
    @Autowired
    IPaymentScheduleDetailService paymentScheduleDetailService;

    @Override
    public int save(BidRequest bidRequest) {
        return bidRequestMapper.insert(bidRequest);
    }

    public int update(BidRequest bidRequest) {
        //处理乐观锁
        int count = bidRequestMapper.updateByPrimaryKey(bidRequest);
        if (count == 0) {
            throw new RuntimeException("乐观锁异常,BidRequestId=" + bidRequest.getId());
        }
        return count;
    }

    @Override
    public List<BidRequest> selectBidRequestsPage(BidRequestQueryObject qo,int type) {//type 表示标的类型 体验标等

        return bidRequestMapper.queryPageData(qo);
    }

    @Override
    public List<BidRequest> queryByBidRequestState(BidRequestQueryObject qo) {
        return bidRequestMapper.queryPageData(qo);
    }

    @Override//后台页面展示
    public PageResult queryPage(BidRequestQueryObject qo) {
        Long count = bidRequestMapper.queryPageCount(qo);
        if (count <= 0) {
            return PageResult.empty(qo.getPageSize());
        }
        List<BidRequest> data = bidRequestMapper.queryPageData(qo);

        return new PageResult(data, count.intValue(), qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public BidRequest get(Long id) {
        return bidRequestMapper.selectByPrimaryKey(id);
    }


    @Override
    public boolean canApplyBorrow(Userinfo userinfo) {
        //借款条件
        if (userinfo.getIsBasicInfo() &&//完成基本信息认证
                userinfo.getIsVedioAuth() &&//完成视频认证
                userinfo.getIsRealAuth() &&//完成实名认证
                userinfo.getScore() >= BidConst.CREDIT_BORROW_SCORE//信用材料分数  大于30才可以借款
                ) {
            return true;
        }
        return false;
    }

    @Override
    public void borrowApply(BidRequest request) {
        //申请贷款逻辑
        Userinfo appyUserinfo = userinfoService.get(UserContext.getCurrent().getId());
        Account account = accountService.get(UserContext.getCurrent().getId());
        //不仅前台校验 后台也需要进一步校验
        if (this.canApplyBorrow(appyUserinfo) && !appyUserinfo.getHasRequestProcess() &&//根据当前用户没有正在申请的贷款
                request.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT) >= 0 &&//大于最小借款金额
                request.getBidRequestAmount().compareTo(account.getRemainBorrowLimit()) <= 0 &&//小于剩余额度
                request.getCurrentRate().compareTo(BidConst.MAX_CURRENT_RATE) <= 0 &&//小于最大利率
                request.getCurrentRate().compareTo(BidConst.SMALLEST_CURRENT_RATE) >= 0 &&//大于最小利率
                request.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT) >= 0//大于最小借款额
                ) {
            //判断满足申请的条件
            //设置bidrequest的参数已经更新userinfo的贷款申请状态码
            BidRequest bidRequest = new BidRequest();//参数只是接受数据 还是自己注值
            bidRequest.setApplyTime(new Date());
            bidRequest.setMinBidAmount(request.getMinBidAmount());
            bidRequest.setBidRequestAmount(request.getBidRequestAmount());
            bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);//待发布
            bidRequest.setBidRequestType(BidConst.BIDREQUEST_TYPE_NORMAL);//为普通信用标
            bidRequest.setCreateUser(UserContext.getCurrent());
            bidRequest.setCurrentRate(request.getCurrentRate());
            bidRequest.setDisableDays(request.getDisableDays());
            bidRequest.setDescription(request.getDescription());
            bidRequest.setMonthes2Return(request.getMonthes2Return());
            bidRequest.setReturnType(BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL);//按月分期
            bidRequest.setTitle(request.getTitle());
            //在用户申请满足申请贷款条件生成 发起投标时就计算好了利息
            bidRequest.setTotalRewardAmount(CalculatetUtil.calTotalInterest(bidRequest.getReturnType(),
                    bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(), bidRequest.getMonthes2Return()));
            //设置userinfo的状态码
            appyUserinfo.addState(BitStatesUtils.HAS_REQUEST_PROCESS);
            //保存申请贷款细节的入库
            this.save(bidRequest);
            //更新userinfo
            userinfoService.update(appyUserinfo);
        }
    }

    /**
     * 体验标的发布
     *
     * @param request
     */
    @Override
    public void expApply(BidRequest request) {
        if (request.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT) >= 0 &&//大于最小借款金额
                request.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT) >= 0//大于最小借款额
                ) {
            //判断满足申请的条件
            //设置bidrequest的参数已经更新userinfo的贷款申请状态码
            BidRequest bidRequest = new BidRequest();//参数只是接受数据 还是自己注值
            bidRequest.setApplyTime(new Date());
            bidRequest.setMinBidAmount(request.getMinBidAmount());
            bidRequest.setBidRequestAmount(request.getBidRequestAmount());
            bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);//待发布
            bidRequest.setBidRequestType(BidConst.BIDREQUEST_TYPE_EXP);//为体验标
            bidRequest.setCreateUser(UserContext.getCurrent());
            bidRequest.setCurrentRate(request.getCurrentRate());
            bidRequest.setDisableDays(request.getDisableDays());
            bidRequest.setMonthes2Return(request.getMonthes2Return());
            bidRequest.setReturnType(request.getReturnType());//按月分期
            bidRequest.setTitle(request.getTitle());
            //在用户申请满足申请贷款条件生成 发起投标时就计算好了利息
            bidRequest.setTotalRewardAmount(CalculatetUtil.calTotalInterest(bidRequest.getReturnType(),
                    bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(), bidRequest.getMonthes2Return()));
            //保存申请贷款细节的入库
            this.save(bidRequest);
        }
    }



    /**
     * 发标前审核
     *
     * @param id
     * @param remark
     * @param state
     */
    @Override
    public void borrowAudit(Long id, String remark, int state) {
        //后台审核贷款的逻辑  id是隐藏中传递的数据
        //查询出贷款发起人 满足借款需求 进入审核
        BidRequest bidRequest = null;
        bidRequest = bidRequestMapper.selectByPrimaryKey(id);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_PUBLISH_PENDING) {

            BidRequestAuditHistory bidRequestAuditHistory = new BidRequestAuditHistory();
            bidRequestAuditHistory.setApplier(bidRequest.getCreateUser());
            bidRequestAuditHistory.setApplyTime(new Date());
            bidRequestAuditHistory.setAuditor(UserContext.getCurrent());
            bidRequestAuditHistory.setAuditTime(new Date());
            bidRequestAuditHistory.setRemark(remark);
            bidRequestAuditHistory.setBidRequestId(id);
            bidRequestAuditHistory.setAuditType(BidRequestAuditHistory.PUBLISH_AUDIT);
            //点击审核通过要 设置状态
            if (state == BidRequestAuditHistory.STATE_PASS) {
                bidRequestAuditHistory.setState(BidRequestAuditHistory.STATE_PASS);//更改审核状态 通过 拒绝等
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);//设置为招标中状态
                //设置招标到期时间
                bidRequest.setDisableDate(DateUtils.addDays(new Date(),
                        bidRequest.getDisableDays()));
                bidRequest.setPublishTime(new Date());//发标时间
                bidRequest.setNote(remark);//备注

            } else {
                //点击审核拒绝 设置reject状态
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE);
                if (bidRequest.getBidRequestType()==BidConst.BIDREQUEST_TYPE_NORMAL) {
                    bidRequestAuditHistory.setState(BidRequestAuditHistory.STATE_REJECT);
                    Userinfo applier = userinfoService.get(bidRequest.getCreateUser().getId());
                    applier.removeState(BitStatesUtils.HAS_REQUEST_PROCESS);
                    userinfoService.update(applier);
                }
            }
            this.update(bidRequest);
            bidRequestAuditHistoryService.save(bidRequestAuditHistory);
        }
    }


    //投标
    @Override
    public void bid(Long bidRequestId, BigDecimal amount, LoginInfo loginInfo) {
        //判断逻辑
        BidRequest bidRequest = this.get(bidRequestId);
        Account bidUserAccount = accountService.get(UserContext.getCurrent().getId());
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_BIDDING &&
                amount.compareTo(bidRequest.getMinBidAmount()) >= 0 &&//大于最小投标额度
                amount.compareTo(bidUserAccount.getUsableAmount().min(bidRequest.getRemainAmount())) <= 0 &&//小于账户余额和所需投标的最小值
                !UserContext.getCurrent().getId().equals(bidRequest.getCreateUser().getId())//投标人不能使本人
                ) {
            //对于借款对象来说
            //投资数量加一 金额加上投资金额
            bidRequest.setBidCount(bidRequest.getBidCount() + 1);
            bidRequest.setCurrentSum(bidRequest.getCurrentSum().add(amount));

            //对于投资对象
            //创建bid对象
            Bid bid = new Bid();
            bid.setActualRate(bidRequest.getCurrentRate());
            bid.setAvailableAmount(amount);//投标金额
            bid.setBidRequestId(bidRequestId);
            bid.setBidRequestState(bidRequest.getBidRequestState());
            bid.setBidTime(new Date());
            bid.setBidRequestTitle(bidRequest.getTitle());
            bid.setBidUser(UserContext.getCurrent());
            //保存入库
            bidService.save(bid);

            //对于投资人
            //账户余额减少 冻结金额增加 生成投标流水
            bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().subtract(amount));
            bidUserAccount.setFreezedAmount(bidUserAccount.getFreezedAmount().add(amount));
            accountService.update(bidUserAccount);
            //创建一个投标流水
            accountFlowService.createBidFlow(bidUserAccount, amount);
            //如果投标已满
            if (bidRequest.getCurrentSum().compareTo(bidRequest.getBidRequestAmount()) == 0) {
                //借款对象的状态更改为满标一审
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                //投标的对象全部变为满标一审
                bidService.updateState(bidRequestId, BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
            }
            this.update(bidRequest);
        }
    }

    //满标一审
    @Override
    public void borrowAudit1(Long id, String remark, int state) {
        //1.根据BidRequest/audit.ftl中所需的数据,在后台控制器中构建需要的数据.

        //2.满标一审的业务逻辑:
        //2.1判断借款的状态是否在满标一审的状态
        BidRequest bidRequest = this.get(id);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1) {
            createRequestAuditHistory(bidRequest, BidRequestAuditHistory.FULL_AUDIT_1, remark, state);
            if (state == BidRequestAuditHistory.STATE_PASS) {//审核通过
                //2.3.1修改借款对象的状态---->满标二审状态
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
                //2.3.2修改投标对象的状态---->满标二审状态
                //这时可以选择批量修改的方式 将投标用户的状态统一更改为满标二审状态
                bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);

            } else {
                auditReject(bidRequest);  //调用满标拒绝的方法
            }
            //不要忘记更新bidrequest了
            this.update(bidRequest);
        }
    }

    @Override
    public void borrowAudit2(Long id, String remark, int state) {
        //满标二审逻辑:
        //1.获取到BiRequest中的id,根据Id查询对于的借款对象信息.
        BidRequest bidRequest = get(id);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2) {
            createRequestAuditHistory(bidRequest, BidRequestAuditHistory.FULL_AUDIT_2, remark, state);
            if (state == BidRequestAuditHistory.STATE_PASS) {//点击审核通过
                // 对于标的操作
                //设置借款对象的的状态--->还款中
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PAYING_BACK);
                //设置投标对象的状态---->还款中
                bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_PAYING_BACK);
                //    对于借款对象的操作
                //借款人的可用金额添加
                Account bidRequestAccount = accountService.get(bidRequest.getCreateUser().getId());
                bidRequestAccount.setUsableAmount(bidRequestAccount.getUsableAmount().add(bidRequest.getBidRequestAmount()));
                //借款人的剩余授信额度减少
                bidRequestAccount.setRemainBorrowLimit(bidRequestAccount.getBorrowLimit().subtract(bidRequest.getBidRequestAmount()));
                //用户的待还金额增加(本金+利息)
                //在用户申请满足申请贷款条件生成 发起投标时就计算好了利息
                bidRequestAccount.setUnReturnAmount(bidRequestAccount.getUnReturnAmount().add(bidRequest.getBidRequestAmount()
                        .add(bidRequest.getTotalRewardAmount())));//这次借的钱加利息
                //移除借款用户userinfo对象<正在借款>的状态码
                Userinfo createUserUserinfo = userinfoService.get(bidRequest.getCreateUser().getId());
                createUserUserinfo.removeState(BitStatesUtils.HAS_REQUEST_PROCESS);
                //支付平台的管理费(初始化平台的系统账户和系统账户流水)
                BigDecimal managementAccount = CalculatetUtil.calAccountManagementCharge(bidRequest.getBidRequestAmount());
                bidRequestAccount.setUsableAmount(bidRequestAccount.getUsableAmount().subtract(managementAccount));
                accountService.update(bidRequestAccount);
                //增加借款流水对象
                accountFlowService.createBorrowSuccessFlow(bidRequestAccount, bidRequest.getBidRequestAmount());

                //系统账户的可用金额增加
                SystemAccount systemAccount = systemAccountService.selectCurrent();//只有一条记录
                systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(managementAccount));
                //更新系统账户数据
                systemAccountService.update(systemAccount);
                //增加系统用户收取平台管理费的流水
                systemAccountFlowService.createReceiveManagementChargeFlow(systemAccount, managementAccount);

                //更新userinfo对象
                userinfoService.update(createUserUserinfo);

                //    4 还款后续的操作
                //生成还款明细对象
                List<PaymentSchedule> paymentSchedules = createPayScheduleList(bidRequest);//抽为一个方法

                //   3    对于投资人的操作
                //遍历bidRequest.getBids()获取到投资列表
                Map<Long, Account> accountMap = new HashMap<Long, Account>();
                //投资人冻结金额减少
                Account bidUserAccount;
                Long bidUserId;
                //遍历投标对象
                for (Bid bid : bidRequest.getBids()) {
                    bidUserId = bid.getBidUser().getId();
                    // bidUserAccount = accountService.get(bidUserId);//大错特错 这样 进不去下面的if里面 会导致map为空
                    bidUserAccount = accountMap.get(bidUserId);
                    if (bidUserAccount == null) {
                        bidUserAccount = accountService.get(bidUserId);
                        accountMap.put(bidUserId, bidUserAccount);
                    }
                    //冻结金额减去这次投资的金额  一个用户多次投资的话 会在这一做一个统计 最后一次更新
                    bidUserAccount.setFreezedAmount(bidUserAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));

                    //增加投资成功的流水
                    accountFlowService.createSuccessBidFlow(bidUserAccount, bid.getAvailableAmount());
                }
                //投资人的代收本金和待收利息增加-->后续算出来的
                for (PaymentSchedule ps : paymentSchedules) {
                    for (PaymentScheduleDetail psd : ps.getDetails()) {
                        //给每一个用户添加待收的本金和利息
                        bidUserAccount = accountMap.get(psd.getInvestorId());
                        bidUserAccount.setUnReceivePrincipal(bidUserAccount.getUnReceivePrincipal().add(psd.getPrincipal()));
                        bidUserAccount.setUnReceiveInterest(bidUserAccount.getUnReceiveInterest().add(psd.getInterest()));
                    }
                }
                //统一更新投资人账号
                for (Account account : accountMap.values()) {
                    accountService.update(account);
                }

            } else {
                //调用满标拒绝的的方法
                auditReject(bidRequest);
            }
        }
        //不要忘记更新bidrequest了
        this.update(bidRequest);
    }


    public List<PaymentSchedule> createPayScheduleList(BidRequest bidRequest) {
        List<PaymentSchedule> paymentSchedules = new ArrayList<PaymentSchedule>();
        //构建相关的信息.
        //根据分期期数 构建相对应的还款对象个数
        PaymentSchedule paymentSchedule;
        BigDecimal principalTemp = BidConst.ZERO;
        BigDecimal interestTemp = BidConst.ZERO;
        //遍历还款对象 每个对象中又遍历出所有的投标对象
        for (int i = 0; i < bidRequest.getMonthes2Return(); i++) {
            paymentSchedule = new PaymentSchedule();
            paymentSchedule.setBidRequestId(bidRequest.getId());
            paymentSchedule.setBidRequestTitle(bidRequest.getTitle());
            paymentSchedule.setBidRequestType(bidRequest.getBidRequestType());
            paymentSchedule.setBorrowUser(bidRequest.getCreateUser());
            paymentSchedule.setDeadLine(DateUtils.addMonths(bidRequest.getPublishTime(), i + 1));
            paymentSchedule.setMonthIndex(i + 1);
            paymentSchedule.setReturnType(bidRequest.getReturnType());
            //不是最后一期的还款
            if (i < bidRequest.getMonthes2Return() - 1) {
                //当期还款利息=通过工具计算出对应该期的还款利息
                paymentSchedule.setInterest(CalculatetUtil.calMonthlyInterest(paymentSchedule.getReturnType(),
                        bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(), i + 1, bidRequest.getMonthes2Return()));
                //计算当期待还金额
                paymentSchedule.setTotalAmount(CalculatetUtil.calMonthToReturnMoney(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(),
                        bidRequest.getCurrentRate(), i + 1, bidRequest.getMonthes2Return()));
                //当期还款本金=计算当期待还金额-利息
                paymentSchedule.setPrincipal(paymentSchedule.getTotalAmount().subtract(paymentSchedule.getInterest()));
                //将之前的每期利息本金存储起来 方便计算最后一期的利息 本金
                principalTemp = principalTemp.add(paymentSchedule.getPrincipal());
                interestTemp = interestTemp.add(paymentSchedule.getInterest());
            } else {
                //是最后一期的还款
                //当期还款本金=标的借款金额-前面几期的本金之和
                paymentSchedule.setPrincipal(bidRequest.getBidRequestAmount().subtract(principalTemp));//借款总额减去前几期本金
                //当期还款利息=标的总利息-前面几期的利息之和
                paymentSchedule.setInterest(bidRequest.getTotalRewardAmount().subtract(interestTemp));
                //当期待还金额=当期还款本金+当期还款利息
                paymentSchedule.setTotalAmount(paymentSchedule.getPrincipal().add(paymentSchedule.getInterest()));
            }
            paymentScheduleService.save(paymentSchedule);
            //每一期里面需要生成还款明细,遍历bidRequest.getBids()
            createScheduleDetail(paymentSchedule, bidRequest);//涉及到对象的引用 上面保存后 下次能取到id
            //创建还款明细对象,设置相关属性
            paymentSchedules.add(paymentSchedule);

        }
        return paymentSchedules;

    }

    //创建投资还款的明细
    private void createScheduleDetail(PaymentSchedule paymentSchedule, BidRequest bidRequest) {
        Bid bid;
        PaymentScheduleDetail psd;
        BigDecimal principalTemp = BidConst.ZERO;
        BigDecimal interestTemp = BidConst.ZERO;
        for (int i = 0; i < bidRequest.getBids().size(); i++) {//遍历所有的投资对象
            bid = bidRequest.getBids().get(i);
            psd = new PaymentScheduleDetail();
            psd.setBidAmount(bid.getAvailableAmount());
            psd.setBidRequestId(bid.getBidRequestId());
            psd.setBidId(bid.getId());
            psd.setBorrowUser(bidRequest.getCreateUser());
            psd.setDeadLine(paymentSchedule.getDeadLine());
            psd.setInvestorId(bid.getBidUser().getId());
            psd.setMonthIndex(paymentSchedule.getMonthIndex());
            psd.setPaymentScheduleId(paymentSchedule.getId());
            psd.setReturnType(bidRequest.getReturnType());
            //不是最后一个投资人
            if (i < bidRequest.getBids().size() - 1) {
                BigDecimal rate = bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(),
                        BidConst.STORE_SCALE);//保持和存储同样的位数
                //还款明细代收本金=该期的还款本金*该投资人投资金额/总金额
                psd.setPrincipal(rate.multiply(paymentSchedule.getPrincipal()));
                //还款明细代收利息=该期的还款利息*该投资人投资金额/总金额
                psd.setInterest(rate.multiply(paymentSchedule.getInterest()));
                //还款明细代收金额=还款明细代收本金+还款明细代收利息
                psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));

                //将本金利息存储起来 准备计算最后一个投资人的本金 利息
                principalTemp = principalTemp.add(psd.getPrincipal());
                interestTemp = interestTemp.add(psd.getInterest());
            } else {
                //是最后一个投资人
                //还款明细代收本金=该期的还款本金-前几期的代收本金之和
                psd.setPrincipal(paymentSchedule.getPrincipal().subtract(principalTemp));
                //还款明细代收利息=该期的还款利息-前几期的代收利息之和
                psd.setInterest(paymentSchedule.getInterest().subtract(interestTemp));
                //还款明细代收金额=还款明细代收本金+还款明细代收利息
                psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));
            }
            paymentScheduleDetailService.save(psd);
            paymentSchedule.getDetails().add(psd);

        }
    }


    //抽取一个记录审核历史的方法
    public void createRequestAuditHistory(BidRequest bidRequest, int actionType, String remark, int state) {
        //2.2创建审核对象BidRequestAuditHistory对象,设置相关属性
        BidRequestAuditHistory brah = new BidRequestAuditHistory();
        //设置审核历史对象的相关属性
        brah.setAuditor(UserContext.getCurrent());
        brah.setAuditType(actionType);//审核的状态  一审 二审 等
        brah.setAuditTime(new Date());
        brah.setRemark(remark);
        brah.setApplyTime(new Date());
        brah.setApplier(bidRequest.getCreateUser());
        brah.setBidRequestId(bidRequest.getId());
        if (state == BidRequestAuditHistory.STATE_PASS) {//审核通过
            //2.3审核通过
            brah.setState(BidRequestAuditHistory.STATE_PASS);
        } else {
            //2.4审核拒
            brah.setState(BidRequestAuditHistory.STATE_REJECT);
        }
        bidRequestAuditHistoryService.save(brah);
    }

    //抽取审核拒绝的方法
    public void auditReject(BidRequest bidRequest) {//一审拒绝和二审拒绝是同样的状态  所以不需要传过来
        //2.4.1修改借款对象的状态---->满标拒绝
        bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
        //2.4.2修改投标对象的状态---->满标拒绝  存在一个客户多次投标的情况 可以先获取一个投标对象的集合
        //遍历集合的时候 将数据存储在一个map中最后统计完成后对数据库积性一次操作 减少对数据库的频繁访问
        Map<Long, Account> accountMap = new HashMap<Long, Account>();
        List<Bid> bids = bidRequest.getBids();
        Long bidUserId;
        Account bidUserAccount;
        //2.4.3遍历投标对象,把投资的钱归还.
        for (Bid bid : bids) {
            bidUserId = bid.getBidUser().getId();
            bidUserAccount = accountMap.get(bidUserId);
            if (bidUserAccount == null) {
                bidUserAccount = accountService.get(bidUserId);
                //添加到map中!!!!!!!
                accountMap.put(bidUserId, bidUserAccount);
            }
            //更改可以金额增加
            bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().add(bid.getAvailableAmount()));
            //冻结金额减少
            bidUserAccount.setFreezedAmount(bidUserAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
            // 生成投标失败的流水
            accountFlowService.createBidFailedFlow(bidUserAccount, bid.getAvailableAmount());
        }
        //遍历map的值  更新投标账号数据
        for (Account account : accountMap.values()) {
            accountService.update(account);
        }
        //更新bidRequest对象数据
        Userinfo bidUserUserinfo = userinfoService.get(bidRequest.getCreateUser().getId());
        bidUserUserinfo.removeState(BitStatesUtils.HAS_REQUEST_PROCESS);
        //bidRequest
        //2.4.4 借款人userinfo对象去掉正在借款的状态码,更新借款人userinfo对象
        userinfoService.update(bidUserUserinfo);
    }
}
