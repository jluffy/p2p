package com.xmg.p2p.mgrsite.controller;

import com.xmg.p2p.base.util.AjaxResult;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 体验金
 */
@Controller
public class ExpBidRequestController {
    @Autowired
    private IBidRequestService bidRequestService;

    @RequestMapping("/expBidRequestPublishPage")
    public String expIndex(Model model) {
        model.addAttribute("minBidRequestAmount", BidConst.SMALLEST_BIDREQUEST_AMOUNT);
        model.addAttribute("minBidAmount", BidConst.SMALLEST_BID_AMOUNT);
        return "expbidrequest/expbidrequestpublish";
    }

    @RequestMapping("/expBidRequestPublish")
    @ResponseBody
    public AjaxResult expBidRequestPublish(BidRequest bidRequest) {
        AjaxResult result = null;
        try {
            bidRequestService.expApply(bidRequest);
            result = new AjaxResult();
        } catch (Exception e) {
            result = new AjaxResult(false, "数据字典数据存储失败");
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/expBidRequest_list")
    public String expBidRequestList(@ModelAttribute("qo") BidRequestQueryObject qo, Model model) {
        qo.setBidRequestType(BidConst.BIDREQUEST_TYPE_EXP);
        model.addAttribute("pageResult", bidRequestService.queryPage(qo));
        return "expbidrequest/expbidrequestlist";
    }
}


