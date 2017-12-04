package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.service.ISystemDictionaryItemService;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.util.AjaxResult;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.website.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class UserFileController {
    @Value("${uploadPath}")
    private String uploadPath;
    @Autowired
    ISystemDictionaryItemService systemDictionaryItemService;
    @Autowired
    IUserFileService userFileService;

    @RequestMapping("/userFile")
    public String userFile(Model model) {
        model.addAttribute("sessionid", UserContext.getRequest().getSession().getId());
        List<UserFile> unselectFileList = userFileService.selectFileTypeList(false);//查出没有选风控的
        if (unselectFileList.size() == 0) {//说明都填写了风控资料了 或者没传数据 跳转到等待审核界面
            model.addAttribute("userFiles", userFileService.selectFileTypeList(true));
            return "userFiles";
        } else {//有没选风控的
            //1查询出没有风控类型的数据
            model.addAttribute("userFiles", unselectFileList);
            //查询出风控类型的列表集合
            model.addAttribute("fileTypes", systemDictionaryItemService.queryListBySn("userFileType"));
            return "userFiles_commit";
        }
    }

    @ResponseBody
    @RequestMapping("/userFileUpload")
    public String userFileUpload(MultipartFile file) {
        //1上传文件到本地
        String imagePath = UploadUtil.upload(file, uploadPath);
        //保存为一条风控材料的记录
        userFileService.apply(imagePath);
        return imagePath;
    }

    @RequestMapping("/userFile_selectType")
    @ResponseBody
    public AjaxResult selectType(Long[] id, Long[] fileType) {
        AjaxResult result = null;
        try {
            userFileService.choiceType(id, fileType);
            result = new AjaxResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new AjaxResult(false, e.getMessage());
        }
        return result;
    }

}
