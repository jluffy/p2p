package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.MailVerify;

public interface IMailVerifyService {
    int save(MailVerify record);
    MailVerify selectByUUID(String uuid);
}
