package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.MailVerify;
import com.xmg.p2p.base.mapper.MailVerifyMapper;
import com.xmg.p2p.base.service.IMailVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailVerifyServiceImpl implements IMailVerifyService {
	@Autowired
	private MailVerifyMapper mailVerifyMapper;

	@Override
	public int save(MailVerify record) {
		return mailVerifyMapper.insert(record);
	}

	@Override
	public MailVerify selectByUUID(String uuid) {
		return mailVerifyMapper.selectByUUID(uuid);

	}
}
