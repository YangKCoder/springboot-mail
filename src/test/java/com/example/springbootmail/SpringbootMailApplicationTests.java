package com.example.springbootmail;

import com.example.springbootmail.service.MailService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import javax.mail.MessagingException;
import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringbootMailApplicationTests {

    @Value("${spring.mail.username}")
    private String from;


    @Autowired
    public MailService mailService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void sendSimpleMail() {
        //i like you
        String to = "494040246@qq.com";
        String subject = "简单邮件发送";
        String text = "我是文本！！！";

        mailService.sendSimpleText(to, from, subject, text);
    }

    @Test
    public void sendAttachmentMail() {

        try {
            File file = ResourceUtils.getFile("classpath:file.txt");
            String to = "494040246@qq.com";
            String subject = "附件邮件发送";
            String text = "我是文本！！！";
            mailService.sendAttachmentMail(to, from, subject, text, file);
        } catch (FileNotFoundException e) {
            log.error("找不到文件:【{}】", e);
        } catch (MessagingException e) {
            log.error("邮件发送失败：【{}】", e);
        }

    }


    @Test
    public void sendHTMLMail() {
        String to = "494040246@qq.com";
        String subject = "HTML邮件发送";
        try {
            mailService.sendHtmlAttachment(to, from, subject);
        } catch (Exception e) {
            log.error("邮件发送失败：【{}】", e);
        }
    }

}
