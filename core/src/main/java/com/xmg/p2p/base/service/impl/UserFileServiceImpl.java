package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.mapper.UserFileMapper;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.UserFileQueryObject;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserFileServiceImpl implements IUserFileService {
    @Autowired
    private UserFileMapper userFileMapper;
    @Autowired
    IUserinfoService userinfoService;

    @Override
    public PageResult queryPage(UserFileQueryObject qo) {
        Long count = userFileMapper.queryPageCount(qo);
        if (count <= 0) {
            return PageResult.empty(qo.getPageSize());
        }
        List<UserFile> listData = userFileMapper.queryPageData(qo);
        return new PageResult(listData, count.intValue(), qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public int save(UserFile userFile) {
        return userFileMapper.insert(userFile);
    }

    @Override
    public int update(UserFile userFile) {
        return userFileMapper.updateByPrimaryKey(userFile);
    }

    @Override
    public void apply(String imagePath) {
        UserFile userFile = new UserFile();
        userFile.setImage(imagePath);
        userFile.setApplier(UserContext.getCurrent());
        userFile.setApplyTime(new Date());
        userFile.setState(UserFile.STATE_NORMAL);
        this.save(userFile);
    }


    @Override
    public void choiceType(Long[] ids, Long[] fileTypes) {
        if (ids != null && fileTypes != null && ids.length == fileTypes.length) {
            UserFile userFile = null;
            SystemDictionaryItem fileType = null;
            for (int i = 0; i < ids.length; i++) {
                userFile = userFileMapper.selectByPrimaryKey(ids[i]);
                if (userFile.getApplier().getId().equals(UserContext.getCurrent().getId())) {
                    fileType = new SystemDictionaryItem();
                    fileType.setId(fileTypes[i]);
                    userFile.setFileType(fileType);
                    this.update(userFile);
                }
            }
        }
    }

    @Override
    public List<UserFile> selectFileTypeList(boolean isFileType) {
        return  userFileMapper.selectFileTypeList(UserContext.getCurrent().getId(),isFileType);
    }

    @Override
    public void audit(Long id, int score, int state, String remark) {
        //根据id 查询数据库对应的风控材料对象
        UserFile userFile=userFileMapper.selectByPrimaryKey(id);
        if(userFile !=null&&userFile.getState()==UserFile.STATE_NORMAL){
            userFile.setAuditor(UserContext.getCurrent());
            userFile.setAuditTime(new Date());
            userFile.setRemark(remark);
            if(state==UserFile.STATE_PASS){//点击了审核通过按钮
               //设置分数
               userFile.setScore(score);
               userFile.setState(UserFile.STATE_PASS);
               //给userinfo的冗余字段score 增加分数
                Userinfo applyUserinfo=userinfoService.get(userFile.getApplier().getId());
                applyUserinfo.setScore(applyUserinfo.getScore()+score);
                userinfoService.update(applyUserinfo);
            }else{
                //点击拒绝审核
                userFile.setState(UserFile.STATE_REJECT);
            }
            userFileMapper.updateByPrimaryKey(userFile);
        }
    }

    @Override
    public List<UserFile> queryAuditListByLoginInfo(Long loginInfoId) {
        return userFileMapper.queryAuditListByLoginInfo(loginInfoId,UserFile.STATE_PASS );
    }
}

