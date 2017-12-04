package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.service.IBidRequestService;
import com.xmg.p2p.business.service.IExpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IUserFileService userFileService;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IExpAccountService exAaccountService;

    @RequestMapping("/index")
    public String index(Model model, BidRequestQueryObject qo) {
        qo.setStates(new int[]{
                BidConst.BIDREQUEST_STATE_BIDDING,
                BidConst.BIDREQUEST_STATE_PAYING_BACK,
                BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK
        });
        qo.setCurrentPage(1);
        qo.setPageSize(5);
        model.addAttribute("bidRequests", bidRequestService.selectBidRequestsPage(qo, BidConst.BIDREQUEST_TYPE_NORMAL));
        model.addAttribute("expBidRequests", bidRequestService.selectBidRequestsPage(qo, BidConst.BIDREQUEST_TYPE_EXP));
        return "main";
    }

    @RequestMapping("/invest")
    public String invest() {
        return "invest";
    }

    @RequestMapping("/invest_list")
    public String investList(BidRequestQueryObject qo, Model model) {
        qo.setStates(new int[]{
                BidConst.BIDREQUEST_STATE_BIDDING,
                BidConst.BIDREQUEST_STATE_PAYING_BACK,
                BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK
        });
        qo.setOrderByType(" bidRequestState desc");
        model.addAttribute("pageResult", bidRequestService.queryPage(qo));
        return "invest_list";
    }

    @RequestMapping("/borrow_info")
    public String borrowInfo(Long id, Model model) {
        BidRequest bidRequest = bidRequestService.get(id);
        String returnPage = null;
        if (bidRequest != null) {
            Userinfo createUser = userinfoService.get(bidRequest.getCreateUser().getId());
            model.addAttribute("bidRequest", bidRequest);
            if (bidRequest.getBidRequestType() == BidConst.BIDREQUEST_TYPE_NORMAL) {
                model.addAttribute("userInfo", createUser);
                model.addAttribute("realAuth", realAuthService.get(createUser.getRealAuthId()));
                model.addAttribute("userFiles", userFileService.queryAuditListByLoginInfo(bidRequest.getCreateUser().getId()));
                returnPage = "borrow_info";
            } else {
                returnPage = "exp_borrow_info";
            }
        }
        //判断是否登录
        if (UserContext.getCurrent() != null) {
            //判断是否是投标本人
            if (bidRequest.getCreateUser().getId().equals(UserContext.getCurrent().getId())) {
                model.addAttribute("self", true);
            } else {
                //不是本人
                if (bidRequest.getBidRequestType() == BidConst.BIDREQUEST_TYPE_NORMAL) {
                model.addAttribute("self", false);
                    model.addAttribute("account", accountService.get(UserContext.getCurrent().getId()));//之前写成userinfo.get 气死我了
                } else {
                    model.addAttribute("expAccount", exAaccountService.get(UserContext.getCurrent().getId()));
                }
            }
        } else {
            //没有登录
            model.addAttribute("self", false);
        }
        return returnPage;
    }
}
