package com.xmg.p2p.api.p2p.service;

import com.xmg.p2p.base.domain.LoginInfo;

public interface ITokenService {
    public static final String TOKEN_IN_HEADER="token_in_header";
    public String createToken(LoginInfo loginInfo);

    void destroyToken(String token);

    /**
     * 根据令牌获取用户信息
     * @param token
     * @return
     */
    Long findToken(String token);
}
