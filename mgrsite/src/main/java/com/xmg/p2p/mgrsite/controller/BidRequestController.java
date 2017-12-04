package com.xmg.p2p.mgrsite.controller;

import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.AjaxResult;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.service.IBidRequestAuditHistoryService;
import com.xmg.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
* 后台借款页面
* */
@Controller
public class BidRequestController {
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IUserFileService userFileService;

    @RequestMapping("/bidrequest_publishaudit_list")
    public String index(Model model, @ModelAttribute("qo") BidRequestQueryObject qo) {
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);//只查询发表前审核状态的
        model.addAttribute("pageResult", bidRequestService.queryPage(qo));
        return "bidrequest/publish_audit";
    }

    //http://localhost:8083/bidrequest_publishaudit?id=2&remark=&state=2
    //借款的后台审核
    @RequestMapping("/bidrequest_publishaudit")
    @ResponseBody
    public AjaxResult borrowAudit(Long id, String remark, int state) {
        AjaxResult result = null;
        try {
            bidRequestService.borrowAudit(id, remark, state);//bidrequest是不含有审核相关信息的
            result = new AjaxResult();
        } catch (Exception e) {
            result = new AjaxResult(false, "审核未通过,原因:" + remark);
        }
        return result;
    }

    /**
     * 后台借款明细
     */
    @RequestMapping("/borrow_info")
    public String borrowInfoPage(Long id, Model model) {
        BidRequest bidRequest = bidRequestService.get(id);
        String returnTypePage=null;
        if (bidRequest != null) {
            Userinfo createUser = userinfoService.get(bidRequest.getCreateUser().getId());
            //借款对象
            model.addAttribute("bidRequest", bidRequest);
            model.addAttribute("audits", bidRequestAuditHistoryService.queryListByRequest(bidRequest.getId()));
            //实名认证信息
            if (bidRequest.getBidRequestType() == BidConst.BIDREQUEST_TYPE_NORMAL) {
                //用户基本信息
                model.addAttribute("userInfo", createUser);
                //投标审核历史
                model.addAttribute("realAuth", realAuthService.get(createUser.getRealAuthId()));
                //风控材料
                model.addAttribute("userFiles", userFileService.queryAuditListByLoginInfo(bidRequest.getCreateUser().getId()));
                returnTypePage="bidrequest/borrow_info";
            }else if(bidRequest.getBidRequestType()==BidConst.BIDREQUEST_TYPE_EXP){
                returnTypePage="expbidrequest/borrow_info";
            }
        }
        return  returnTypePage;
    }

    //满标一审
    @RequestMapping("/bidrequest_audit1_list")
    public String indexAudit1(Model model, @ModelAttribute("qo") BidRequestQueryObject qo) {
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);//只查询发表前审核状态的
        model.addAttribute("pageResult", bidRequestService.queryPage(qo));
        return "bidrequest/audit1";
    }

    @RequestMapping("/bidrequest_audit1")
    @ResponseBody
    public AjaxResult bidRequestAudit1(Long id, String remark, int state) {
        AjaxResult result = null;
        try {
            bidRequestService.borrowAudit1(id, remark, state);//bidrequest是不含有审核相关信息的
            result = new AjaxResult();
        } catch (Exception e) {
            result = new AjaxResult(false, "审核未通过,原因:" + remark);
        }
        return result;
    }

    //满标2审
    @RequestMapping("/bidrequest_audit2_list")
    public String indexAudit2(Model model, @ModelAttribute("qo") BidRequestQueryObject qo) {
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);//只查询发表前审核状态的
        model.addAttribute("pageResult", bidRequestService.queryPage(qo));
        return "bidrequest/audit2";
    }

    @RequestMapping("/bidrequest_audit2")
    @ResponseBody
    public AjaxResult bidRequestAudit2(Long id, String remark, int state) {
        AjaxResult result = null;
        try {
            bidRequestService.borrowAudit2(id, remark, state);//bidrequest是不含有审核相关信息的
            result = new AjaxResult();
        } catch (Exception e) {
            result = new AjaxResult(false, "审核未通过,原因:" + remark);
        }
        return result;
    }

}
