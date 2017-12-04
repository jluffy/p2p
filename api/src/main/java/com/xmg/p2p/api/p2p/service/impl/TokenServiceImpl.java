package com.xmg.p2p.api.p2p.service.impl;

import com.xmg.p2p.api.p2p.service.ITokenService;
import com.xmg.p2p.base.domain.LoginInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Service
public class TokenServiceImpl implements ITokenService {
    //缓存token
    public static  Map<String,Long> tokens=new HashMap<>();
    @Override
    public String createToken(LoginInfo loginInfo) {
        String token= UUID.randomUUID().toString();
        Long id=loginInfo.getId();
        tokens.put(token,id);
        return token;
    }

    @Override
    public void destroyToken(String token) {
        tokens.remove(token);
    }

    @Override
    public Long findToken(String token) {
        return tokens.get(token);
    }
}
