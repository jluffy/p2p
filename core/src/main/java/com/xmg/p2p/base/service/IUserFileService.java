package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.UserFileQueryObject;

import java.util.List;

public interface IUserFileService {
    PageResult queryPage(UserFileQueryObject qo);
    int save(UserFile userFile);
    int update(UserFile userFile);
    void apply(String imagePath);
    void choiceType(Long[] id, Long[] fileType);

    List<UserFile> selectFileTypeList(boolean isFileType);

    void audit(Long id, int score, int state, String remark);

    List<UserFile> queryAuditListByLoginInfo(Long id);
}
