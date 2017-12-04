
package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.AjaxResult;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.service.IBidRequestService;
import com.xmg.p2p.website.util.RequireLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
public class BorrowInfoController {

    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IAccountService accountService;

    @RequireLogin
    @RequestMapping("/borrowInfo")
    public String borrow(Model model) {

        //借款需要满足一定条件
        Userinfo userinfo = userinfoService.getCurrent();
        if (bidRequestService.canApplyBorrow(userinfo)) {
            //满足借款条件时还要判断是否有正在申请的贷款 跳转不同的页面
            if (!userinfo.getHasRequestProcess()) {//没有
                model.addAttribute("account", accountService.get(UserContext.getCurrent().getId()));
                model.addAttribute("minBidRequestAmount", BidConst.SMALLEST_BIDREQUEST_AMOUNT);
                model.addAttribute("minBidAmount", BidConst.MIN_WITHDRAW_AMOUNT);
                return "borrow_apply";
            } else {//有
                return "borrow_apply_result";
            }
        } else {
            return "redirect:/borrowIndex";
        }
    }

    @RequestMapping("/borrow_apply")
    @RequireLogin
    public String borrowApply(BidRequest bidRequest) {
       bidRequestService.borrowApply(bidRequest);
        return "redirect:/borrowInfo";
    }

    //投标审核操作
    @RequestMapping("/borrow_bid")
    @ResponseBody
    public AjaxResult bid(Long bidRequestId, BigDecimal amount,LoginInfo loginInfo){
        AjaxResult result=null;
        try{
            this.bidRequestService.bid(bidRequestId,amount, loginInfo);
            result=new AjaxResult();
        }catch(Exception e){
            result=new AjaxResult(false,"投标失败");
            e.printStackTrace();
        }
        return  result;
    }
}
