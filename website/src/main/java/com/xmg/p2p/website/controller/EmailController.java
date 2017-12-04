package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.domain.MailVerify;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IMailVerifyService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.AjaxResult;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.UUID;

@Controller
public class EmailController {
    //验证邮箱的地址
    @Value("${email.applicationBindEamilUrl}")
    private String applicationBindEmailUrl;
    @Autowired
    IMailVerifyService mailVerifyService;
    @Autowired
    IUserinfoService userinfoService;
    @Autowired
    JavaMailSender javaMailSender;//springboot内置邮件发送对象

    @RequestMapping("/sendEmail")
    @ResponseBody
    public AjaxResult sendEmail(String email) {
        AjaxResult result = null;
        try {
            //创建uuid
            String uuid = UUID.randomUUID().toString();
            //拼接邮件的内容(html)
            StringBuilder info = new StringBuilder();
            // "<a href='localhost/checkEmail'/>"
            info.append("感谢您注册我们的p2p网站,请点击<a href='")
                    .append(applicationBindEmailUrl)
                    .append(uuid)
                    .append("'>这里</a>完成验证,验证连接的有效时间为")
                    .append(BidConst.EMAIL_VALID_DAY)
                    .append("天,如果是你本人操作,请尽快在有效期内完成验证,如果不是您本人的操作请忽略次邮件");
            //发送邮件
            //System.out.println(info);
            sendRealEamil(email,info.toString());
            //保存邮箱数据入库包括邮件邮箱地址,发送邮件的时间,uuid和操作的用户
            Userinfo userinfo = userinfoService.get(UserContext.getCurrent().getId());
            MailVerify mailVerify = new MailVerify();
            mailVerify.setEmail(email);
            mailVerify.setSendTime(new Date());
            mailVerify.setUserInfoId(userinfo.getId());
            mailVerify.setUuid(uuid);
            //保存数据入库
            mailVerifyService.save(mailVerify);
            result = new AjaxResult("邮件验证信息发送成功,请查看邮件进行确认");
        } catch (Exception e) {
            result = new AjaxResult("邮箱验证发送验证信息失败");
            e.printStackTrace();
        }
        return result;
    }
    private void  sendRealEamil(String email,String content) throws Exception{
        //1创建message消息体
        MimeMessage mimeMessage =javaMailSender.createMimeMessage();
        //2创建helper
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
        //3配置收件人
        helper.setTo(email);
        //4配置发送人
        helper.setFrom("");
        //5设置邮件标题
        helper.setSubject("测试邮件");
        //6设置内容
        helper.setText(content,true);//true表示是一个html的文本
        //7发送邮件
        javaMailSender.send(mimeMessage);
    }
}
