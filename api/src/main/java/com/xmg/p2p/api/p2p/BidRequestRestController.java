package com.xmg.p2p.api.p2p;

import com.alibaba.fastjson.JSON;
import com.xmg.p2p.api.p2p.service.ITokenService;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.util.AjaxResult;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.Bid;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bidRequests/")
public class BidRequestRestController {
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private ITokenService tokenService;

    /**
     * 借款列表
     * @param qo
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public PageResult getList(BidRequestQueryObject qo){
        qo.setStates(new int[]{BidConst.BIDREQUEST_STATE_PAYING_BACK,BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK,BidConst.BIDREQUEST_STATE_BIDDING});
         return bidRequestService.queryPage(qo);
    }
    /**
     * 借款详情
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Map<String,Object> getDetails(@PathVariable("id")Long id){
      return   JSON.parseObject(bidRequestService.get(id).getJsonString());
    }

    /**
     * 获取投资列表
     */
    @RequestMapping(method = RequestMethod.GET,value = "/{id}/bids/")
    public List<Bid> getBids(@PathVariable("id") Long id){
        return bidRequestService.get(id).getBids();
    }

    /**
     * 投标业务
     */
    @RequestMapping(value = "/{id}/bids/",method = RequestMethod.POST)
    public AjaxResult bids(@PathVariable("id")Long id, BigDecimal amount, HttpServletRequest request){
        //投标一定是要拿到当前登录的用户
        String token = request.getHeader(ITokenService.TOKEN_IN_HEADER);
        Long userId=tokenService.findToken(token);
        LoginInfo bidUser=new LoginInfo();
        bidUser.setId(userId);
        bidRequestService.bid(id,amount,bidUser);
        return new AjaxResult();
    }
}
